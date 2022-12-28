/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.shapes;

import coordinate.generic.SCoord;
import coordinate.generic.VCoord;
import coordinate.model.Transform;

/**
 *
 * @author jmburu
 * @param <S>
 * @param <V>
 */
public abstract class OrientedBoundingBox<S extends SCoord, V extends VCoord> {
    public S center;
    public V radius;
    public V invRadius;
    public Transform<S, V> rotation;

    
}
