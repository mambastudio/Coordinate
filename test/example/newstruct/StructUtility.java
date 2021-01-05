/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.newstruct;

import coordinate.generic.AbstractCoordinate;
import coordinate.list.IntList;
import coordinate.struct.ByteStruct;
import coordinate.struct.refl.ByteStructInfo;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class StructUtility {
   
    public static Alignment getAlignment(Class<? extends ByteStruct> c)
    {
        //get all fields in bytestruct
        Field[] structFields = getAllFields(c);        
        Alignment alignment = new Alignment();
        
        int currentOffset = 0; //lets start with offset of 0 
        
        for (Field field : structFields) 
        {
            if(isInstanceOf(field.getType(), ByteStruct.class))
            {
                Alignment deepAlignment = getAlignment((Class<? extends ByteStruct>) field.getType());                
                int deepOffsetStart = computeAlignmentOffset(currentOffset, deepAlignment.getMaxBytes());  //get new deep offset based on max size of field
                alignment.addOffsets(deepOffsetStart, deepAlignment.getOffsets());
                alignment.addByteSizes(deepAlignment.getByteSizes());
                alignment.addFieldSizes(deepAlignment.getSize()); //size of inner struct
                currentOffset += deepAlignment.getSize(); //update current offset based on inner struct size
            }
            else
            {
                int byteSize = getByteSizeOf(field.getType());
                int newOffset = computeAlignmentOffset(currentOffset, byteSize);               
                alignment.addOffsets(newOffset); 
                alignment.addByteSizes(byteSize);                 
                alignment.addFieldSizes(byteSize);
                currentOffset = alignment.getLastIndex(); //update current offset
            }
        }        
        
        return alignment;
    }
    
    //new offset based on previous offset
    public static int computeAlignmentOffset(int offset, int byteSize)
    {
        return (offset + byteSize - 1) & ~(byteSize - 1);
    }
    
    //is class A instance of class B
    public static boolean isInstanceOf(Class a, Class b)
    {
        return b.isAssignableFrom(a);
    }
    
    //instance of class T
    public static<T> T getInstance(Class<T> clazz)
    {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    //byte size of primitive or abstract coordinate
    public static int getByteSizeOf(Class clazz)
    {    
        //is primitive
        if(clazz.isPrimitive())
        {
            switch (clazz.getName()) {
            case "int":
            case "float":
            case "boolean":
                return 4;
            case "long":
                return 8;
            default:
                throw new UnsupportedOperationException("Not supported yet."); 
            }
        }        
        //is abstract coordinate
        else if(isInstanceOf(clazz, AbstractCoordinate.class))
        {
            AbstractCoordinate coord = (AbstractCoordinate) getInstance(clazz);
            return coord.getSize() * coord.getByteSize();  
        }  
        
        throw new UnsupportedOperationException("Not supported yet."); 
    }    
    
    //get all fields from bytestruct
    public static Field[] getAllFields(Class<? extends ByteStruct> c)
    {
        Field fields[] = c.getDeclaredFields();
        List<Field> structFieldList = new ArrayList<>();
        for (Field field : fields) {
            int modifier = field.getModifiers();
            if ( Modifier.isPublic(modifier) &&
                    !Modifier.isVolatile(modifier))
                structFieldList.add(field);            
        }
        
       return structFieldList.toArray(new Field[structFieldList.size()]);
    }
        
    //get all sizes of fields byte 
    public static int[] getByteSizes(Class<? extends ByteStruct> c)
    {
        Field[] structFields = getAllFields(c);
        //FIXME, what if I encounter unions
        IntList fieldByteSizes = new IntList();        
        for(Field field: structFields)
        {
            //deal with structs
            if(isInstanceOf(field.getType(), ByteStruct.class))
            {
                int[] innerSizes = getByteSizes((Class<? extends ByteStruct>) field.getType());
                fieldByteSizes.add(innerSizes);
            }
            else
                fieldByteSizes.add(getByteSizeOf(field.getType()));
        }
                
        return fieldByteSizes.trim();
    }
}
