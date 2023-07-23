/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory;

import static coordinate.unsafe.UnsafeUtils.copyMemory;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author jmburu
 */
public class NativeInteger extends MemoryAddress<NativeInteger, int[]>{

    public NativeInteger(NativeInteger pointer) {
        super(pointer);
    }

    public NativeInteger(long capacity)
    {
        super(capacity);
    }

    public NativeInteger(NativeInteger pointer, long offset) {
        super(pointer, offset);
    }
    
    public NativeInteger(NativeInteger pointer, long offset, long capacity) {
        super(pointer, offset, capacity);
    }
     
    @Override
    public void dispose()
    {        
        if(address()!=0)
            getUnsafe().freeMemory(address());
    }
    
    public void set(long offset, int value)
    {
        rangeCheck(offset, capacity());
        getUnsafe().putInt(address() + toAmountBytes(offset), value);
    }
    
    public int get(long offset)
    {
        rangeCheck(offset, capacity());
        return getUnsafe().getInt(address() + toAmountBytes(offset));
    }
    
    
    public void swapElement(long index1, long index2)
    {
        rangeCheck(index1, capacity());
        rangeCheck(index2, capacity());       
        int temp = get(index1);
        set(index1, get(index2));
        set(index2, temp);
    }
    
    @Override
    public int sizeOf() {
        return 4;
    }
    
    public int getLast()
    {
        return get(capacity()-1);
    }

    @Override
    public NativeInteger offsetMemory(long offset) {
        rangeCheck(offset, capacity());
        return new NativeInteger(this, offset);
    }

    @Override
    public String getString(long start, long end) {
        long cap = end - start;
        rangeCheckBound(start, end, arrayCapacityLimitString);
        int[] arr = new int[(int)cap];
        copyTo(arr, start);
        return Arrays.toString(arr);
    }
    
    public NativeInteger fill(int val) {
        for (long i = 0, len = capacity(); i < len; i++)
            set(i, val);
        return this;
    }
    
    public NativeInteger fillRandomRange(int min, int max)
    {
        for (long i = 0, len = capacity(); i < len; i++)
            set(i, ThreadLocalRandom.current().nextInt(min, max + 1));
        return this;
    }
    
    @Override
    public String toString()
    {
        if(capacity() > arrayCapacityLimitString)
            return getString(0, arrayCapacityLimitString);
        else
            return getString(0, capacity());
    }

    @Override
    protected final NativeInteger copyStateSize() {
        return new NativeInteger(capacity());
    }

    @Override
    public void resize(long capacity) {
        rangeAboveZero(capacity);               
        if(capacity < capacity())     
        {
            NativeInteger n = new NativeInteger(capacity);
            copyMemory(null, address(), null, n.address(), toAmountBytes(capacity));
            dispose();
            this.address = n.address;
            this.capacityBytes = n.capacityBytes;
        }        
        else
        {
            NativeInteger n = new NativeInteger(capacity).fill(0);
            copyMemory(null, address(), null, n.address(), capacityBytes);
            dispose();
            this.address = n.address;
            this.capacityBytes = n.capacityBytes;
        }
    }
}
