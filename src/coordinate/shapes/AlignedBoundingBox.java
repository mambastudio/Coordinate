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
 * @param <T>
 * @param <B>
 */
public abstract class AlignedBoundingBox<
        S extends SCoord, 
        V extends VCoord,          
        R extends AbstractRay<S, V>,
        T extends TriangleShape<S, V, R>,
        B extends AlignedBoundingBox<S, V, R, T, B>> implements AbstractBound<S, V, R, B>{
    protected final S minimum;
    protected final S maximum;
    
    protected AlignedBoundingBox(S min, S max)
    {
        this.minimum = min;
        this.maximum = max;
    }
    
    public final boolean rayIntersectionP(R ray)
    {
        V t0 = (V) minimum.sub(ray.getOrigin()).div(ray.getInverseDirection());
        V t1 = (V) maximum.sub(ray.getOrigin()).div(ray.getInverseDirection());
        
        V tmin = (V) V.min3(t0, t1), tmax = (V) V.max3(t0, t1);
        
        return tmin.maxComponent() <= tmax.minComponent();
    }
    
    public boolean triBoxOverlap(T triangle)
    {
        return ShapeUtility.triBoxOverlap((B)this, triangle);
    }
    
    public boolean planeBoxOverlap(T triangle)
    {
        return ShapeUtility.planeBoxIntersection(triangle, (B)this);
    }
}
