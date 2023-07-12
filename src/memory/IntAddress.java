/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memory;

import static coordinate.unsafe.UnsafeUtils.copyMemory;
import static coordinate.unsafe.UnsafeUtils.getIntCapacity;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmburu
 */
public class IntAddress extends MemoryAddress{

    public IntAddress(MemoryAddress pointer) {
        super(pointer);
    }

    public IntAddress(long size)
    {
        super(size);
    }
    
    public final long getAddress()
    {
        return address;
    }
    
    public final long getAddress(int offset)
    {
        rangeCheck(offset, capacity);
        return address + offset * 4;
    }
    
    public final long getSize()
    {
        return capacity;
    }
    
    public final IntBuffer getDirectIntBuffer()
    {
        return getDirectIntBuffer(0, 0);
    }
    
    public final IntBuffer getDirectIntBuffer(int offset, int size)
    {
        if(!(offset == 0 && size == 0)) //if it's getDirectIntBuffer(0, 0);
            rangeCheck(offset + size - 1, this.capacity);
        ByteBuffer bb = ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder());
        try {
            addressField.setLong(bb, getAddress(offset));
            capacityField.setInt(bb, size * 4); System.out.println("asdfas");
           
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(IntAddress.class.getName()).log(Level.SEVERE, null, ex);
        }           
        System.out.println(bb);
        return bb.asIntBuffer();
    }
    
    @Override
    public void dispose()
    {
        System.out.println("native array garbage collected");
        getUnsafe().freeMemory(address);
    }
    
    public void set(long offset, int value)
    {
        rangeCheck(offset, capacity);
        getUnsafe().putInt(address + getIntCapacity(offset), value);
    }
    
    public int get(long offset)
    {
        rangeCheck(offset, capacity);
        return getUnsafe().getInt(address + getIntCapacity(offset));
    }
    
    //for why we use 16, refer to https://mail.openjdk.org/pipermail/panama-dev/2021-November/015852.html 
    public void copyFrom(int[] array, long offset)
    {
        rangeCheck(offset + array.length-1, capacity); //check array copy within bounds for this native array
        copyMemory(array, 16, null, address, array.length * INTSIZE);
    }
    
    public void copyTo(int[] array, long offset)
    {
        rangeCheck(offset + array.length-1, capacity); //check array copy within bounds for this native array
        copyMemory(null, address, array, 16, array.length * INTSIZE);
    }
    
    //Size should encompass the fromIndex and toIndex 
    protected final void rangeCheck(long offset, long size) {
        if (offset < 0)
            throw new IndexOutOfBoundsException("offset is less than zero " + offset);
        if (offset >= size)
            throw new IndexOutOfBoundsException("offset is beyond range, offset = " + offset + " size = " +size);        
    }    

    @Override
    public int sizeOf() {
        return 4;
    }
}
