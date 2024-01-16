/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;

import coordinate.memory.type.StructBase;

/**
 *
 * @author user
 * @param <V>
 */
public interface BiObjLongFunction<V extends StructBase> {
    public V apply(V v, long value);
}
