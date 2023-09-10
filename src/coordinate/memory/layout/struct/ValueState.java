/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.layout.struct;

import coordinate.memory.layout.LayoutValue;
import java.nio.ByteBuffer;

/**
 *
 * @author user
 */
public final class ValueState {
    private final Class<?> carrier;
    private final ByteBuffer buffer;
    private final int offset;
    
    private ValueState(Class<?> carrier, int offset, ByteBuffer buffer)
    {
        if(!LayoutValue.isValidCarrier(carrier))
            throw new UnsupportedOperationException("not a suitable carrier");
        this.carrier = carrier;
        this.buffer = buffer;
        this.offset = offset;
    }
    
    public void set(Object value)
    {
        if (!isAssignable(value.getClass(), carrier)) {
            throw new UnsupportedOperationException("value is not assignable from " + carrier.getSimpleName());
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
        } else if (carrier == boolean.class) {
            buffer.put(offset, (boolean) value ? (byte) 1 : (byte) 0);
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
        else if(carrier == boolean.class)
            return buffer.get(offset) > (byte)0;
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
            }
        }
        return false;
    }
    
    public static ValueState valueState(Class<?> carrier, int offset, ByteBuffer buffer)
    {
        return new ValueState(carrier, offset, buffer);
    }
}
