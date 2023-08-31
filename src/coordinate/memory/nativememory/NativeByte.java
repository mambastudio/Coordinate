/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.nativememory;

import static coordinate.unsafe.UnsafeUtils.copyMemory;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import java.util.Arrays;

/**
 *
 * @author jmburu
 */
public class NativeByte extends MemoryAddress<NativeByte, byte[]> {

    public NativeByte(NativeByte pointer) {
        super(pointer);
    }

    public NativeByte(long capacity)
    {
        super(capacity);
    }

    public NativeByte(NativeByte pointer, long offset) {
        super(pointer, offset);
    }
    
    public NativeByte(NativeByte pointer, long offset, long capacity) {
        super(pointer, offset, capacity);
    }
     
    @Override
    public void freeMemory()
    {                
        if(address()!=0)
        {
            getUnsafe().freeMemory(address());
            address = 0;
        }
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
        return 1;
    }
    
    public int getLast()
    {
        return get(capacity()-1);
    }

    @Override
    public NativeByte offsetMemory(long offset) {
        rangeCheck(offset, capacity());
        return new NativeByte(this, offset);
    }

    @Override
    public String getString(long start, long end) {
        long cap = end - start;
        rangeCheckBound(start, end, arrayCapacityLimitString);
        byte[] arr = new byte[(int)cap];
        copyToArr(arr, start, end - start);
        return Arrays.toString(arr);
    }
       
    @Override
    public String toString()
    {
        if(capacity() > arrayCapacityLimitString)
            return NativeByte.this.getString(0, arrayCapacityLimitString);
        else
            return NativeByte.this.getString(0, capacity());
    }

    @Override
    protected final NativeByte copyStateSize() {
        return new NativeByte(capacity());
    }

    @Override
    public void resize(long capacity) {
        rangeAboveZero(capacity);               
        if(capacity < capacity())     
        {
            NativeByte n = new NativeByte(capacity);
            copyMemory(null, address(), null, n.address(), toAmountBytes(capacity));
            freeMemory();
            this.address = n.address;
            this.capacityBytes = n.capacityBytes;
        }        
        else
        {
            NativeByte n = new NativeByte(capacity);
            copyMemory(null, address(), null, n.address(), capacityBytes);
            freeMemory();
            this.address = n.address;
            this.capacityBytes = n.capacityBytes;
        }
    }    
}
