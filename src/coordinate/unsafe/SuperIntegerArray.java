/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.unsafe;

import static coordinate.unsafe.UnsafeUtils.INTSIZE;
import static coordinate.unsafe.UnsafeUtils.copyMemory;
import static coordinate.unsafe.UnsafeUtils.getIntCapacity;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import coordinate.utility.Sweeper;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public abstract class SuperIntegerArray {
    
    private final long address;
    private final long size;    
    
    //buffer fields
    static final Field addressField, capacityField;
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
    
    public SuperIntegerArray(long size)
    {
        this.size = size;
        address = getUnsafe().allocateMemory(getIntCapacity(size));
        
        initSweeper();
    }
    
    private void initSweeper()
    {
        //For garbage collection
        Sweeper.getSweeper().register(this, ()->dispose());
    }
        
    public final long getAddress()
    {
        return address;
    }
    
    public final long getAddress(int offset)
    {
        rangeCheck(offset, size);
        return address + offset * 4;
    }
    
    public final long getSize()
    {
        return size;
    }
    
    public final IntBuffer getDirectIntBuffer()
    {
        return getDirectIntBuffer(0, 0);
    }
    
    public final IntBuffer getDirectIntBuffer(int offset, int size)
    {
        if(!(offset == 0 && size == 0)) //if it's getDirectIntBuffer(0, 0);
            rangeCheck(offset + size - 1, this.size);
        ByteBuffer bb = ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder());
        try {
            addressField.setLong(bb, getAddress(offset));
            capacityField.setInt(bb, size * 4); System.out.println("asdfas");
           
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(SuperIntegerArray.class.getName()).log(Level.SEVERE, null, ex);
        }           
        System.out.println(bb);
        return bb.asIntBuffer();
    }
    
    public void dispose()
    {
        System.out.println("native array garbage collected");
        getUnsafe().freeMemory(address);
    }
    
    public void set(long offset, int value)
    {
        rangeCheck(offset, size);
        getUnsafe().putInt(address + getIntCapacity(offset), value);
    }
    
    public int get(long offset)
    {
        rangeCheck(offset, size);
        return getUnsafe().getInt(address + getIntCapacity(offset));
    }
    
    //for why we use 16, refer to https://mail.openjdk.org/pipermail/panama-dev/2021-November/015852.html 
    public void copyFrom(int[] array, long offset)
    {
        rangeCheck(offset + array.length-1, size); //check array copy within bounds for this native array
        copyMemory(array, 16, null, address, array.length * INTSIZE);
    }
    
    public void copyTo(int[] array, long offset)
    {
        rangeCheck(offset + array.length-1, size); //check array copy within bounds for this native array
        copyMemory(null, address, array, 16, array.length * INTSIZE);
    }
    
    //Size should encompass the fromIndex and toIndex 
    protected final void rangeCheck(long offset, long size) {
        if (offset < 0)
            throw new IndexOutOfBoundsException("offset is less than zero " + offset);
        if (offset >= size)
            throw new IndexOutOfBoundsException("offset is beyond range, offset = " + offset + " size = " +size);        
    }    
}
