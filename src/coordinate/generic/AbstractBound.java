/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 * @param <R>
 * @param <B>
 */
public interface AbstractBound<S extends SCoord, V extends VCoord, R extends AbstractRay<S, V>, B extends AbstractBound> {
    public void include(S s);
    public S getCenter(); 
    public float getCenter(int dim);
    public S getMinimum();
    public S getMaximum();
    public B getInstance();
    
    default S get(int index)
    {
        switch (index) {
            case 0:
                return getMinimum();
            case 1:
                return getMaximum();
            default:
                return null;
        }
    }
    
    default boolean intersectP(R ray)
    {
        //Ray box intersection 
        //It's branchless
        //https://tavianator.com/fast-branchless-raybounding-box-intersections-part-2-nans/        
        float t1 = (getMinimum().get(0) - ray.getOrigin().get(0)) * ray.getInverseDirection().get(0);
        float t2 = (getMaximum().get(0) - ray.getOrigin().get(0)) * ray.getInverseDirection().get(0);
        
        float tmin = min(t1, t2);
        float tmax = max(t1, t2);
        
        for (int i = 1; i < 3; ++i) 
        {
            t1 = (getMinimum().get(i) - ray.getOrigin().get(i)) * ray.getInverseDirection().get(i);
            t2 = (getMaximum().get(i) - ray.getOrigin().get(i)) * ray.getInverseDirection().get(i);

            tmin = max(tmin, min(min(t1, t2), tmax));
            tmax = min(tmax, max(max(t1, t2), tmin));
        }
        
        return tmax > max(tmin, 0.0f);        
    }
    
    default void include(B b)
    {
        include((S) b.getMaximum());
        include((S) b.getMinimum());
    }
    
    default void include(B... bArray)
    {
        for(B b : bArray)
            include(b);
    }
    
    default int maximumExtentAxis() {
        V diag = (V) getMaximum().sub(getMinimum());
        
        if (diag.get('x') > diag.get('y') && diag.get('x') > diag.get('z')) {
            return 0;
        } else if (diag.get('y') > diag.get('z')) {
            return 1;
        } else {
            return 2;
        }
    }
    
    default V getExtents()
    {
        return (V) getMaximum().sub(getMinimum());
    }
    
    default float getMaximumExtent()
    {
        V v = getExtents();
        return v.get(maximumExtentAxis());
    }
    
    default B getCopy()
    {
        B bound = getInstance();        
        bound.include(this.getMinimum());
        bound.include(this.getMaximum());
        return bound;
    }
    
}
