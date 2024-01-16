/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.shapes;

import coordinate.generic.AbstractRay;
import coordinate.generic.SCoord;
import coordinate.generic.VCoord;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 * @param <R>
 */
public interface GenericShape<S extends SCoord, V extends VCoord, R extends AbstractRay<S, V, R>> {
    default boolean intersect(R r)
    {
        return false;
    }
    
    default boolean intersect(R r, float[] tuv)
    {
        return false;
    }
}
