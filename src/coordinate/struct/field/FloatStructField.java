/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.field;

import coordinate.generic.AbstractCoordinateFloat;
import coordinate.struct.FloatStruct;
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
public class FloatStructField {
    private Field field;
    private FloatStruct runtimeParent;
    
    private FloatStructField(FloatStruct runtimeOwner, Field field)
    {
        if(!(isFloat(field)
                || (runtimeOwner instanceof FloatStruct)
                || (runtimeOwner instanceof AbstractCoordinateFloat)))
            throw new UnsupportedOperationException("field should be float");
        this.field = field;
        this.runtimeParent = runtimeOwner;
    }
    
    public int getNumberOfFloats()
    {
        if(isFloat())
            return 1;
        if(isAbstractCoordinateFloat())
            return getCoordinateFloat().length;
        else
            throw new UnsupportedOperationException("field should be float");
    }
   
    private boolean isFloat(Field field)
    {
        return field.getType().isPrimitive()? field.getType().getName().equals("float") : false;
    }
    
    public boolean isFloat()
    {
        return isFloat(field);
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
    
    public boolean isFloatStruct()
    {
        return isInstanceOf(FloatStruct.class);
    }
    
    public boolean isAbstractCoordinateFloat()
    {
        return isInstanceOf(AbstractCoordinateFloat.class);
    }
    
    public float[] getCoordinateFloat()
    {
        AbstractCoordinateFloat floatBuffer = (AbstractCoordinateFloat) this.getFieldRuntimeObject();
        return floatBuffer.getArray();
    }
    
    public void setCoordinateFloat(float... array)
    {        
        AbstractCoordinateFloat floatBuffer = (AbstractCoordinateFloat) this.getFieldRuntimeObject();
        floatBuffer.set(array);        
    }
    
    private boolean isInstanceOf(Class b)
    { 
        //isInstanceOf(a, b){ return b.isAssignableFrom(a);}
        return b.isAssignableFrom(field.getType());
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
    
    public float getFloat()
    {
        try {
            return field.getFloat(runtimeParent);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(FloatStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Error in casting and processing"); 
    }
    
    public void setFloat(float value)
    {
        try {       
            field.setFloat(runtimeParent, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(FloatStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public FloatStruct getChildFloatStructOf(FloatStruct parentStruct)
    {
        FloatStruct childStruct = null;
        try {
            childStruct = (FloatStruct)field.get(parentStruct);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(FloatStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return childStruct;
    }
    
    public static List<FloatStructField> getAllStructFields(FloatStruct struct)
    {
        Field fields[] = struct.getClass().getDeclaredFields();
        List<FloatStructField> structFieldList = new ArrayList<>();
        for (Field field : fields) {
            FloatStructField structField = new FloatStructField(struct, field);           
            if ( structField.isPublic() &&
                    !structField.isVolatile())                           
                if(structField.isFloatStruct()) 
                {                             
                    List<FloatStructField> listInner = getAllStructFields(structField.getChildFloatStructOf(struct));
                    structFieldList.addAll(listInner);
                } 
                else if(structField.isFloat())
                    structFieldList.add(structField);
                else if(structField.isAbstractCoordinateFloat())
                    structFieldList.add(structField);                
        }
        return structFieldList;
    }
}
