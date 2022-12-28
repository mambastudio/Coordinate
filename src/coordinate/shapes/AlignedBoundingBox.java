/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.shapes;

import coordinate.generic.SCoord;
import coordinate.generic.VCoord;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 */
public abstract class AlignedBoundingBox<S extends SCoord, V extends VCoord> {
    protected final S min;
    protected final S max;
    
    protected AlignedBoundingBox(S min, S max)
    {
        this.min = min;
        this.max = max;
    }
}
