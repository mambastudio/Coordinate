/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import coordinate.memory.type.LayoutMemory.PathElement;
import coordinate.memory.type.ValueState;
import coordinate.struct.refl.StructField;
import coordinate.utility.Value1Di;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author user 
 * 
 * https://en.wikipedia.org/wiki/Data_structure_alignment
 * 
 */
public abstract class StructAbstract implements StructInterface {
    private StructField[] fields;    
    private int maxAlign; //maximum byte of field
    private int byteSize; //size of this struct
    
    public StructAbstract()
    {
        init();
    }
    
    //this should be called in constructor
    private void init()
    {
        //generate fields first
        generateStructFields();
        
        //calculate alignment of members, their offsets and overall bytesize of struct
        //all information is stored in this struct
        calculateAlignValues();
        calculateByteSize(new Value1Di());
        calculateOffsetValues(new Value1Di());
    }
    
    //constructor call
    @Override
    public void generateStructFields() 
    {
        fields = StructField.getAllStructFieldsAsArray(this);        
    }
        
    //construtor call
    @Override
    public final int calculateAlignValues()
    {
        if(fields.length == 1)
        {
            maxAlign = fields[0].calculateAlignValues();
        }
        else
        {
            StructField maxByteField = Collections.max(Arrays.asList(fields), 
                   (a, b) -> Integer.compare(a.calculateAlignValues(), b.calculateAlignValues()));   
            
            //largest align value of field
            maxAlign = maxByteField.getAlign();
        }
        return maxAlign;
    }
    
    //construtor call
    @Override
    public void calculateByteSize(Value1Di offset)
    {
        for (StructField field : fields) {
            field.calculateByteSize(offset);                
        }       
        //offsite is the address of the last byte of the field 
        byteSize = computeAlignmentOffset(offset.x, maxAlign);        
    }
    
    @Override
    public void calculateOffsetValues(Value1Di offset)
    {
        for (StructField field : fields) {
            field.calculateOffset(offset);
        }
    }
    
    @Override
    public int getByteSize()
    {
        return byteSize;
    }
    
    @Override
    public StructField[] getStructFields() {
        return fields;
    }
    
    @Override
    public int[] getOffsetValues()
    {
        int[] offsets = new int[fields.length];
        for(int i = 0; i<offsets.length; i++)
            offsets[i] = fields[i].getOffset();
        return offsets;
    }
        
    @Override
    public int getAlignValue() 
    {
        return maxAlign;
    }
    
}
