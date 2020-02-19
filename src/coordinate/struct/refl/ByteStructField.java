/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.refl;

import coordinate.generic.AbstractCoordinate;
import coordinate.generic.AbstractCoordinateFloat;
import coordinate.generic.AbstractCoordinateInteger;
import coordinate.struct.ByteStruct;
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
    
    private ByteStructField(ByteStruct runtimeOwner, Field field)
    {
        if(!(field.getType().isPrimitive() 
                || (runtimeOwner instanceof AbstractCoordinate)
                || (runtimeOwner instanceof ByteStruct)))
            throw new UnsupportedOperationException("field not supported yet");
        this.field = field;
        this.runtimeParent = runtimeOwner;
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
    
    private boolean isPrimitive()
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
    
    public static List<ByteStructField> getAllStructFields(ByteStruct struct)
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
