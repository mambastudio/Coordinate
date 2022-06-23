/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct.newstruct.base;

import coordinate.utility.Value1Di;
import java.util.Arrays;

/**
 *
 * @author user
 * 
 * base struct interface
 * 
 */
public interface StructInterface {
    
    //fields
    public void generateStructFields(); //call in constructor first - 1
    public StructField[] getStructFields();
    
    //byte size -> size of struct in memory
    public void calculateByteSize(Value1Di offset); //call in constructor first - 3
    public int getByteSize();
    default int sizeof()
    {
        return getByteSize();
    }
    
    //align values and offsets
    public int calculateAlignValues(); //call in constructor first - 2
    public int getAlignValue();
    public void calculateOffsetValues(Value1Di offset); //call in constructor first - 4
    public int[] getOffsetValues(); //get offset of each field/member (stored in structfields)
         
    /**
     * Align is the size of data (e.g. 4-byte data -> int, float), and offset is starting point 
     * or reference starting point.
     * 
     * The returned value is the new starting point or offset of the align data.
     * 
     * https://en.wikipedia.org/wiki/Data_structure_alignment
     * 
     * @param offset
     * @param align
     * @return 
     */
    default int computeAlignmentOffset(int offset, int align)
    {
        return (offset + align - 1) & -align;
    }  
    
    default String getLayout()
    {
        calculateOffsetValues(new Value1Di());
        StringBuilder builder = new StringBuilder();
        for(StructField field : getStructFields())
        {
            builder.append("alignment ").append(field.getAlign()).append(" ");
            builder.append("byte size ").append(field.getByteSize()).append("\n");            
        }
        builder.append("field offsets: ").append(Arrays.toString(getOffsetValues())).append("\n");
        builder.append("byte size of struct: ").append(getByteSize());
        
        return builder.toString();
    }
}
