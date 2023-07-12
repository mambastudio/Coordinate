/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memory;

import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import coordinate.utility.Sweeper;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <M>
 */
public abstract class MemoryAddress<M extends MemoryAddress> {
    
    protected final long address;
    protected final long capacity;    
        
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
    
    public MemoryAddress(long capacity)
    {
        this.address = capacity;
        this.capacity = calculateCapacity(capacity);
        
        initSweeper();
    }
    
    public MemoryAddress(MemoryAddress pointer)
    {
        this.address = pointer.address;
        this.capacity = calculateCapacity(pointer);
        
        initSweeper();
    }
    
    private void initSweeper()
    {
        //For garbage collection
        Sweeper.getSweeper().register(this, ()->dispose());
    }
    
    private long calculateCapacity(MemoryAddress address)
    {
        return address.capacity * address.sizeOf()/sizeOf();
    }
    
    private long calculateCapacity(long capacity)
    {
        return getUnsafe().allocateMemory(capacity * sizeOf());
    }
    
    public final long capacity()
    {
        return capacity/sizeOf();
    }
    
    public final long address()
    {
        return address;
    }
        
    public final long address(long offset)
    {
        rangeCheck(offset * sizeOf(), capacity);
        return address + offset * sizeOf();
    }
        
    public final ByteBuffer getDirectIntBuffer()
    {
        return getDirectIntBuffer(0, 1);
    }
    
    public final ByteBuffer getDirectIntBuffer(int offset, int capacity)
    {
        rangeCheck(offset * sizeOf() + capacity * sizeOf() - 1, this.capacity);
        ByteBuffer bb = ByteBuffer.allocateDirect(capacity * sizeOf()).order(ByteOrder.nativeOrder());
        try {
            addressField.setLong(bb, address(offset));
            capacityField.setInt(bb, capacity * sizeOf()); 
           
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(IntAddress.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public abstract void dispose();
    public abstract int sizeOf();
    public abstract void copyTo(M dest, long offset);
    public abstract void copyFrom(M src, long offset);
}
