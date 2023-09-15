/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.layout.struct;

import coordinate.memory.layout.LayoutValue;
import coordinate.utility.RangeCheck;
import java.nio.ByteBuffer;

/**
 *
 * @author user
 */
public final class ValueState {
    private final Class<?> carrier;
    private final ByteBuffer buffer;
    private final int offset;
    
    //for traversal in an array loop
    private final int   arrayElementSize;    
    private final long  arrayLength;    
        
    //we don't have any use of value byte size here, hence omitted
    private ValueState(Class<?> carrier, int offset, int arrayElementSize, long arrayLength, ByteBuffer buffer)
    {
        if(!LayoutValue.isValidCarrier(carrier))
            throw new UnsupportedOperationException("not a suitable carrier");
        this.carrier = carrier;
        this.buffer = buffer;
        this.offset = offset;
        this.arrayElementSize = arrayElementSize;
        if(arrayLength < 1)
            throw new UnsupportedOperationException("length is not valid");
        this.arrayLength = arrayLength;
    }
    
    public long length()
    {
        return arrayLength;
    }
    
    public void set(Object value)
    {
        if (!isAssignable(value.getClass(), carrier)) {
            throw new UnsupportedOperationException("value is not equivalent to " + carrier.getSimpleName());
        }
        
        if (carrier == int.class) {
            buffer.putInt(offset, (int) value);
        } else if (carrier == short.class) {
            buffer.putShort(offset, (short) value);
        } else if (carrier == double.class) {
            buffer.putDouble(offset, (double) value);
        } else if (carrier == float.class) {
            buffer.putFloat(offset, (float) value);
        } else if (carrier == char.class) {
            buffer.putChar(offset, (char) value);
        } else if (carrier == byte.class) {
            buffer.put(offset, (byte) value);
        } else if (carrier == long.class) {
            buffer.putLong(offset, (long) value);
        } else if (carrier == boolean.class) {
            buffer.put(offset, (boolean) value ? (byte) 1 : (byte) 0);
        } else {
            throw new UnsupportedOperationException("not a value type");
        }
    }
    
    public void set(long index, Object value)
    {
        if (!isAssignable(value.getClass(), carrier)) {
            throw new UnsupportedOperationException("value is not equivalent to " + carrier.getSimpleName());
        }
        
        RangeCheck.rangeCheck(index, arrayLength);
        
        if (carrier == int.class) {
            buffer.putInt((int) (offset + index * arrayElementSize), (int) value);
        } else if (carrier == short.class) {
            buffer.putShort((int) (offset + index * arrayElementSize), (short) value);
        } else if (carrier == double.class) {
            buffer.putDouble((int) (offset + index * arrayElementSize), (double) value);
        } else if (carrier == float.class) {
            buffer.putFloat((int) (offset + index * arrayElementSize), (float) value);
        } else if (carrier == char.class) {
            buffer.putChar((int) (offset + index * arrayElementSize), (char) value);
        } else if (carrier == byte.class) {
            buffer.put((int) (offset + index * arrayElementSize), (byte) value);
        } else if (carrier == long.class) {
            buffer.putLong((int) (offset + index * arrayElementSize), (long) value);
        } else if (carrier == boolean.class) {
            buffer.put((int) (offset + index * arrayElementSize), (boolean) value ? (byte) 1 : (byte) 0);
        } else {
            throw new UnsupportedOperationException("not a value type");
        }
    }
    
    public Object get()
    {                
        if(carrier == int.class)
            return buffer.getInt(offset);
        else if(carrier == short.class)
            return buffer.getShort(offset);
        else if(carrier == double.class)
            return buffer.getDouble(offset);
        else if(carrier == float.class)
            return buffer.getFloat(offset);
        else if(carrier == char.class)
            return buffer.getChar(offset);
        else if(carrier == byte.class)
            return buffer.get(offset);
        else if(carrier == long.class)
            return buffer.getLong(offset);
        else if(carrier == boolean.class)
            return buffer.get(offset) > (byte)0;
        throw new UnsupportedOperationException("not a value type");
    }
    
    public Object get(long index)
    {                
        RangeCheck.rangeCheck(index, arrayLength);
        
        if(carrier == int.class)
            return buffer.getInt((int) (offset + index * arrayElementSize));
        else if(carrier == short.class)
            return buffer.getShort((int) (offset + index * arrayElementSize));
        else if(carrier == double.class)
            return buffer.getDouble((int) (offset + index * arrayElementSize));
        else if(carrier == float.class)
            return buffer.getFloat((int) (offset + index * arrayElementSize));
        else if(carrier == char.class)
            return buffer.getChar((int) (offset + index * arrayElementSize));
        else if(carrier == byte.class)
            return buffer.get((int) (offset + index * arrayElementSize));
        else if(carrier == long.class)
            return buffer.getLong((int) (offset + index * arrayElementSize));
        else if(carrier == boolean.class)
            return buffer.get((int) (offset + index * arrayElementSize)) > (byte)0;
        throw new UnsupportedOperationException("not a value type");
    }
    
    private static boolean isAssignable(Class<?> valueType, Class<?> targetType) {
        if (valueType.equals(targetType)) {
            return true;
        }
        if (targetType.isPrimitive()) {
            if (targetType == int.class && (valueType == Integer.class || valueType == int.class)) {
                return true;
            } else if (targetType == short.class && (valueType == Short.class || valueType == short.class)) {
                return true;
            } else if (targetType == double.class && (valueType == Double.class || valueType == double.class)) {
                return true;
            } else if (targetType == float.class && (valueType == Float.class || valueType == float.class)) {
                return true;
            } else if (targetType == char.class && (valueType == Character.class || valueType == char.class)) {
                return true;
            } else if (targetType == byte.class && (valueType == Byte.class || valueType == byte.class)) {
                return true;
            } else if (targetType == boolean.class && (valueType == Boolean.class || valueType == boolean.class)) {
                return true;
            } else if (targetType == long.class && (valueType == Long.class || valueType == long.class)) {
                return true;
            }
        }
        return false;
    }
    
    //we don't have any use of value byte size here, hence omitted
    public static ValueState valueState(Class<?> carrier, int offset, int arrayElementSize, long arrayLength, ByteBuffer buffer)
    {
        return new ValueState(carrier, offset, arrayElementSize, arrayLength, buffer);
    }
}
