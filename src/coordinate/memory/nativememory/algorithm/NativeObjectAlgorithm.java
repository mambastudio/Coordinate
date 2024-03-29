/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.nativememory.algorithm;

import coordinate.generic.TemplateAlgorithms;
import coordinate.memory.nativememory.NativeInteger;
import coordinate.memory.nativememory.NativeObject;
import coordinate.memory.nativememory.NativeObject.Element;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 *
 * @author user
 * @param <E>
 * @param <DataTransform>
 */
public interface NativeObjectAlgorithm<E extends Element<E>, DataTransform>
        extends TemplateAlgorithms<
                        E, 
                        NativeObject<E>,  
                        NativeObject<E>,  
                        DataTransform,
                        NativeInteger, 
                        UnaryOperator<E>, 
                        BinaryOperator<E>>{    
}
