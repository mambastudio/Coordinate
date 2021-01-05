/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.field;

import coordinate.generic.AbstractCoordinateInteger;
import coordinate.struct.IntStruct;
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
public class IntStructField {
    private Field field;
    private IntStruct runtimeParent;
    
    private IntStructField(IntStruct runtimeOwner, Field field)
    {
        if(!(isInt(field)
                || (runtimeOwner instanceof IntStruct)))
            throw new UnsupportedOperationException("field should be int");
        this.field = field;
        this.runtimeParent = runtimeOwner;
    }
   
    private boolean isInt(Field field)
    {
        return field.getType().isPrimitive()? field.getType().getName().equals("int") : false;
    }
    
    public boolean isInt()
    {
        return field.getType().isPrimitive()? field.getType().getName().equals("int") : false;
    }
    
    public boolean isAbstractCoordinateInt()
    {
        return isInstanceOf(AbstractCoordinateInteger.class);
    }
    
    public int getInt()
    {
        try {
            return field.getInt(runtimeParent);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(FloatStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Error in casting and processing"); 
    }
    
    public int[] getCoordinateInt()
    {
        AbstractCoordinateInteger intBuffer = (AbstractCoordinateInteger) this.getFieldRuntimeObject();
        return intBuffer.getArray();
    }
    
    public void setCoordinateInt(int... array)
    {        
        AbstractCoordinateInteger intBuffer = (AbstractCoordinateInteger) this.getFieldRuntimeObject();
        intBuffer.set(array);        
    }
    
    private boolean isInstanceOf(Class b)
    { 
        //isInstanceOf(a, b){ return b.isAssignableFrom(a);}
        return b.isAssignableFrom(field.getType());
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
    
    public boolean isIntStruct()
    {
        return isInstanceOf(IntStruct.class);
    }
    
    public int getNumberOfInts()
    {
        if(isInt())
            return 1;
        if(isAbstractCoordinateInt())
            return getCoordinateInt().length;
        else
            throw new UnsupportedOperationException("field should be float");
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
            
    public void setInt(int value)
    {
        try {       
            field.setInt(runtimeParent, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(IntStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public IntStruct getChildIntStructOf(IntStruct parentStruct)
    {
        IntStruct childStruct = null;
        try {
            childStruct = (IntStruct)field.get(parentStruct);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(IntStructField.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return childStruct;
    }
    
    public static List<IntStructField> getAllStructFields(IntStruct struct)
    {
        Field fields[] = struct.getClass().getDeclaredFields();
        List<IntStructField> structFieldList = new ArrayList<>();
        for (Field field : fields) {
            IntStructField structField = new IntStructField(struct, field);           
            if ( structField.isPublic() &&
                    !structField.isVolatile())                           
                if(structField.isIntStruct()) 
                {                             
                    List<IntStructField> listInner = getAllStructFields(structField.getChildIntStructOf(struct));
                    structFieldList.addAll(listInner);
                } 
                else if(structField.isInt())
                    structFieldList.add(structField);
                else if(structField.isAbstractCoordinateInt())
                    structFieldList.add(structField);                
        }
        return structFieldList;
    }
}
