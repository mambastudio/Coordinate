/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.type.algorithm;

import coordinate.memory.type.MemoryStruct;
import coordinate.memory.type.StructBase;
import coordinate.utility.RangeCheckArray;
import java.util.function.BinaryOperator;

/**
 *
 * @author user
 * @param <S>
 */
public class SerialAlgorithmStruct <S extends StructBase>{
    public S reduce(MemoryStruct<S> values, long n, MemoryStruct<S> result, BinaryOperator<S> f) {
        RangeCheckArray.validateIndexSize(n, Long.MAX_VALUE);
        RangeCheckArray.validateRangeSize(0, n, values.size());
        S res = (S) values.get(0).newStruct();
        for(long i = 0; i<n; i++)
            res = f.apply(res, values.get(i));
        result.set(0, res); //result is one value
        return res;
    }
}
