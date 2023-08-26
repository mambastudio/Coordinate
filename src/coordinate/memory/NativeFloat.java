/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory;

import static coordinate.unsafe.UnsafeUtils.copyMemory;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import coordinate.utility.RangeCheck;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author jmburu
 */
public class NativeFloat extends MemoryAddress<NativeFloat, float[]>{

    public NativeFloat(NativeFloat pointer) {
        super(pointer);
    }

    public NativeFloat(long capacity)
    {
        super(capacity);
    }

    public NativeFloat(NativeFloat pointer, long offset) {
        super(pointer, offset);
    }
    
    public NativeFloat(NativeFloat pointer, long offset, long capacity) {
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
    
    public void set(long offset, float value)
    {
        rangeCheck(offset, capacity());
        getUnsafe().putFloat(address() + toAmountBytes(offset), value);
    }
    
    public float get(long offset)
    {
        rangeCheck(offset, capacity());
        return getUnsafe().getFloat(address() + toAmountBytes(offset));
    }
    
    
    public void swapElement(long index1, long index2)
    {
        rangeCheck(index1, capacity());
        rangeCheck(index2, capacity());       
        float temp = get(index1);
        set(index1, get(index2));
        set(index2, temp);
    }
    
    @Override
    public int sizeOf() {
        return 4;
    }
    
    public float getLast()
    {
        return get(capacity()-1);
    }

    @Override
    public NativeFloat offsetMemory(long offset) {
        rangeCheck(offset, capacity());
        return new NativeFloat(this, offset);
    }

    @Override
    public String getString(long start, long end) {
        long cap = end - start;
        rangeCheckBound(start, end, arrayCapacityLimitString);
        float[] arr = new float[(int)cap];
        copyToArr(arr, start, end - start);
        return Arrays.toString(arr);
    }
    
    public <E extends FloatElement<E>> String getString(long start, long end, E e)
    {
        long cap = end - start;
        rangeCheckBound(start, end, arrayCapacityLimitString);
        StringBuilder builder = new StringBuilder();
        for(long i = start; i<start + cap; i++)
        {
            E ee = e.newInstance();
            ee.setFloat(get(i));            
            builder.append(ee).append(", ");
        }
        return builder.substring(0, builder.length()-2);
    }
    
    public NativeFloat fill(float val) {
        for (long i = 0, len = capacity(); i < len; i++)
            set(i, val);
        return this;
    }
    
    public NativeFloat fill(float val, long n)
    {
        RangeCheck.rangeCheckBound(0, n, capacity());
        for (long i = 0, len = n; i < len; i++)
            set(i, val);
        return this;
    }
    
    public NativeFloat fillRandomRange(float min, float max)
    {
        for (long i = 0, len = capacity(); i < len; i++)
            set(i, (float) ThreadLocalRandom.current().nextDouble(min, max + 1)); 
        return this;
    }
    
    @Override
    public String toString()
    {
        if(capacity() > arrayCapacityLimitString)
            return NativeFloat.this.getString(0, arrayCapacityLimitString);
        else
            return NativeFloat.this.getString(0, capacity());
    }

    @Override
    protected final NativeFloat copyStateSize() {
        return new NativeFloat(capacity());
    }

    @Override
    public void resize(long capacity) {
        rangeAboveZero(capacity);               
        if(capacity < capacity())     
        {
            NativeFloat n = new NativeFloat(capacity);
            copyMemory(null, address(), null, n.address(), toAmountBytes(capacity));
            freeMemory();
            this.address = n.address;
            this.capacityBytes = n.capacityBytes;
        }        
        else
        {
            NativeFloat n = new NativeFloat(capacity);
            copyMemory(null, address(), null, n.address(), capacityBytes);
            freeMemory();
            this.address = n.address;
            this.capacityBytes = n.capacityBytes;
        }
    }
    
    public <E extends FloatElement<E>> E get(long index, E e)
    {
        e.setFloat(get(index));
        return e;
    }
    
    public <E extends FloatElement<E>> void set(long index, E e)
    {
        this.set(index, e.getFloat());
    }
    
    public static interface FloatElement<E extends FloatElement<E>>
    {
        public float getFloat();
        public void setFloat(float value);
        public E newInstance();
    }
}
