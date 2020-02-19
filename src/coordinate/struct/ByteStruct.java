/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import coordinate.struct.refl.ByteStructField;
import coordinate.struct.refl.ByteStructInfo;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 *
 * @author user
 */
public abstract class ByteStruct {
    
    private byte[] globalArray = null;
    private int globalArrayIndex = -1;
    private int[] offsets;
    
    protected final int getGlobalArrayIndex()
    {
        return globalArrayIndex;
    }
    
    //TODO: Soughtout the global array for inner struct
    protected final void setGlobalArray(byte[] globalArray, int index)
    {
        this.globalArray = globalArray;
        this.globalArrayIndex = index * getSize(); 
        
    }
    
    protected void setOffsets(int[] offsets)
    {
        this.offsets = offsets;
    }
    
    protected int[] getOffsets()
    {
        return offsets;
    }
    
    public final void refreshGlobalArray()
    {
        if(globalArray == null || globalArrayIndex == -1) return;
        byte [] array = getArray();               
        System.arraycopy(array, 0, globalArray, globalArrayIndex, getSize());
        
    }
    
    public final void skipByte(ByteBuffer bb, int skip) {
        bb.position( bb.position() + skip);
    }
     
    protected final ByteBuffer getLocalByteBuffer(ByteOrder order)
    {
        if(globalArray == null)
            return null;        
        ByteBuffer buffer = ByteBuffer.wrap(globalArray, globalArrayIndex, getSize());
        return buffer.order(order);
    }
    
    protected final ByteBuffer getEmptyLocalByteBuffer(ByteOrder order)
    {
        byte[] array = new byte[getSize()];
        ByteBuffer buffer = ByteBuffer.wrap(array);
        return buffer.order(order);
    }
    
    public static void putFloat(ByteBuffer buffer, float... array)
    {
        for(float f: array)
            buffer.putFloat(f);
    }
    
    public static float[] getFloatArray(ByteBuffer buffer, int size)
    {
        if(size == 0) return null;
        else
        {
            float[] array = new float[size];
            for(int i = 0; i<size; i++)
                array[i] = buffer.getFloat();
            return array;
        }
    }
    
    public static void putInt(ByteBuffer buffer, int... array)
    {
        for(int i: array)
            buffer.putFloat(i);
    }
    
    public static int[] getIntArray(ByteBuffer buffer, int size)
    {
        if(size == 0) return null;
        else
        {
            int[] array = new int[size];
            for(int i = 0; i<size; i++)
                array[i] = buffer.getInt();
            return array;
        }
    }
    
    protected boolean isInstanceOf(Class a, Class b)
    {
        return b.isAssignableFrom(a);
    }
    
    public int getSize()
    {      
        if(offsets == null)
        {
            ByteStructInfo info = new ByteStructInfo(this.getClass());
            offsets = info.offsets();
        }
        return offsets[offsets.length - 1];
    }
    
    public ByteStruct getObject()
    {
        return this;
    }
    
    //used to be abstract
    public void  initFromGlobalArray()
    {
        
        ByteBuffer buffer = this.getLocalByteBuffer(ByteOrder.nativeOrder());   
        List<ByteStructField> fields = ByteStructField.getAllStructFields(this);
        int[] offsetValues = this.getOffsets();
        int pos = buffer.position(); //current position in global array (translated as local position)        
        for(int i = 0; i<fields.size(); i++)
        {
            ByteStructField field = fields.get(i);            
            buffer.position(pos + offsetValues[i]);            
            if(field.isAbstractCoordinateFloat())
            {                   
                float[] array = ByteStruct.getFloatArray(buffer, field.getCoordinateFloatSize());           
                field.setCoordinateFloat(array);
            }
            else if(field.isAbstractCoordinateInt())
            {
                int[] array = ByteStruct.getIntArray(buffer, field.getCoordinateIntSize());                
                field.setCoordinateInt(array);
            }
            else if(field.isBoolean())
            {
                field.setBoolean(buffer.getInt() == 1);
            }
            else if(field.isFloat())
            {                
                field.setFloat(buffer.getFloat());
            }
            else if(field.isLong())
            {                
                field.setLong(buffer.getLong());
            }            
            else if(field.isInt())
            {
                field.setInt(buffer.getInt());
            }
        }
    }
    
    //used to be abstract
    public byte [] getArray()
    {
        ByteBuffer buffer = this.getEmptyLocalByteBuffer(ByteOrder.nativeOrder());
        List<ByteStructField> fields = ByteStructField.getAllStructFields(this);
        int pos = buffer.position(); //current position in global array (translated as local position) 
        
        for(int i = 0; i<fields.size(); i++)                                                
        {
            ByteStructField field = fields.get(i);  
            buffer.position(pos + offsets[i]);
            
            if(field.isAbstractCoordinateFloat())
                ByteStruct.putFloat(buffer, field.getCoordinateFloat());
            else if(field.isAbstractCoordinateInt())
                ByteStruct.putInt(buffer, field.getCoordinateInt());
            else if(field.isBoolean())
                buffer.putInt(field.getBoolean() ? 1 : 0);
            else if(field.isInt())
                buffer.putInt(field.getInt());
            else if(field.isFloat())
                buffer.putFloat(field.getFloat());
            else if(field.isLong())
                buffer.putLong(field.getLong());
        }
        
        return buffer.array();
    }
    
    public final byte[] getGlobalArray()
    {
        return globalArray;
    }
}
