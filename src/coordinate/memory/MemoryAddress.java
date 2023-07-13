/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory;

import static coordinate.unsafe.UnsafeUtils.copyMemory;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import coordinate.utility.Sweeper;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <M>
 * @param <A>
 */
public abstract class MemoryAddress<M extends MemoryAddress<M, A>, A> {
    
    protected long address;
    protected long capacityBytes;    
        
    //buffer fields
    protected static final Field addressField, capacityField;
    static {
    try {
            addressField = Buffer.class.getDeclaredField("address");
            addressField.setAccessible(true);
            capacityField = Buffer.class.getDeclaredField("capacity");
            capacityField.setAccessible(true);

        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }
    
    protected MemoryAddress(long capacity)
    {
        this.address = getUnsafe().allocateMemory(toAmountBytes(capacity));
        this.capacityBytes = toAmountBytes(capacity);
        
        initSweeper();
    }
    
    protected MemoryAddress(M memory)
    {
        this.address = memory.address;
        this.capacityBytes = memory.capacityBytes;
        
        initSweeper();
    }
    
    protected MemoryAddress(M memory, long offset)
    {
        this.address = memory.address + toAmountBytes(offset);
        this.capacityBytes = memory.capacityBytes - toAmountBytes(offset);
        
        initSweeper();
    }
    
    private void initSweeper()
    {
        //For garbage collection
        Sweeper.getSweeper().register(this, ()->dispose());
    }
        
    protected long toAmountBytes(long amount)
    {
        return amount * sizeOf();
    }
            
    public final long capacity()
    {
        return capacityBytes/sizeOf();
    }
    
    public final long address()
    {
        return address;
    }
        
    public final long address(long offset)
    {
        rangeCheck(toAmountBytes(offset), capacityBytes);
        return address + toAmountBytes(offset);
    }
    
    public void swap(M m)
    {
        long tempAddress = address;
        long tempCapacityBytes = capacityBytes;
        
        address = m.address;
        capacityBytes = m.capacityBytes;
        m.address = tempAddress;
        m.capacityBytes = tempCapacityBytes;
    }
        
    public final ByteBuffer getDirectIntBuffer()
    {
        return getDirectIntBuffer(0, 1);
    }
    
    public final ByteBuffer getDirectIntBuffer(int offset, int capacity)
    {
        rangeCheck(toAmountBytes(offset) + toAmountBytes(capacity) - 1, this.capacityBytes);
        ByteBuffer bb = ByteBuffer.allocateDirect((int) toAmountBytes(capacity)).order(ByteOrder.nativeOrder());
        try {
            addressField.setLong(bb, address + toAmountBytes(offset));
            capacityField.setInt(bb,     (int) toAmountBytes(capacity)); 
           
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(MemoryAddress.class.getName()).log(Level.SEVERE, null, ex);
        }             
        return bb;
    }
    
    //Size should encompass the fromIndex and toIndex 
    protected final void rangeCheck(long offset, long size) {
        if (offset < 0)
            throw new IndexOutOfBoundsException("offset is less than zero " + offset);
        if (offset >= size)
            throw new IndexOutOfBoundsException("offset is beyond range, offset = " + offset + " size = " +size);        
    }    
    
    private Optional<Class<?>> getPrimitiveArrayObject(Object object)
    {
        /* Check if the given object is an array. */
        if (object.getClass().isArray()) {

            Class<?> componentType;
            componentType = object.getClass().getComponentType();
            return Optional.of(componentType);
        }
        return Optional.empty();           
    }
    
    private int getPrimitiveArrayLength(Object object)
    {
        Optional<Class<?>> option = getPrimitiveArrayObject(object);    
        option.orElseThrow(()-> new UnsupportedOperationException("not a primitive array"));
   
        if (option.get().isPrimitive()) {
            if (boolean.class.isAssignableFrom(option.get())) {
                boolean[] arr = (boolean[]) object;
                return arr.length;
            }

            else if (byte.class.isAssignableFrom(option.get())) {
                byte[] arr = (byte[]) object;
                return arr.length;
            }       

            else if (int.class.isAssignableFrom(option.get())) {
                int[] arr = (int[]) object;
                return arr.length;
            }
        }
        throw new UnsupportedOperationException("primitive array not supported yet");
    }
    
    //for why we use 16, refer to https://mail.openjdk.org/pipermail/panama-dev/2021-November/015852.html 
    public void put(A array, long offset)
    {
        int length = getPrimitiveArrayLength(array);
        rangeCheck(offset + length-1, capacity()); //check array copy within bounds for this native array
        copyMemory(array, 16, null, address(), toAmountBytes(length));
    }
    
    public void get(A array, long offset)
    {
        int length = getPrimitiveArrayLength(array);
        rangeCheck(offset + length-1, capacity()); //check array copy within bounds for this native array
        copyMemory(null, address(), array, 16, toAmountBytes(length));
    }
        
    public abstract void dispose();
    public abstract int sizeOf();
    public abstract M getMemory(long offset);
}
