/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.functions;

import coordinate.memory.NativeInteger;
import static java.lang.Math.min;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.stream.LongStream;

/**
 *
 * @author user
 * 
 * transform, exclusiveScan, reduce, partition, sort pairs
 * 
 */
public class ParallelNative {
    
    public static NativeInteger transform(NativeInteger input, IntFunction<Integer> function)
    {
        LongStream.range(0, input.capacity())
                .parallel()
                .forEach(i->{
                    input.set(i, function.apply(input.get(i)));                    
                });
        return input;
    }
    
    public static void reduce(NativeInteger input)
    {
        SerialNative.reduce(input);
    }
    
    public static int exclusiveScan(NativeInteger input)
    {
        NativeInteger output = new NativeInteger(input.capacity());
        int total = exclusiveScan(input, output);
        input.copyFrom(output);
        return total;
    }
    
    public static int exclusiveScan(NativeInteger input, NativeInteger output)
    {
        return SerialNative.exclusiveScan(input, output);
    }
    
    public static int partition(NativeInteger values, NativeInteger result, long n, NativeInteger flags) 
    {    
        NativeInteger stencil_1 = transform(flags.copy(), i -> i != 0 ? 1 : 0);
        NativeInteger stencil_2 = transform(stencil_1.copy(), i -> i != 0 ? 0 : 1);
        int st1 = exclusiveScan(stencil_1);
        int st2 = exclusiveScan(stencil_2);
                
        LongStream.range(0, n)
                .parallel()
                .forEach(i->{
                    if(flags.get(i) != 0)                      
                        result.set(stencil_1.get(i), values.get(i));              
                });
        LongStream.range(0, n)
                .parallel()
                .forEach(i->{ 
                    if(flags.get(i) == 0)                    
                        result.set(stencil_2.get(i) + st1, values.get(i));                    
                });
        if((st1 + st2) > result.capacity())
            throw new IndexOutOfBoundsException("Issue with partition");    
        
        return st1;
    }
    
    //Parallel Butterfly Sorting Algorithm on GPU by Bilal et al    
    public static void sort_pair(NativeInteger keys, NativeInteger values, BiPredicate<Integer, Integer> op)
    {
        long radix  = 2;
        long toIndex = min(keys.capacity(), values.capacity());
        long until = until(toIndex);
        long sizeList = toIndex;
        long T = (long) (Math.pow(radix, until)/radix);//data.length/radix if n is power of 2;
        
        for(long xout = 1; xout<=until; xout++)
        {            
            double[] PowerX = new double[]{Math.pow(radix, xout)};
            LongStream.range(0, T)
                    .parallel()
                    .forEach(t->{                        
                        if(t >= sizeList)
                            return;
                        
                        int yIndex      = (int) (t/(PowerX[0]/radix));  
                        int kIndex      = (int) (t%(PowerX[0]/radix));
                        int PosStart    = (int) (kIndex + yIndex * PowerX[0]);
                        int PosEnd      = (int) (PowerX[0] - kIndex - 1 + yIndex * PowerX[0]);
                        
                        if(!isInRange(PosStart, 0, toIndex)) 
                            return;
                        if(!isInRange(PosEnd, 0, toIndex)) 
                            return;
                        
                        if(op.test(keys.get(PosStart), keys.get(PosEnd)))
                        {
                            keys.swapElement(PosStart, PosEnd);
                            values.swapElement(PosStart, PosEnd);
                        }
                    });
            if(xout > 1)
            {                
                for(long xin = xout; xin > 0; xin--)
                {
                    PowerX[0] = (Math.pow(radix, xin));
                    LongStream.range(0, T)
                        .parallel()
                        .forEach(t->{      
                            if(t >= sizeList)
                                return;
                            
                            int yIndex      = (int) (t/(PowerX[0]/radix));  
                            int kIndex      = (int) (t%(PowerX[0]/radix));
                            int PosStart    = (int) (kIndex + yIndex * PowerX[0]);
                            int PosEnd      = (int) (kIndex + yIndex * PowerX[0] + PowerX[0]/radix);
                            
                            if(!isInRange(PosStart, 0, toIndex)) 
                                return;
                            if(!isInRange(PosEnd, 0, toIndex)) 
                                return;

                            if(op.test(keys.get(PosStart), keys.get(PosEnd)))
                            {
                                keys.swapElement(PosStart, PosEnd);
                                values.swapElement(PosStart, PosEnd);
                            }
                        });
                }
            }                    
        }
    }
    
    //Parallel Butterfly Sorting Algorithm on GPU by Bilal et al    
    public static void sort(NativeInteger input, BiPredicate<Integer, Integer> op)
    {
        long radix  = 2;
        long toIndex = input.capacity();
        long until = until(toIndex);
        long sizeList = toIndex;
        long T = (long) (Math.pow(radix, until)/radix);//data.length/radix if n is power of 2;
        
        for(long xout = 1; xout<=until; xout++)
        {            
            double[] PowerX = new double[]{Math.pow(radix, xout)};
            LongStream.range(0, T)
                    .parallel()
                    .forEach(t->{                        
                        if(t >= sizeList)
                            return;
                        
                        int yIndex      = (int) (t/(PowerX[0]/radix));  
                        int kIndex      = (int) (t%(PowerX[0]/radix));
                        int PosStart    = (int) (kIndex + yIndex * PowerX[0]);
                        int PosEnd      = (int) (PowerX[0] - kIndex - 1 + yIndex * PowerX[0]);
                        
                        if(!isInRange(PosStart, 0, toIndex)) 
                            return;
                        if(!isInRange(PosEnd, 0, toIndex)) 
                            return;
                        
                        if(op.test(input.get(PosStart), input.get(PosEnd)))
                        {
                            input.swapElement(PosStart, PosEnd);                            
                        }
                    });
            if(xout > 1)
            {                
                for(long xin = xout; xin > 0; xin--)
                {
                    PowerX[0] = (Math.pow(radix, xin));
                    LongStream.range(0, T)
                        .parallel()
                        .forEach(t->{      
                            if(t >= sizeList)
                                return;
                            
                            int yIndex      = (int) (t/(PowerX[0]/radix));  
                            int kIndex      = (int) (t%(PowerX[0]/radix));
                            int PosStart    = (int) (kIndex + yIndex * PowerX[0]);
                            int PosEnd      = (int) (kIndex + yIndex * PowerX[0] + PowerX[0]/radix);
                            
                            if(!isInRange(PosStart, 0, toIndex)) 
                                return;
                            if(!isInRange(PosEnd, 0, toIndex)) 
                                return;

                            if(op.test(input.get(PosStart), input.get(PosEnd)))
                            {
                                input.swapElement(PosStart, PosEnd);                                
                            }
                        });
                }
            }                    
        }
    }
    
    private static long until(long size)
    {
        long log2 = log2nlz(size);
        long difference = (long) (Math.pow(2, log2) - size);

        if(difference == 0) return log2;
        else                return log2+1;
    }
    
    //log2
    private static long log2nlz(long bits )
    {
        if( bits == 0 )
            throw new UnsupportedOperationException("value should be zero");
        return 63 - Long.numberOfLeadingZeros( bits );
    }
    
    //toIndex is exclusive
    protected static final boolean isInRange(long index, long fromIndex, long toIndex)
    {
        if(index < fromIndex)
            return false;
        else return index < toIndex;
    }
}
