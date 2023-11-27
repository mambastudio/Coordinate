/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.shapes;

import coordinate.generic.AbstractBound;
import coordinate.generic.AbstractRay;
import coordinate.generic.SCoord;
import coordinate.generic.VCoord;

/**
 * 
 * https://jcgt.org/published/0007/03/04/paper-lowres.pdf
 *
 * @author user
 * @param <S>
 * @param <V>
 * @param <R> 
 * @param <B>
 */
public interface AlignedBBoxShape<
        S extends SCoord, 
        V extends VCoord,          
        R extends AbstractRay<S, V>,        
        B extends AlignedBBoxShape<S, V, R, B>> extends AbstractBound<S, V, R, B>{
        
    default boolean rayIntersectionP(R ray)
    {
        V t0 = (V) getMinimum().sub(ray.getOrigin()).div(ray.getInverseDirection());
        V t1 = (V) getMaximum().sub(ray.getOrigin()).div(ray.getInverseDirection());
        
        V tmin = (V) V.min3(t0, t1), tmax = (V) V.max3(t0, t1);
        
        return tmin.maxComponent() <= tmax.minComponent();
    }    
}
