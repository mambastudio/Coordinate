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
public abstract class PlaneShape<S extends SCoord, V extends VCoord, R extends AbstractRay<S, V>> implements GenericShape<S, V, R>{
    protected V normal;
    protected S center;
    
    //triangle
    protected PlaneShape(S v0, V e1, V e2)
    {
        normal = (V) e1.cross(e2).normalize();
        center = (S) v0.copy();
    }
    
    @Override
    public boolean intersect(R r)
    {
        float denominator = normal.dot(r.getDirection());
        
        // 0.0001 is an arbitrary epsilon value. We just want
        // to avoid working with intersections that are almost
        // orthogonal.
        
        if(Math.abs(denominator) > 0.0001f) {
            V difference = (V) center.sub(r.getOrigin());
            float t = difference.dot(normal)/denominator;
            
            if(t > 0.0001f)
                return true;
        }
        return false;
    }
    
    /**     
     * @param localV0
     * @param halfExtents
     * @return 
     * 
     * - localV0 is the triangle coordinate transformed/translated relative to the
     * centre of the box (aligned) 
     * - halfExtents is the half extents of a box (aligned)
     * 
     * To use it, do the following:
     *   V e0            = P2 - P1;  //p1, p2, p3 are triangle coordinates
     *   V e1            = P3 - P2;
     *   V normal        = e0 cross e1;
     *   S v0            = (S)(P1 - box.getCenter());
     *   V halfExtents   = box.getHalfExtents();
     *   
     *   return planeBoxIntersection(v0, halfExtents);
     * 
     */
    public boolean planeBoxIntersection(S localV0, V halfExtents){        
        int q;        
        
        S vmin = (S) localV0.newS(0, 0, 0), vmax = (S) localV0.newS(0, 0, 0);
        float v;
        
        for(q = 0; q <= 2; q++)
        {
            v = localV0.get(q);					

            if(normal.get(q)>0.0f)
            {
                vmin.setIndex(q, - halfExtents.get(q) - v);	
                vmax.setIndex(q,   halfExtents.get(q) - v);                 
            }
            else
            {
                vmin.setIndex(q,   halfExtents.get(q) - v);                 
                vmax.setIndex(q, - halfExtents.get(q) - v);   
            }
        }
        
        if(normal.dot(vmin) >  0.0f) return false;        
        return normal.dot(vmax) >= 0.0f;
    }        
}
