/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.structbyte;

import coordinate.struct.refl.ArrayRefl;
import coordinate.generic.AbstractCoordinate;
import coordinate.generic.AbstractCoordinateFloat;
import coordinate.generic.AbstractCoordinateInteger;
import coordinate.utility.Value1Di;
import coordinate.struct.annotation.arraysize;
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
public class StructureField {
    private Field field;
    private Structure parentObject;
    
    //need here to put info on struct from array
    //TODO
    
    private int alignment;
    private int byteSize;
    private int offset;
    
    private StructureField(Structure parentObject, Field field)
    {
        if(!(field.getType().isPrimitive() 
                || (parentObject instanceof AbstractCoordinate)
                || (parentObject instanceof Structure)))
            
            throw new UnsupportedOperationException("field not supported yet");
        this.field = field;
        this.parentObject = parentObject;           
    }
    
    //called by struct (in constructor)
    public final int setupAlignment()
    {
        if(this.isPrimitive())        
            alignment =  getByteSizeofPrimitive((Class<? extends Number>) field.getType());        
        else if(this.isAbstractCoordinate())
            alignment =  getByteSizeofAbstractCoordinate((Class<? extends AbstractCoordinate>) field.getType());
        else if(this.isArray())
        {
            if(this.isArrayClassPrimitive())        
                alignment =  getByteSizeofPrimitive((Class<? extends Number>) getArrayClass());        
            else if(this.isArrayClassAbstractCoordinate())
                alignment =  getByteSizeofAbstractCoordinate((Class<? extends AbstractCoordinate>) getArrayClass());
            else
            {
                Structure struct = (Structure)getObject(getArrayClass());
                alignment =  struct.getByteSize();
            }
        }
        else
            alignment =  ((Structure)getFieldObject()).setupAlignment();
        
        return alignment;
    }    
   
    //called by struct (in constructor)
    public final void setupByteSize(Value1Di offset)
    {
        if(this.isPrimitive())
        {
            byteSize = getByteSizeofPrimitive((Class<? extends Number>) field.getType());
            offset.x = computeAlignmentOffset(offset.x, alignment);
            offset.x += byteSize;         
            
        }
        else if(this.isAbstractCoordinate())
        {
            byteSize = getByteSizeofAbstractCoordinate((Class<? extends AbstractCoordinate>) field.getType());
            offset.x = computeAlignmentOffset(offset.x, alignment);
            offset.x += byteSize;
        }
        else if(this.isArray())
        {
            int arraysize = getArraySize();
            
            if(this.isArrayClassPrimitive())
            {
                byteSize = getByteSizeofPrimitive((Class<? extends Number>) getArrayClass()) * arraysize;
                offset.x = computeAlignmentOffset(offset.x, alignment);
                offset.x += byteSize;         
            }
            else if(this.isArrayClassAbstractCoordinate())
            {
                byteSize = getByteSizeofAbstractCoordinate((Class<? extends AbstractCoordinate>) getArrayClass()) * arraysize;
                offset.x = computeAlignmentOffset(offset.x, alignment);
                offset.x += byteSize;
            }
            else
            {
                Structure structure =(Structure)getObject(getArrayClass());
                byteSize = structure.getByteSize() * arraysize;
                offset.x = computeAlignmentOffset(offset.x, alignment);
                offset.x += byteSize;
            }
        }
        else
        {
            Structure structure = (Structure)getFieldObject();
            byteSize = structure.getByteSize();
            offset.x = computeAlignmentOffset(offset.x, alignment);
            offset.x += byteSize;
        }
    }
    
    //called outside independently
    public final void setupOffset(Value1Di offset)
    {
        if(this.isPrimitive())
        {
            offset.x = computeAlignmentOffset(offset.x, alignment);
            this.offset = offset.x;
            offset.x += byteSize;
        }
        else if(this.isAbstractCoordinate())
        {
            offset.x = computeAlignmentOffset(offset.x, alignment);
            this.offset = offset.x;
            offset.x += byteSize;
        }
        else if(this.isArray())
        {
            offset.x = computeAlignmentOffset(offset.x, alignment);
            this.offset = offset.x;
            offset.x += byteSize;
        }
        else
        {
            Structure structure = (Structure)getFieldObject();
            offset.x = computeAlignmentOffset(offset.x, alignment); //struct alignment
            this.offset = offset.x;
            offset.x += byteSize;
            structure.setupOffset(new Value1Di()); //Notice here we start from scratch ( = 0)
        }
    }
    
        
    public int getAlignment()
    {
        return alignment;
    }
    
    public void setOffset(int address)
    {
        this.offset = address;
    }
    
    public int getOffset()
    {
        return offset;
    }
    
    public Class getDeclaringClass()
    {
        return field.getDeclaringClass();
    }
    
    public Class getFieldTypeClass()
    {
        if(this.isArray())
            return getArrayClass();
        else
            return field.getType();
    }
    
    public Object getDeclaredObject()
    {
        try {
            return field.get(parentObject);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getName()
    {
        return field.getName();
    }
    
    public int getByteSize()
    {
        return byteSize;
    }
    
    public int getArraySize()
    {
        if(!isArray())
            throw new IllegalArgumentException("field is not an array.");
        else if(field.isAnnotationPresent(arraysize.class))
        {
            arraysize annotation = field.getAnnotation(arraysize.class);
            return annotation.value();
        }
        else
            throw new IllegalArgumentException("no annotation");
    }
    
    private Class getArrayClass()
    {
        if(isArray())
            return field.getType().getComponentType();
        else
            throw new IllegalArgumentException("this is not an array class");
    }
    
    public boolean isPrimitive()
    {
        return field.getType().isPrimitive();
    }
    
    public boolean isBoolean()
    {
        return isPrimitive()? field.getType().getName().equals("boolean") : false;
    }
    
    public boolean isLong()
    {
        return isPrimitive()? field.getType().getName().equals("long") : false;
    }
    
    public boolean isFloat()
    {
        return isPrimitive()? field.getType().getName().equals("float") : false;
    }
    
    public boolean isInt()
    {
        return isPrimitive()? field.getType().getName().equals("int") : false;
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
    
    public boolean isStructure()
    {
        if(isArray())
            return false;
        return isInstanceOf(Structure.class);
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
    
    public boolean isParentSameClass()
    {
        return isInstanceOf(getDeclaringClass());
    }
    
    public boolean isArray()
    {
        return field.getType().isArray();
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
    
    public boolean isArrayClassStructure()
    {
        if(isArray())
        {
            Class clazz = getArrayClass();
            return this.isInstanceOf(clazz, Structure.class);
        }
        return false;
    }
    
    public boolean isFieldNull()
    {
        try {
            return field.get(parentObject) == null;
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public static<S extends Structure> List<StructureField> getAllStructFields(S struct)
    {
        Field fields[] = struct.getClass().getDeclaredFields();
        ArrayList<StructureField> structFieldList = new ArrayList<>();
        for (Field field : fields) {
            StructureField structField = new StructureField(struct, field);           
            if ( structField.isPublic() &&
                    !structField.isVolatile())                           
                if(structField.isStructure())                 
                    if(structField.isParentSameClass())
                        throw new UnsupportedOperationException("detected recursion in class");
                    else
                    {                
                        structField.initialize(); //initialize field if it's a struct                                        
                        structFieldList.add(structField);
                    } 
                else if(structField.isArray())
                {
                    
                    structField.initialize(); //initialize field if it's a struct
                    structFieldList.add(structField);
                }
                else
                {          
                    structField.initialize();
                    structFieldList.add(structField);
                }
        }
        return structFieldList;
    } 
    
    public static<S extends Structure> StructureField[] getAllStructFieldsAsArray(S struct)
    {
        List<StructureField> list = getAllStructFields(struct);
        return list.toArray(new StructureField[list.size()]);
    }
    
    private boolean isInstanceOf(Class b)
    { 
        //isInstanceOf(a, b){ return b.isAssignableFrom(a);}
        //return b.isAssignableFrom(field.getType());
        return isInstanceOf(b, getFieldTypeClass());
    }
    
    private boolean isInstanceOf(Class a, Class b)
    { 
        //isInstanceOf(a, b){ return b.isAssignableFrom(a);}
        return a.isAssignableFrom(b);
    }
    
    private int getByteSizeofPrimitive(Class<? extends Number> number)
    {         
        switch (number.getName()) {
            case "char":
                return 2;
            case "short":
                return 2;
            case "int":
            case "float":
            case "boolean":
                return 4;
            case "long":
                return 8;
            case "double":
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
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void initialize()
    {        
        if(this.isArray())
        {              
            int size = getArraySize();
            Object array = ArrayRefl.newInstance(getFieldTypeClass(), size);           
            setFieldObject(array);
            
        }        
        else if(this.isAbstractCoordinate() || this.isStructure()){            
            Object object = getObject(getFieldTypeClass());
            setFieldObject(object);
        }
    }
    
    public void setFieldObject(Object object)
    {
        try {
            field.set(parentObject, object);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Object getFieldObject()
    {
        try {
            return field.get(parentObject);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new IllegalStateException("unable to get object of field");
    }
    
    
    public float getFloat()
    {
        try {
            return field.getFloat(parentObject);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Error in casting and processing"); 
    }
    
    public void setFloat(float value)
    {
        try {       
            field.setFloat(parentObject, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public float[] getCoordinateFloat()
    {
        AbstractCoordinateFloat floatBuffer = (AbstractCoordinateFloat) this.getFieldObject();
        return floatBuffer.getArray();
    }
    
    public void setCoordinateFloat(float... array)
    {        
        AbstractCoordinateFloat floatBuffer = (AbstractCoordinateFloat) this.getFieldObject();
        floatBuffer.set(array);        
    }
    
    public int getCoordinateFloatSize()
    {
        AbstractCoordinateFloat floatBuffer = (AbstractCoordinateFloat) this.getFieldObject();
        return floatBuffer.getSize();
    }
    
    public int[] getCoordinateInt()
    {
        AbstractCoordinateInteger intBuffer = (AbstractCoordinateInteger) this.getFieldObject();
        return intBuffer.getArray();
    }
    
    public void setCoordinateInt(int... array)
    {        
        AbstractCoordinateInteger intBuffer = (AbstractCoordinateInteger) this.getFieldObject();
        intBuffer.set(array);        
    }
    
    public int getCoordinateIntSize()
    {
        AbstractCoordinateInteger intBuffer = (AbstractCoordinateInteger) this.getFieldObject();
        return intBuffer.getSize();
    }
    
    public boolean getBoolean()
    {
        try {
            return field.getBoolean(parentObject);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Error in casting and processing"); 
    }
    
    public void setBoolean(boolean value)
    {
        try {       
            field.setBoolean(parentObject, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getInt()
    {
        try {
            return field.getInt(parentObject);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Error in casting and processing"); 
    }
    
    public void setInt(int value)
    {
        try {       
            field.setInt(parentObject, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public long getLong()
    {
        try {
            return field.getLong(parentObject);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Error in casting and processing"); 
    }
    
    public void setLong(long value)
    {
        try {       
            field.setLong(parentObject, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(StructureField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //https://en.wikipedia.org/wiki/Data_structure_alignment    
    protected int computeAlignmentOffset(int offset, int align)
    {
        return (offset + align - 1) & -align;
    }
}
