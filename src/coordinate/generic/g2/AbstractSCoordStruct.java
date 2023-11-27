/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic.g2;

import coordinate.generic.SCoord;
import coordinate.generic.VCoord;
import coordinate.memory.type.StructBase;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 * @param <SCoordStruct>
 */
public abstract class AbstractSCoordStruct<
        S extends SCoord, 
        V extends VCoord,
        SCoordStruct extends AbstractSCoordStruct<S, V, SCoordStruct>>
        
        implements StructBase<SCoordStruct>, SCoord<S, V> {
    
}
