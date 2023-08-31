/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.nativememory.algorithm;

import coordinate.memory.nativememory.NativeInteger;
import coordinate.memory.nativememory.NativeObject;
import coordinate.memory.nativememory.NativeObject.Element;
import coordinate.utility.RangeCheck;
import java.util.function.BinaryOperator;

/**
 *
 * @author user
 * @param <E>
 */
public class SerialNativeObjectAlgorithm<E extends Element<E>> implements NativeObjectAlgorithm<E, NativeInteger>{

    @Override
    public E reduce(NativeObject<E> values, long n, NativeObject<E> result, BinaryOperator<E> f) {
        RangeCheck.rangeAboveZero(n);
        RangeCheck.rangeCheckBound(0, n, values.capacity());
        E res = values.get(0).newInstance();
        for(long i = 0; i<n; i++)
            res = f.apply(res, values.get(i));
        result.set(0, res);
        return res;
    }
    
}
