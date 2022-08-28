/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.surface;

import coordinate.generic.VCoord;

/**
 *
 * @author user
 * @param <V>
 */
public interface AbstractFrame<V extends VCoord> {
    public void setFromZ(V v);
    public V toWorld(V a);
    public V toLocal(V a);
    
    public V binormal();
    public V tangent ();
    public V normal  ();
}
