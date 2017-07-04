/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;

import coordinate.generic.VCoord;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 */
public interface AbstractRay<S extends SCoord, V extends VCoord> 
{
    public void set(float ox, float oy, float oz, float dx, float dy, float dz);
    public void set(S o, V d);
    public S getPoint();
    public S getPoint(float t);
    public V getDirection();
}
