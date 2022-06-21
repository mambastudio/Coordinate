/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.structbyte;

import coordinate.struct.refl.StructureField;
import coordinate.struct.AbstractByteStruct;
import coordinate.struct.AbstractStruct;
import coordinate.struct.refl.ArrayRefl;
import coordinate.utility.Value1Di;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author user
 */
public  class Structure extends AbstractByteStruct<byte[]>{
    private final StructureField[] fields;    
    private final int alignment;
    private final int byteSize;
    
    //global array
    private byte[] globalArray = null;
    private int globalArrayIndex = -1;
    
    protected Structure()
    {
        fields =  initFields();
        alignment = setupAlignment();
        byteSize = setupByteSize(new Value1Di());
        this.initOffset();
    }
    
    //construtor call
    public final int setupAlignment()
    {
        if(fields.length == 1)
        {
            return fields[0].setupAlignment();
        }
        else
        {
            StructureField maxByteField = Collections.max(Arrays.asList(fields), 
                   (a, b) -> Integer.compare(a.setupAlignment(), b.setupAlignment()));   

            return maxByteField.getAlignment();
        }
    }
    
    //construtor call
    protected final int setupByteSize(Value1Di offset)
    {
        for (StructureField field : fields) {
            field.setupByteSize(offset);                
        }       
        return computeAlignmentOffset(offset.x, alignment);        
    }
    
    //called from outside struct or called independently, if need arises
    @Override
    public final void setupOffset(Value1Di offset)
    {    
        for (StructureField field : fields) {
            field.setupOffset(offset);
        }
    }
    
    //called from outside struct or called independently, if need arises
    public final void initOffset()
    {
        setupOffset(new Value1Di());
    }
    
    public String getLayout()
    {
        initOffset();
        StringBuilder builder = new StringBuilder();
        for(StructureField field : fields)
        {
            builder.append("alignment ").append(field.getAlignment()).append(" ");
            builder.append("byte size ").append(field.getByteSize()).append("\n");            
        }
        builder.append("field offsets: ").append(Arrays.toString(getOffsets())).append("\n");
        builder.append("byte size of struct: ").append(getByteSize());
        
        return builder.toString();
    }
    
    public StructureField[] getAllFields()
    {
        return fields;
    }
    
    private StructureField[] initFields()
    {
        StructureField[] nfields = StructureField.getAllStructFieldsAsArray(this);
        return nfields;
    }
    
    protected int getAlignment()
    {
        return alignment;
    }
        
    public StructureField getLastField()
    {
        return fields[fields.length-1];
    }
       
    public int[] getOffsets()
    {
        int[] offsets = new int[fields.length];
        for(int i = 0; i<offsets.length; i++)
            offsets[i] = fields[i].getOffset();
        return offsets;
    }
            
    @Override
    public int getByteSize()
    {
        return byteSize;
    }
    
    protected final int getGlobalArrayIndex()
    {
        return globalArrayIndex;
    }
    
    public final void setGlobalArray(byte[] globalArray, int index)
    {
        setGlobalBuffer(globalArray);
        setGlobalIndex(index * getByteSize());
    }
    
    public final void refreshGlobalArray()
    {
        if(globalArray == null || globalArrayIndex == -1) return;
        byte [] array = getFieldValuesAsArray();               
        System.arraycopy(array, 0, globalArray, globalArrayIndex, getByteSize());
        
    }
    
    public final void refreshFieldValues()
    {
        if(globalArray == null || globalArrayIndex == -1) return;
        this.setFieldValuesFromGlobalArray();
    }
       
    @Override
    public final byte[] getFieldValuesAsArray()
    {
        ByteBuffer buffer = this.getEmptyLocalByteBuffer(ByteOrder.nativeOrder());        
        int pos = buffer.position(); //current position in global array (translated as local position) 
        for (StructureField field : fields) {             
            buffer.position(pos + field.getOffset());            
            if(field.isAbstractCoordinateFloat())
                putFloat(buffer, field.getCoordinateFloat());
            else if(field.isAbstractCoordinateInt())
                putInt(buffer, field.getCoordinateInt());
            else if(field.isBoolean())
                buffer.putInt(field.getBoolean() ? 1 : 0);
            else if(field.isInt())
                buffer.putInt(field.getInt());
            else if(field.isFloat())
                buffer.putFloat(field.getFloat());
            else if(field.isLong())
                buffer.putLong(field.getLong());
            else if(field.isStructure())
            {
                Structure struct = (Structure) field.getFieldObject();
                struct.setGlobalBuffer(globalArray);
                struct.setGlobalIndex(globalArrayIndex+ field.getOffset());
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
                else if(array instanceof Structure[])
                    throw new IllegalStateException("structure array not supported yet");
            }
            
        }
        
        return buffer.array();
    }
    
    @Override
    public final void setFieldValuesFromGlobalArray()
    {        
        ByteBuffer buffer = getLocalByteBuffer(ByteOrder.nativeOrder());          
        int pos = buffer.position(); //current position in global array (translated as local position)        
        for (StructureField field : fields) {
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
            else if(field.isStructure())
            {
                Structure struct = (Structure) field.getFieldObject();
                struct.setGlobalBuffer(globalArray);
                struct.setGlobalIndex(globalArrayIndex+ field.getOffset());
                struct.setFieldValuesFromGlobalArray();
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
                else if(array instanceof Structure[])
                    throw new IllegalStateException("structure array not supported yet");
                
            }
        }
    }
        
    private ByteBuffer getEmptyLocalByteBuffer(ByteOrder order)
    {
        byte[] array = new byte[getByteSize()];
        ByteBuffer buffer = ByteBuffer.wrap(array);
        return buffer.order(order);
    }
    
    private ByteBuffer getLocalByteBuffer(ByteOrder order)
    {
        if(globalArray == null)
            return null;           
        ByteBuffer buffer = ByteBuffer.wrap(globalArray, globalArrayIndex, getByteSize());
        return buffer.order(order);
    }
    
    private void putFloat(ByteBuffer buffer, float... array)
    {
        for(float f: array)
            buffer.putFloat(f);
    }
    
    private void putInt(ByteBuffer buffer, int... array)
    {
        for(int i: array)
            buffer.putFloat(i);
    }
    
    private float[] getFloatArray(ByteBuffer buffer, int size)
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
    
    private int[] getIntArray(ByteBuffer buffer, int size)
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
    
    //https://en.wikipedia.org/wiki/Data_structure_alignment    
    protected int computeAlignmentOffset(int offset, int align)
    {
        return (offset + align - 1) & -align;
    }  
    
    @Override
    public void setGlobalBuffer(byte[] globalBuffer)
    {
        this.globalArray = globalBuffer;
    }
    
    @Override
    public void setGlobalIndex(int globalIndex)
    {
        this.globalArrayIndex = globalIndex;
    }
}
