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

/**
 *
 * @author user
 */
public abstract class SuperIntegerArray {
    
    private final long size;
    private final long address;
    
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
        return address + offset;
    }
    
    public final long getSize()
    {
        return size;
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
