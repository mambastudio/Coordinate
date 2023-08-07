/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.algorithms;

import coordinate.memory.NativeInteger;
import coordinate.memory.NativeObject;
import coordinate.memory.NativeObject.Element;
import coordinate.utility.RangeCheck;
import coordinate.utility.Utility;
import static java.lang.Math.min;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        transform(input, function, input.capacity(), input);
        return input;
    }
    
    public static NativeInteger transform(NativeInteger input, IntFunction<Integer> function, long n, NativeInteger output)
    {
        RangeCheck.rangeCheckBound(0, n, input.capacity());
        RangeCheck.rangeCheckBound(0, n, output.capacity());
        LongStream.range(0, n)
                .parallel()
                .forEach(i->{
                    output.set(i, function.apply(input.get(i)));                    
                });
        return output;
    }
    
    public static<T extends Element<T>> NativeInteger transform(NativeObject<T> input, Function<T, Integer> function)
    {
        return transform(input, function, input.capacity(), new NativeInteger(input.capacity()));
    }
    
    public static<T extends Element<T>> NativeInteger transform(NativeObject<T> input, Function<T, Integer> function, long n, NativeInteger output)
    {
        RangeCheck.rangeCheckBound(0, n, input.capacity());
        RangeCheck.rangeCheckBound(0, n, output.capacity());
        LongStream.range(0, n)
                .parallel()
                .forEach(i->{
                    output.set(i, function.apply(input.get(i)));                    
                });
        return output;
    }
    
    public static Stream<Integer> streamInteger(NativeInteger nativeInteger)
    {
        return StreamSupport.stream(new NativeIntegerSpliterator(nativeInteger), true);
    }
    
    public static int reduce(NativeInteger input)
    {
        return SerialNative.reduce(input);
    }
    
    public static int exclusiveScan(NativeInteger input)
    {
        NativeInteger output = new NativeInteger(input.capacity());
        int total = exclusiveScan(input, output);
        input.copyFromMem(output);
        return total;
    }
    
    public static int exclusiveScan(NativeInteger input, int n, NativeInteger output)
    {
        return SerialNative.exclusiveScan(input, n, output);
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
    public static void sort_pair(
            NativeInteger keys,     NativeInteger values, 
            NativeInteger keysOut,  NativeInteger valuesOut, 
            long n,
            BiPredicate<Integer, Integer> op)
    {
        RangeCheck.rangeCheckBound(0, keys.capacity(), n);
        RangeCheck.rangeCheckBound(0, values.capacity(), n);
        RangeCheck.rangeCheckBound(0, keysOut.capacity(), n);
        RangeCheck.rangeCheckBound(0, valuesOut.capacity(), n);
        
        //transfer copy to memory
        if(!keys.isSame(keysOut))
            keys.copyToMem(keysOut, n);
        if(!values.isSame(valuesOut))
            values.copyToMem(valuesOut, n);
        
        long radix  = 2;
        long toIndex = n;
        long next_log2 = Utility.next_log2(toIndex);        
        long sizeList = toIndex;
        long T = (long) (Math.pow(radix, next_log2)/radix);//data.length/radix if n is power of 2;
        
        for(long xout = 1; xout<=next_log2; xout++)
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
                        
                        if(op.test(keysOut.get(PosStart), keysOut.get(PosEnd)))
                        {
                            keysOut.swapElement(PosStart, PosEnd);
                            valuesOut.swapElement(PosStart, PosEnd);
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

                            if(op.test(keysOut.get(PosStart), keysOut.get(PosEnd)))
                            {
                                keysOut.swapElement(PosStart, PosEnd);
                                valuesOut.swapElement(PosStart, PosEnd);
                            }
                        });
                }
            }                    
        }
    }
        
    public static void sort_pair(NativeInteger keys, NativeInteger values, BiPredicate<Integer, Integer> op)
    {
        RangeCheck.rangeAboveZero(keys.capacity());
        RangeCheck.rangeAboveZero(values.capacity());
        
        long n = min(keys.capacity(), values.capacity());
        sort_pair(keys, values, keys, values, n, op);
    }
    
    //Parallel Butterfly Sorting Algorithm on GPU by Bilal et al    
    public static void sort(NativeInteger input, BiPredicate<Integer, Integer> op)
    {
        long radix  = 2;
        long toIndex = input.capacity();
        long next_log2 = Utility.next_log2(toIndex);
        long sizeList = toIndex;
        long T = (long) (Math.pow(radix, next_log2)/radix);//data.length/radix if n is power of 2;
        
        for(long xout = 1; xout<=next_log2; xout++)
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
        
    //toIndex is exclusive
    protected static final boolean isInRange(long index, long fromIndex, long toIndex)
    {
        if(index < fromIndex)
            return false;
        else return index < toIndex;
    }
}
