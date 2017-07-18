/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic.raytrace;

import coordinate.generic.AbstractBound;
import coordinate.generic.AbstractRay;
import coordinate.generic.SCoord;

/**
 *
 * @author user
 * @param <P>
 * @param <R>
 * @param <I>
 * @param <A>
 * @param <B>
 */
public interface AbstractPrimitive
        <P extends SCoord,         
         R extends AbstractRay, 
         I extends AbstractIntersection, 
         A extends AbstractAccelerator, 
         B extends AbstractBound>
{
    public int getCount();
    public B getBound(int primID);
    public P getCentroid(int primID);    
    public B getBound();
    public boolean intersect(R r, int primID, I isect);
    public boolean intersectP(R r, int primID);        
    public boolean intersect(R r, I isect);    
    public boolean intersectP(R r);
    public A getAccelerator();
    public void buildAccelerator();
    public float getArea(int primID);
    
    default float pdfA(int primID) 
    {
        return 1.f / getArea(primID);
    }
    
    default float inverseArea(int primID)
    {
        return pdfA(primID);
    }
}
