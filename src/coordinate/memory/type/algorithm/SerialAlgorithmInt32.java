/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.type.algorithm;

import static coordinate.memory.type.MemoryRegion.checkSameByteCapacity;
import coordinate.memory.type.MemoryStruct;
import coordinate.memory.type.MemoryStructFactory.Int32;
import coordinate.utility.RangeCheckArray;
import coordinate.utility.Utility;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.stream.LongStream;

/**
 *
 * @author user
 */
public class SerialAlgorithmInt32 {
    public MemoryStruct<Int32> transform(MemoryStruct<Int32> values, IntUnaryOperator f) {
        RangeCheckArray.validateIndexAboveZero(values.size());        
        MemoryStruct<Int32> results = new MemoryStruct(new Int32(), values.size());
        for(long i = 0; i<values.size(); i++)
            results.set(i, new Int32(f.applyAsInt(values.get(i).value())));
        return results;
    }
        
    public int exclusive_scan(MemoryStruct<Int32> values, long n, MemoryStruct<Int32> result, IntBinaryOperator f) {
        //condition check result.size() > size values.size()
        if(result.size() <= values.size())
            throw new UnsupportedOperationException("result array is lesser than values size");
        RangeCheckArray.validateIndexAboveZero(n);        
        RangeCheckArray.validateRangeSize(0, n, values.size());
        RangeCheckArray.validateRangeSize(0, n, result.size());
          
        result.set(0, new Int32()); // just in case it's not zero
        for(long i = 1; i<n; i++)
            result.set(i, new Int32(f.applyAsInt(values.get(i-1).value(), result.get(i-1).value())));        
        return result.get(n-1).value();        
    }
       
    public int exclusive_scan(MemoryStruct<Int32> values, long n, MemoryStruct<Int32> results){
        return this.exclusive_scan(values, n, results, (a, b)-> a + b);
    }

   
    public int reduce(MemoryStruct<Int32> values, long n, MemoryStruct<Int32> result, IntBinaryOperator f) {
        RangeCheckArray.validateIndexAboveZero(n);
        RangeCheckArray.validateRangeSize(0, n, values.size());
        RangeCheckArray.validateRangeSize(0, n, result.size());
        
        int res = 0;
        for(long i = 0; i<n; i++)
            res = f.applyAsInt(res, values.get(i).value());
        result.set(0, new Int32(res)); //result is one value or array size 1
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
    public int partition(MemoryStruct<Int32> values, MemoryStruct<Int32> result, long n, MemoryStruct<Int32> flags) {
        RangeCheckArray.validateIndexAboveZero(n);
        RangeCheckArray.validateRangeSize(0, n, values.size());
        RangeCheckArray.validateRangeSize(0, n, result.size());
        
        MemoryStruct<Int32> stencil_1 = transform(flags, i -> i != 0 ? 1 : 0);
        MemoryStruct<Int32> stencil_2 = transform(stencil_1, i -> i != 0 ? 0 : 1);
        
        MemoryStruct<Int32> stl_1_out = new MemoryStruct(new Int32(), stencil_1.size()); //initialised zero automatically
        MemoryStruct<Int32> stl_2_out = new MemoryStruct(new Int32(), stencil_2.size());
                
        int st1_total = exclusive_scan(stencil_1, n, stl_1_out) + stencil_1.get(n-1).value(); 
        int st2_total = exclusive_scan(stencil_2, n, stl_2_out) + stencil_2.get(n-1).value();
                
        LongStream.range(0, n)
                .parallel()
                .forEach(i->{
                    if(flags.get(i).value() != 0)                      
                        result.set(stl_1_out.get(i).value(), values.get(i));              
                });
        LongStream.range(0, n)
                .parallel()
                .forEach(i->{ 
                    if(flags.get(i).value() == 0)                    
                        result.set(stl_2_out.get(i).value() + st1_total, values.get(i));                    
                });
        if((st1_total + st2_total) > n)
            throw new IndexOutOfBoundsException("Issue with partition");  
        
        stencil_1.dispose();
        stencil_2.dispose();
        
        stl_1_out.dispose();
        stl_2_out.dispose();
        
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
    public void sort_pairs(MemoryStruct<Int32> keys, MemoryStruct<Int32> values, MemoryStruct<Int32> keysOut, MemoryStruct<Int32> valuesOut, long n) {
        RangeCheckArray.validateRangeSize(0, keys.size(),       n);
        RangeCheckArray.validateRangeSize(0, values.size(),     n);
        RangeCheckArray.validateRangeSize(0, keysOut.size(),    n);
        RangeCheckArray.validateRangeSize(0, valuesOut.size(),  n);
        
        //transfer copyStruct to memory
        checkSameByteCapacity(keys.getMemory(), keysOut.getMemory());
        keys.copyTo(keysOut);
        checkSameByteCapacity(values.getMemory(), valuesOut.getMemory());
        values.copyTo(valuesOut);
        
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
                        
                        if(!RangeCheckArray.isIndexInRange(PosStart, 0, toIndex)) 
                            return;
                        if(!RangeCheckArray.isIndexInRange(PosEnd, 0, toIndex)) 
                            return;
                        
                        if(keysOut.get(PosStart).value() > keysOut.get(PosEnd).value())
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
                            
                            if(!RangeCheckArray.isIndexInRange(PosStart, 0, toIndex)) 
                                return;
                            if(!RangeCheckArray.isIndexInRange(PosEnd, 0, toIndex)) 
                                return;

                            if(keysOut.get(PosStart).value() > keysOut.get(PosEnd).value())
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
