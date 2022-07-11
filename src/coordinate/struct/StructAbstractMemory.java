/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import coordinate.struct.refl.StructField;
import coordinate.struct.refl.ArrayRefl;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author user
 * @param <GlobalBuffer>
 */
public abstract class StructAbstractMemory<GlobalBuffer> extends StructAbstract {
    
    private long globalIndex = -1;
    private GlobalBuffer globalBuffer;
        
    
    
    //called by method in this class -> setFieldValuesFromGlobalArray()
    protected abstract ByteBuffer getLocalByteFromGlobalBuffer(ByteOrder order);
    
   
    protected byte[] getFieldValuesAsArray()
    {
        ByteBuffer buffer = this.getEmptyLocalByteBuffer(ByteOrder.nativeOrder());        
        int pos = buffer.position(); //current position in global array (translated as local position) 
        for (StructField field : this.getStructFields()) {             
            buffer.position(pos + field.getOffset());            
            if(field.isAbstractCoordinateFloat())
                putFloat(buffer, field.getCoordinateFloat());
            else if(field.isAbstractCoordinateInt())
                putInt(buffer, field.getCoordinateInt());
            else if(field.isBoolean())
                buffer.putInt(field.getBoolean() ? 1 : 0);
            else if(field.isInt())
                buffer.putInt(field.getInt());
             else if(field.isShort())
                buffer.putShort(field.getShort());
            else if(field.isFloat())
                buffer.putFloat(field.getFloat());
            else if(field.isLong())
                buffer.putLong(field.getLong());
            else if(field.isStructure())
            {
                StructAbstractMemory struct = (StructAbstractMemory) field.getFieldObject();
                struct.setGlobal(getGlobalBuffer(), getGlobalIndex()+ field.getOffset());  //because every struct has index location              
                buffer.put(struct.getFieldValuesAsArray());
                 
            }
            else if(field.isArray())
            {
                //TODO (put boolean and struct)
                int arraySize = field.getArraySize();
                Object array = field.getFieldObject();
                if(array instanceof int[])
                    for(int i = 0; i<arraySize; i++)
                        buffer.putInt(ArrayRefl.getInt(array, i));
                else if(array instanceof float[])
                    for(int i = 0; i<arraySize; i++)
                        buffer.putFloat(ArrayRefl.getFloat(array, i));
                else if(array instanceof long[])
                    for(int i = 0; i<arraySize; i++)
                        buffer.putLong(ArrayRefl.getLong(array, i));
                else if(array instanceof double[])
                    for(int i = 0; i<arraySize; i++)
                        buffer.putDouble(ArrayRefl.getDouble(array, i));
                else if(array instanceof boolean[])
                    throw new IllegalStateException("boolean not supported yet");
                else if(array instanceof StructAbstractMemory[])
                    throw new IllegalStateException("structure array not supported yet");
            }
            
        }
        
        return buffer.array();
    }
    
    //transfer from global buffer to field values
    protected abstract void setGlobalBufferFromFieldValues();
    
    public void refreshGlobalBuffer()
    {
        setGlobalBufferFromFieldValues();
    }
    
    //transfer from field values to global buffer      
    protected void  setFieldValuesFromGlobalBuffer()
    {        
        ByteBuffer buffer = getLocalByteFromGlobalBuffer(ByteOrder.nativeOrder());          
        int pos = buffer.position(); //current position in global array (translated as local position)        
        for (StructField field : this.getStructFields()) {
            buffer.position(pos + field.getOffset());            
            if(field.isAbstractCoordinateFloat())
            {
                float[] array = getFloatArray(buffer, field.getCoordinateFloatSize());           
                field.setCoordinateFloat(array);
            }
            else if(field.isAbstractCoordinateInt())
            {
                int[] array = getIntArray(buffer, field.getCoordinateIntSize());                
                field.setCoordinateInt(array);
            }
            else if(field.isBoolean())
                field.setBoolean(buffer.getInt() == 1);
            else if(field.isFloat())
                field.setFloat(buffer.getFloat());
            else if(field.isLong())
                field.setLong(buffer.getLong());
            else if(field.isInt())
                field.setInt(buffer.getInt());  
            else if(field.isShort())
                field.setShort(buffer.getShort());  
            else if(field.isStructure())
            {
                StructAbstractMemory struct = (StructAbstractMemory) field.getFieldObject();
                struct.setGlobal(getGlobalBuffer(), getGlobalIndex() + field.getOffset()); //because every struct has index location
                struct.setFieldValuesFromGlobalBuffer();
            }
            else if(field.isArray())
            {
                //TODO (put boolean and struct)
                int arraySize = field.getArraySize();
                Object array = field.getFieldObject();
                if(array instanceof int[])
                    for(int i = 0; i<arraySize; i++)
                        ArrayRefl.set(array, i, buffer.getInt());
                else if(array instanceof float[])
                    for(int i = 0; i<arraySize; i++)
                        ArrayRefl.set(array, i, buffer.getFloat());
                else if(array instanceof long[])
                    for(int i = 0; i<arraySize; i++)
                        ArrayRefl.set(array, i, buffer.getLong());
                else if(array instanceof double[])
                    for(int i = 0; i<arraySize; i++)
                        ArrayRefl.set(array, i, buffer.getDouble());
                else if(array instanceof boolean[])
                    throw new IllegalStateException("boolean not supported yet");
                else if(array instanceof StructAbstractMemory[])
                    throw new IllegalStateException("structure array not supported yet");
                
            }
        }
    }
    
    public void refreshFieldValues()
    {
        setFieldValuesFromGlobalBuffer();
    }
    
    protected void setGlobal(GlobalBuffer globalBuffer, long globalIndex)
    {
        this.globalBuffer = globalBuffer;
        this.globalIndex = globalIndex;
    }
        
    protected GlobalBuffer getGlobalBuffer()
    {
        return globalBuffer;
    }
    
    protected long getGlobalIndex()
    {
        return globalIndex;
    }
    
    protected ByteBuffer getEmptyLocalByteBuffer(ByteOrder order)
    {
        byte[] array = new byte[getByteSize()];
        ByteBuffer buffer = ByteBuffer.wrap(array);
        return buffer.order(order);
    }
    
    protected void putFloat(ByteBuffer buffer, float... array)
    {
        for(float f: array)
            buffer.putFloat(f);
    }
    
    protected void putInt(ByteBuffer buffer, int... array)
    {
        for(int i: array)
            buffer.putFloat(i);
    }
    
    protected float[] getFloatArray(ByteBuffer buffer, int size)
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
    
    protected int[] getIntArray(ByteBuffer buffer, int size)
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
}
