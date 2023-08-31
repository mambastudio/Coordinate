/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.nativememory.algorithm;

import coordinate.generic.TemplateAlgorithms;
import coordinate.memory.nativememory.NativeInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

/**
 *
 * @author jmburu
 */
public interface NativeIntegerAlgorithm extends TemplateAlgorithms<Integer, NativeInteger, NativeInteger, NativeInteger, NativeInteger, IntUnaryOperator, IntBinaryOperator>{
    @Override
    default Integer exclusive_scan(NativeInteger values, long n, NativeInteger results){
        return this.exclusive_scan(values, n, results, (a, b)-> a + b);
    } 
}
