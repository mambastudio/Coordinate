/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

import coordinate.generic.AbstractCoordinate;
import coordinate.list.IntList;
import coordinate.struct.ByteStruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */

/*https://courses.cs.washington.edu/courses/cse351/17sp/lectures/CSE351-L14-structs_17sp-ink-day2.pdf
* provides naturally aligned variable offsets based on the paper
*
*/

public class StructInfo {
    
    Field structFields[] = null;
    
    public StructInfo(Class<? extends ByteStruct> c)
    {
        Field fields[] = c.getDeclaredFields();
        List<Field> structFieldList = new ArrayList<>();
        for (Field field : fields) {
            int modifier = field.getModifiers();
            if ( Modifier.isPublic(modifier) &&
                    !Modifier.isVolatile(modifier))
                structFieldList.add(field);            
        }
        
        structFields = structFieldList.toArray(new Field[structFieldList.size()]);
    }
    
    public int getDefaultFieldCount()
    {
        return structFields.length;
    }
    
    public void addByteValue(IntList byteValues, Class clazz)
    {
        if(clazz.isPrimitive())
            byteValues.add(getByteSizeofPrimitive((Class<? extends Number>) clazz));
        else
        {
            Object o = getObject(clazz);
            if(o instanceof AbstractCoordinate)
            {
                byteValues.add(this.getByteSizeofAbstractCoordinate(clazz));
            }
        }
    }
    
    public int getByteValue(Class clazz)
    {
        if(clazz.isPrimitive())
            return getByteSizeofPrimitive((Class<? extends Number>) clazz);
        else
        {
            Object o = getObject(clazz);
            if(o instanceof AbstractCoordinate)
            {
                return this.getByteSizeofAbstractCoordinate(clazz);
            }
        }
        return -1;
    }
    
    public int[] getByteValues()
    {
        IntList offsets = new IntList();
        
        for(Field field: structFields)
        {
            addByteValue(offsets, field.getType());
        }
        
        return offsets.trim();
    }
    
    public int getByteSizeofPrimitive(Class<? extends Number> number)
    {         
        if(number.getName().equals("int") || number.getName().equals("float") || number.getName().equals("boolean"))
            return 4;
        else
            return -1;
    }
    
    public int getByteSizeofAbstractCoordinate(Class<? extends AbstractCoordinate> clazz)
    {
        AbstractCoordinate coord = (AbstractCoordinate) getObject(clazz);
        
        if(this.isInstanceOf(coord.getClass(), AbstractCoordinate.class))
            return coord.getSize() * coord.getByteSize();
        return -1;
    }
    
    public int[] offsets()
    {
        IntList values = new IntList();
        IntList offsets = new IntList();
        
        for(Field field : structFields)
        {
            values.add(getByteValue(field.getType()));
        }
        int max = Arrays.stream(values.trim()).max().getAsInt(); 
        
        int offset = 0;
        for (Field field : structFields) {
             
            int byteValue = getByteValue(field.getType());           
            
            if(isInstanceOf(field.getType(), ByteStruct.class))
            {
                StructInfo info = new StructInfo((Class<? extends ByteStruct>) field.getType());
                int deepOffset[] = info.offsets();
                int deepValues[] = info.getByteValues();
                int deepMax      = Arrays.stream(deepValues).max().getAsInt();                 
                int deepOffsetStart = computeAlignmentOffset(offset, deepMax);  //get new deep offset
                
                //transform local offset to global offset
                for(int i = 0; i<deepOffset.length; i++)
                    deepOffset[i] += deepOffsetStart;
                
                //update global offset arrays with local offset arrays
                for(int i = 0; i<deepOffset.length-1; i++)
                    offsets.add(deepOffset[i]);
                
                //update offset to the size of deep struct size
                offset = deepOffset[deepOffset.length-1];
            }
            else
            {
                offsets.add(computeAlignmentOffset(offset, byteValue)); //where to put value into array to accomodate index 0          
                offset += byteValue;
            }
        }
        offsets.add(computeAlignmentOffset(offset, max)); 
        
        return offsets.trim();
    }
    
    public int computeAlignmentOffset(int offset, int alignment)
    {
        return (offset + alignment - 1) & ~(alignment - 1);
    }
    
    private Object getObject(Class clazz)
    {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(StructInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private boolean isInstanceOf(Class a, Class b)
    {
        return b.isAssignableFrom(a);
    }
  
    public static int[] offsets(Class<? extends ByteStruct> c)
    {
        StructInfo info = new StructInfo(c);
        return info.offsets();
    }
    
    public static int getByteArraySize(Class<? extends ByteStruct> c, int size)
    {
        StructInfo info = new StructInfo(c);
        int[] offsets = info.offsets();
        return offsets[offsets.length-1] * size;
    }
}
