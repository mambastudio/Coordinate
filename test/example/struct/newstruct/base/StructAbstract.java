/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct.newstruct.base;

import coordinate.struct.refl.StructureField;
import coordinate.utility.Value1Di;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author user
 * @param <GlobalBuffer>
 * 
 * https://en.wikipedia.org/wiki/Data_structure_alignment
 * 
 */
public abstract class StructAbstract<GlobalBuffer> implements StructInterface {
    private StructureField[] fields;    
    private int alignment; //A power of 2, alignment of offset
    private int byteSize;
    
    public StructAbstract()
    {
        init();
    }
    
    //this should be called in constructor
    public final void init()
    {
        //generate fields first
        generateStructureFields();
        
        //calculate alignment of members, their offsets and overall bytesize of struct
        //all information is stored in this struct
        calculateAlignValues();
        calculateByteSize(new Value1Di());
        calculateOffsetValues(new Value1Di());
    }
    
    //construtor call
    @Override
    public final void calculateAlignValues()
    {
        if(fields.length == 1)
        {
            alignment = fields[0].setupAlignment();
        }
        else
        {
            StructureField maxByteField = Collections.max(Arrays.asList(fields), 
                   (a, b) -> Integer.compare(a.setupAlignment(), b.setupAlignment()));   

            alignment = maxByteField.getAlignment();
        }
    }
}
