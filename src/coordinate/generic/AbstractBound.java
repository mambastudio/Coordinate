/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;

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
    
    default boolean intersectP(R ray, float[] hitt)
    {
        float t0 = ray.getMin(), t1 = ray.getMax();
        for (int i = 0; i < 3; ++i) 
        {
            // Update interval for _i_th bounding box slab, page 180           
            float tNear = (getMinimum().get(i) - ray.getOrigin().get(i)) * ray.getInverseDirection().get(i);
            float tFar = (getMaximum().get(i) - ray.getOrigin().get(i)) * ray.getInverseDirection().get(i);

            // Update parametric interval from slab intersection $t$s
            if (tNear > tFar) 
            {
                float swap = tNear;
                tNear = tFar;
                tFar = swap;
            }
            if (tNear > t0) t0=tNear;
            if (tFar < t1) t1=tFar;
            if (t0 > t1) 
            {
                return false;
            }
        }
        if (hitt != null) 
        {
            hitt[0] = t0;
            hitt[1] = t1;
        }
        return true;
    }
    
    default void include(B b)
    {
        include((S) b.getMaximum());
        include((S) b.getMinimum());
    }
    
    default int maximumExtent() {
        V diag = (V) getMaximum().sub(getMinimum());
        
        if (diag.get('x') > diag.get('y') && diag.get('x') > diag.get('z')) {
            return 0;
        } else if (diag.get('y') > diag.get('z')) {
            return 1;
        } else {
            return 2;
        }
    }
    
}
