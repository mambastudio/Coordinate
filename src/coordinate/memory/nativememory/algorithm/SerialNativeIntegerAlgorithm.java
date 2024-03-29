/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.nativememory.algorithm;

import coordinate.memory.nativememory.NativeInteger;
import coordinate.utility.RangeCheck;
import coordinate.utility.Utility;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.stream.LongStream;

/**
 *
 * @author jmburu
 */
public class SerialNativeIntegerAlgorithm implements NativeIntegerAlgorithm{

    @Override
    public NativeInteger transform(NativeInteger values, IntUnaryOperator f) {
        RangeCheck.rangeAboveZero(values.capacity());        
        NativeInteger results = new NativeInteger(values.capacity());
        for(long i = 0; i<values.capacity(); i++)
            results.set(i, f.applyAsInt(values.get(i)));
        return results;
    }

    @Override
    public Integer exclusive_scan(NativeInteger values, long n, NativeInteger result, IntBinaryOperator f) {
        RangeCheck.rangeAboveZero(n);
        RangeCheck.checkBound(0, n, values.capacity());
        RangeCheck.checkBound(0, n, result.capacity());
          
        result.set(0, 0); // just in case it's not zero
        for(long i = 1; i<n; i++)
            result.set(i, f.applyAsInt(values.get(i-1), result.get(i-1)));        
        return result.get(n-1);        
    }

    @Override
    public Integer reduce(NativeInteger values, long n, NativeInteger result, IntBinaryOperator f) {
        RangeCheck.rangeAboveZero(n);
        RangeCheck.checkBound(0, n, values.capacity());
        int res = 0;
        for(long i = 0; i<n; i++)
            res = f.applyAsInt(res, values.get(i));
        result.set(0, res);
        return res;
    }

    /**
     * Parallel mapping approach, but with an exclusive scan that is serial
     * 
     * @param values
     * @param result
     * @param n
     * @param flags
     * @return 
     */
        
    @Override
    public Integer partition(NativeInteger values, NativeInteger result, long n, NativeInteger flags) {
        RangeCheck.rangeAboveZero(n);
        RangeCheck.checkBound(0, n, values.capacity());
        RangeCheck.checkBound(0, n, result.capacity());
        
        NativeInteger stencil_1 = transform(flags, i -> i != 0 ? 1 : 0);
        NativeInteger stencil_2 = transform(stencil_1, i -> i != 0 ? 0 : 1);
        
        NativeInteger stl_1_out = new NativeInteger(stencil_1.capacity()); //initialised zero automatically
        NativeInteger stl_2_out = new NativeInteger(stencil_2.capacity());
                
        int st1_total = exclusive_scan(stencil_1, n, stl_1_out) + stencil_1.get(n-1); 
        int st2_total = exclusive_scan(stencil_2, n, stl_2_out) + stencil_2.get(n-1);
                
        LongStream.range(0, n)
                .parallel()
                .forEach(i->{
                    if(flags.get(i) != 0)                      
                        result.set(stl_1_out.get(i), values.get(i));              
                });
        LongStream.range(0, n)
                .parallel()
                .forEach(i->{ 
                    if(flags.get(i) == 0)                    
                        result.set(stl_2_out.get(i) + st1_total, values.get(i));                    
                });
        if((st1_total + st2_total) > n)
            throw new IndexOutOfBoundsException("Issue with partition");  
        
        stencil_1.freeMemory();
        stencil_2.freeMemory();
        
        stl_1_out.freeMemory();
        stl_2_out.freeMemory();
        
        return st1_total;
    }

    /**
     * Actually using stream/forkjoinpool, hence parallel (performance test not done), 
     * but algorithm is suited for GPU based on the following wonderful paper:
     * 
     * Parallel Butterfly Sorting Algorithm on GPU by Bilal et al  
     * 
     * @param keys
     * @param values
     * @param keysOut
     * @param valuesOut
     * @param n
    */
    @Override
    public void sort_pairs(NativeInteger keys, NativeInteger values, NativeInteger keysOut, NativeInteger valuesOut, long n) {
        RangeCheck.checkBound(0, keys.capacity(), n);
        RangeCheck.checkBound(0, values.capacity(), n);
        RangeCheck.checkBound(0, keysOut.capacity(), n);
        RangeCheck.checkBound(0, valuesOut.capacity(), n);
        
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
                        
                        if(!RangeCheck.isRangeInBound(PosStart, 0, toIndex)) 
                            return;
                        if(!RangeCheck.isRangeInBound(PosEnd, 0, toIndex)) 
                            return;
                        
                        if(keysOut.get(PosStart) > keysOut.get(PosEnd))
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
                            
                            if(!RangeCheck.isRangeInBound(PosStart, 0, toIndex)) 
                                return;
                            if(!RangeCheck.isRangeInBound(PosEnd, 0, toIndex)) 
                                return;

                            if(keysOut.get(PosStart) > keysOut.get(PosEnd))
                            {
                                keysOut.swapElement(PosStart, PosEnd);
                                valuesOut.swapElement(PosStart, PosEnd);
                            }
                        });
                }
            }                    
        }
    }    
}
