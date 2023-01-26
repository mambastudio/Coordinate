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
public abstract class PlaneShape<S extends SCoord, V extends VCoord, R extends AbstractRay<S, V>> {
    protected V normal;
    protected float distance;
    
    //triangle
    protected PlaneShape(S v0, V e1, V e2)
    {
        normal = (V) e1.cross(e2).normalize();
        distance = normal.dot(v0);
    }
}
