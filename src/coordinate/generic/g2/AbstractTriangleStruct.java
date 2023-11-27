/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic.g2;

import coordinate.generic.AbstractRay;
import coordinate.generic.SCoord;
import coordinate.generic.VCoord;
import coordinate.memory.type.StructBase;
import coordinate.shapes.AlignedBBoxShape;
import coordinate.shapes.TriangleShape;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 * @param <R>
 * @param <B>
 * @param <TriShape>
 */
public abstract class AbstractTriangleStruct<
        S extends SCoord, 
        V extends VCoord, 
        R extends AbstractRay<S, V, R>,
        B extends AlignedBBoxShape<S, V, R, B>,
        TriShape extends AbstractTriangleStruct<S, V, R, B, TriShape>> 
        
        implements StructBase<TriShape>, TriangleShape<S, V, R, B>{
        
}
