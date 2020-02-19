/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.refl;

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

public class ByteStructInfo {
    
    Field structFields[] = null;
    
    public ByteStructInfo(Class<? extends ByteStruct> c)
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
    
    public int getByteSize(Class clazz)
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
        switch (number.getName()) {
            case "int":
            case "float":
            case "boolean":
                return 4;
            case "long":
                return 8;
            default:
                return -1;
        }
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
        IntList byteSizeValues = new IntList();     //byte size
        IntList offsets = new IntList();            //offset position of value
        
        //get byte size of field
        for(Field field : structFields)        
            byteSizeValues.add(getByteSize(field.getType()));
        
        //maximum byte size
        int maxByteSize = Arrays.stream(byteSizeValues.trim()).max().getAsInt(); 
        
        int currentOffset = 0; //lets start with offset of 0 
        
        //iterate the struct fields
        for (Field field : structFields) 
        {
            //is it a struct
            if(isInstanceOf(field.getType(), ByteStruct.class))
            {
                //all the section here is recursive if encounter inner struct
                ByteStructInfo info = new ByteStructInfo((Class<? extends ByteStruct>) field.getType());
                int deepOffset[] = info.offsets();
                int deepValues[] = info.getByteValues();
                int deepMax      = Arrays.stream(deepValues).max().getAsInt();                 
                int deepOffsetStart = computeAlignmentOffset(currentOffset, deepMax);  //get new deep offset based on max size of field
                
                //transform local offset to global offset
                for(int i = 0; i<deepOffset.length; i++)
                    deepOffset[i] += deepOffsetStart;
                
                //update global offset arrays with local offset arrays
                for(int i = 0; i<deepOffset.length-1; i++)
                    offsets.add(deepOffset[i]);
                
                //update offset to the size of deep struct size
                currentOffset = deepOffset[deepOffset.length-1];               
            }
            //deal with normal primitives
            else
            {
                int byteSize = getByteSize(field.getType());
                int newOffset = computeAlignmentOffset(currentOffset, byteSize);               
                offsets.add(newOffset); //where to put value into array                 
                currentOffset = newOffset + byteSize;
            }
        }
        offsets.add(computeAlignmentOffset(currentOffset, maxByteSize)); //last position of offset = size of struct       
        return offsets.trim();
    }
    
    //new offset based on previous offset
    public int computeAlignmentOffset(int offset, int byteSize)
    {
        return (offset + byteSize - 1) & ~(byteSize - 1);
    }
    
    private Object getObject(Class clazz)
    {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private boolean isInstanceOf(Class a, Class b)
    {
        return b.isAssignableFrom(a);
    }
  
    public static int[] offsets(Class<? extends ByteStruct> c)
    {
        ByteStructInfo info = new ByteStructInfo(c);
        return info.offsets();
    }
    
    public static int getByteArraySize(Class<? extends ByteStruct> c, int size)
    {
        ByteStructInfo info = new ByteStructInfo(c);
        int[] offsets = info.offsets();
        return offsets[offsets.length-1] * size;
    }
}
