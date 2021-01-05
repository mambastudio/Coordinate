/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.field;

import coordinate.generic.AbstractCoordinate;
import coordinate.generic.AbstractCoordinateFloat;
import coordinate.generic.AbstractCoordinateInteger;
import coordinate.struct.ByteStruct;
import coordinate.struct.annotation.ByteStructAnnotation;
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
public class ByteStructField {
    private Field field;
    private ByteStruct runtimeParent;
    private int byteSize;
    
    private ByteStructField(ByteStruct runtimeOwner, Field field)
    {
        if(!(field.getType().isPrimitive() 
                || (runtimeOwner instanceof AbstractCoordinate)
                || (runtimeOwner instanceof ByteStruct)))
            
            throw new UnsupportedOperationException("field not supported yet");
        this.field = field;
        this.runtimeParent = runtimeOwner;
        this.setupByteSize();
    }
    
    public int getByteSize()
    {
        return byteSize;
    }
    
    private void setupByteSize()
    {
        if(this.isPrimitive())        
            byteSize = getByteSizeofPrimitive((Class<? extends Number>) field.getType());        
        else if(this.isAbstractCoordinate())
            byteSize = getByteSizeofAbstractCoordinate((Class<? extends AbstractCoordinate>) field.getType());
        else if(this.isArray())
        {            
            byteSize = getArrayClassByteSize();            
        }
    }
    
    
    
    
    
    private int getByteSizeofPrimitive(Class<? extends Number> number)
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
    
    private int getByteSizeofAbstractCoordinate(Class<? extends AbstractCoordinate> clazz)
    {
        if(!isAbstractCoordinate())
            throw new IllegalArgumentException("field is not abstract coordinate");
        AbstractCoordinate coord = (AbstractCoordinate) getObject(clazz);
        
        return coord.getSize() * coord.getByteSize();
        
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
    
    public boolean isPublic()
    {
        int modifier = field.getModifiers();
        return Modifier.isPublic(modifier);
    }
    
    public boolean isVolatile()
    {
        int modifier = field.getModifiers();
        return Modifier.isVolatile(modifier);
    }
    
    public boolean isArray()
    {
        return field.getType().isArray();
    }
    
    public int getArraySize()
    {
        if(!isArray())
            throw new IllegalArgumentException("field is not an array.");
        else if(field.isAnnotationPresent(ByteStructAnnotation.class))
        {
            ByteStructAnnotation annotation = field.getAnnotation(ByteStructAnnotation.class);
            return annotation.arraysize();                
        }
        else
            throw new IllegalArgumentException("no annotation");
    }
    
    public Class getArrayClass()
    {
        if(isArray())
            return field.getType().getComponentType();
        else
            throw new IllegalArgumentException("this is not an array class");
    }
    
    public int getArrayClassByteSize()
    {
        if(isArray())
        {
            Class clazz = getArrayClass();
            if(this.isArrayClassPrimitive())
                return getByteSizeofPrimitive((Class<? extends Number>) clazz); 
            else if(this.isArrayClassPrimitive())
                return getByteSizeofAbstractCoordinate((Class<? extends AbstractCoordinate>) clazz);
            else
                throw new IllegalArgumentException("this is not a supported array class for byte size calculation");
            //TODO:
            //ADD ByteStruct (easy, just add ByteStructInfo.offset(clazz))
        }
        throw new IllegalArgumentException("this is not an array class");
    }
    
    public boolean isArrayClassPrimitive()
    {
        if(isArray())
        {
            Class clazz = getArrayClass();
            return clazz.isPrimitive();
        }
        return false;
    }
    
    public boolean isArrayClassAbstractCoordinate()
    {
        if(isArray())
        {
            Class clazz = getArrayClass();
            return this.isInstanceOf(clazz, AbstractCoordinate.class);
        }
        return false;
    }
    
    public boolean isArrayClassByteStruct()
    {
        if(isArray())
        {
            Class clazz = getArrayClass();
            return this.isInstanceOf(clazz, ByteStruct.class);
        }
        return false;
    }
    
    public boolean isPrimitive()
    {
        return field.getType().isPrimitive();
    }
    
    public boolean isInt()
    {
        return isPrimitive()? field.getType().getName().equals("int") : false;
    }
    
    public int getInt()
    {
        try {
            return field.getInt(runtimeParent);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Error in casting and processing"); 
    }
    
    public void setInt(int value)
    {
        try {       
            field.setInt(runtimeParent, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public boolean isLong()
    {
        return isPrimitive()? field.getType().getName().equals("long") : false;
    }
    
    public long getLong()
    {
        try {
            return field.getLong(runtimeParent);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Error in casting and processing"); 
    }
    
    public void setLong(long value)
    {
        try {       
            field.setLong(runtimeParent, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isBoolean()
    {
        return isPrimitive()? field.getType().getName().equals("boolean") : false;
    }
    
    public boolean getBoolean()
    {
        try {
            return field.getBoolean(runtimeParent);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Error in casting and processing"); 
    }
    
    public void setBoolean(boolean value)
    {
        try {       
            field.setBoolean(runtimeParent, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isFloat()
    {
        return isPrimitive()? field.getType().getName().equals("float") : false;
    }
    
    public float getFloat()
    {
        try {
            return field.getFloat(runtimeParent);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Error in casting and processing"); 
    }
    
    public void setFloat(float value)
    {
        try {       
            field.setFloat(runtimeParent, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isByteStruct()
    {
        return isInstanceOf(ByteStruct.class);
    }
    
    public boolean isAbstractCoordinate()
    {
        return isInstanceOf(AbstractCoordinate.class);
    }
    
    public boolean isAbstractCoordinateFloat()
    {
        return isInstanceOf(AbstractCoordinateFloat.class);
    }
    
    public boolean isAbstractCoordinateInt()
    {
        return isInstanceOf(AbstractCoordinateInteger.class);
    }
        
    private boolean isInstanceOf(Class b)
    { 
        //isInstanceOf(a, b){ return b.isAssignableFrom(a);}
        return b.isAssignableFrom(field.getType());
    }
    
    private boolean isInstanceOf(Class a, Class b)
    { 
        //isInstanceOf(a, b){ return b.isAssignableFrom(a);}
        return a.isAssignableFrom(b);
    }
    
    public boolean isParentSameClass()
    {
        return isInstanceOf(getDeclaringClass());
    }
    
    public String getName()
    {
        return preprocessString(field.getName());
    }
    
    public String getClassName()
    {
        if(isArray())
            return preprocessString(field.getType().getComponentType().getName())+"[]";
        return preprocessString(field.getType().getName());
    }
    
    public Class getActualClass()
    {
        return field.getType();
    }
    
    public String getDeclaringClassName()
    {
        return preprocessString(field.getDeclaringClass().getName());
    }
    
    public Class getDeclaringClass()
    {
        return field.getDeclaringClass();
    }
    
    private String preprocessString(String string)
    {
        if(string.contains("$"))
            return string.split("\\$")[1];
        return string;
    }
    
    public ByteStruct getChildByteStructOf(ByteStruct parentStruct)
    {
        ByteStruct childStruct = null;
        try {
            childStruct = (ByteStruct)field.get(parentStruct);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return childStruct;
    }
    
    
    public Object getFieldRuntimeObject()
    {
        try {
            return field.get(runtimeParent);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public void setCoordinateFloat(float... array)
    {        
        AbstractCoordinateFloat floatBuffer = (AbstractCoordinateFloat) this.getFieldRuntimeObject();
        floatBuffer.set(array);        
    }
    
    public float[] getCoordinateFloat()
    {
        AbstractCoordinateFloat floatBuffer = (AbstractCoordinateFloat) this.getFieldRuntimeObject();
        return floatBuffer.getArray();
    }
    
    public int getCoordinateFloatSize()
    {
        AbstractCoordinateFloat floatBuffer = (AbstractCoordinateFloat) this.getFieldRuntimeObject();
        return floatBuffer.getSize();
    }
    
    public void setCoordinateInt(int... array)
    {        
        AbstractCoordinateInteger intBuffer = (AbstractCoordinateInteger) this.getFieldRuntimeObject();
        intBuffer.set(array);        
    }
    
    public int[] getCoordinateInt()
    {
        AbstractCoordinateInteger intBuffer = (AbstractCoordinateInteger) this.getFieldRuntimeObject();
        return intBuffer.getArray();
    }
    
    public int getCoordinateIntSize()
    {
        AbstractCoordinateInteger intBuffer = (AbstractCoordinateInteger) this.getFieldRuntimeObject();
        return intBuffer.getSize();
    }
    
    private static ByteStruct getInstance(Class clazz)
    {
        try {
            return (ByteStruct) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ByteStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static<B extends ByteStruct> List<ByteStructField> getAllStructFields(Class<? extends ByteStruct> clazz)
    {
        Field fields[] = clazz.getDeclaredFields();
        ByteStruct struct = getInstance(clazz);
        List<ByteStructField> structFieldList = new ArrayList<>();
        for (Field field : fields) {
            ByteStructField structField = new ByteStructField(struct, field);           
            if ( structField.isPublic() &&
                    !structField.isVolatile())                           
                if(structField.isByteStruct())                 
                    if(structField.isParentSameClass())
                        throw new UnsupportedOperationException("detected recursion in class");
                    else
                    {                 
                        structFieldList.add(structField);
                    } 
                else
                    structFieldList.add(structField);
        }
        return structFieldList;
    }    
    
    public static<B extends ByteStruct> List<ByteStructField> getAllStructFields(B struct)
    {
        Field fields[] = struct.getClass().getDeclaredFields();
        List<ByteStructField> structFieldList = new ArrayList<>();
        for (Field field : fields) {
            ByteStructField structField = new ByteStructField(struct, field);           
            if ( structField.isPublic() &&
                    !structField.isVolatile())                           
                if(structField.isByteStruct())                 
                    if(structField.isParentSameClass())
                        throw new UnsupportedOperationException("detected recursion in class");
                    else
                    {                             
                        List<ByteStructField> listInner = getAllStructFields(structField.getChildByteStructOf(struct));
                        structFieldList.addAll(listInner);
                    } 
                else
                    structFieldList.add(structField);
        }
        return structFieldList;
    }    
}
