/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic.raytrace;

import coordinate.generic.AbstractBound;
import coordinate.generic.AbstractRay;

/**
 *
 * @author user
 * @param <R>
 * @param <I>
 * @param <P>
 * @param <B>
 */
public interface AbstractAccelerator
        <R extends AbstractRay, 
         I extends AbstractIntersection, 
         P extends AbstractPrimitive, 
         B extends AbstractBound> 
{
    public void build(P primitives);
    public boolean intersect(R ray, I isect);
    public boolean intersectP(R ray);    
    public void intersect(R[] rays, I[] isects);   
    public B getBound();
    
    default int partition(P primitives, int[] objects, int start, int end, int splitDimension, float splitCoord)
    {
        int mid = start;
        for(int i = start; i < end; i++)
        {
            if(primitives.getBound(objects[i]).getCenter(splitDimension) < splitCoord)
            {
                swap(i, mid, objects);                
                mid++;
            }
        }
        return mid;
    }
    
    default void swap(int i, int j, int... arr)
    {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
