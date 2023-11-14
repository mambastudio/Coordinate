/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.type;

import coordinate.utility.RangeCheckArray;
import java.util.Objects;
import java.util.function.LongFunction;
import java.util.function.ObjLongConsumer;

/**
 *
 * @author user
 */
public class ValueState {
    protected final Class<?> carrier;
    protected final long byteOffset;
        
    //for traversal in an array loop
    protected final long   arrayElementSize;    
    protected final long  arrayLength;    
        
    //we don't have any use of value byte size here, hence omitted
    protected ValueState(Class<?> carrier, long byteOffset, long arrayLength, long arrayElementSize)
    {
        if(!LayoutValue.isValidCarrier(carrier))
            throw new UnsupportedOperationException("not a suitable carrier");
        this.carrier = carrier;        
        this.byteOffset = byteOffset;
        this.arrayElementSize = arrayElementSize;
        if(arrayLength < 1)
            throw new UnsupportedOperationException("length is not valid");
        this.arrayLength = arrayLength;
    }
    
    public long length()
    {
        return arrayLength;
    }
    
    public void forEachSet(MemoryRegion memory, LongFunction function)
    {
        for(long i = 0; i < length(); i++)
            set(memory, i, function.apply(i));
    }  
        
    public final void set(MemoryRegion memory, Object value){ 
        set(memory, 0, value);
    } 
        
    public final void set(MemoryRegion memory, long index, Object value)
    {
        Objects.requireNonNull(memory, ()-> "no memory assigned for this value state");
        checkAssignable(value.getClass(), carrier);    
        RangeCheckArray.validateIndexSize(index, arrayLength);
        
        if (carrier == int.class) {         
            memory.set(LayoutValue.JAVA_INT, byteOffset + index * arrayElementSize, (int) value);
        } else if (carrier == short.class) {
            memory.set(LayoutValue.JAVA_SHORT, byteOffset + index * arrayElementSize, (short) value);
        } else if (carrier == double.class) {
            memory.set(LayoutValue.JAVA_DOUBLE, byteOffset + index * arrayElementSize, (double) value);
        } else if (carrier == float.class) {            
            memory.set(LayoutValue.JAVA_FLOAT, byteOffset + index * arrayElementSize, (float) value);
        } else if (carrier == char.class) {
            memory.set(LayoutValue.JAVA_CHAR, byteOffset + index * arrayElementSize, (char) value);
        } else if (carrier == byte.class) {
            memory.set(LayoutValue.JAVA_BYTE, byteOffset + index * arrayElementSize, (byte) value);
        } else if (carrier == long.class) {
            memory.set(LayoutValue.JAVA_LONG, byteOffset + index * arrayElementSize, (long) value);
        } else if (carrier == boolean.class) {
            memory.set(LayoutValue.JAVA_BOOLEAN, byteOffset + index * arrayElementSize, (boolean) value);
        } else {
            throw new UnsupportedOperationException("not a value type");
        }
    }
    
    public void forEachGet(MemoryRegion memory, ObjLongConsumer function)
    {
        for(long i = 0; i < length(); i++)
            function.accept(get(memory), i);
    }
    
    public Object get(MemoryRegion memory)
    {
        return get(memory, 0);
    } 
        
    public Object get(MemoryRegion memory, long index)
    {
        Objects.requireNonNull(memory, ()-> "no memory assigned for this value state");
        RangeCheckArray.validateIndexSize(index, arrayLength);
        
        if(carrier == int.class)
            return memory.get(LayoutValue.JAVA_INT, byteOffset + index * arrayElementSize);
        else if(carrier == short.class)
            return memory.get(LayoutValue.JAVA_SHORT, byteOffset + index * arrayElementSize);
        else if(carrier == double.class)
            return memory.get(LayoutValue.JAVA_DOUBLE, byteOffset + index * arrayElementSize);
        else if(carrier == float.class)
            return memory.get(LayoutValue.JAVA_FLOAT, byteOffset + index * arrayElementSize);
        else if(carrier == char.class)
            return memory.get(LayoutValue.JAVA_CHAR, byteOffset + index * arrayElementSize);
        else if(carrier == byte.class)
            return memory.get(LayoutValue.JAVA_BYTE, byteOffset + index * arrayElementSize);
        else if(carrier == long.class)
            return memory.get(LayoutValue.JAVA_LONG, byteOffset + index * arrayElementSize);
        else if(carrier == boolean.class)
            return memory.get(LayoutValue.JAVA_BOOLEAN, byteOffset + index * arrayElementSize);
        throw new UnsupportedOperationException("not a value type");
    }
    
    protected void checkAssignable(Class<?> valueType, Class<?> targetType)
    {
        if (!isAssignable(valueType, targetType)) {
            throw new UnsupportedOperationException("value is not equivalent to " + targetType);
        }
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
    public static ValueState valueState(Class<?> carrier, long byteOffset, long arrayLength, long arrayElementSize)
    {
        return new ValueState(carrier, byteOffset, arrayLength, arrayElementSize);
    }
}
