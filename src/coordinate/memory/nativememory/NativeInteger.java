/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.nativememory;

import static coordinate.unsafe.UnsafeUtils.copyMemory;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import coordinate.utility.RangeCheck;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntUnaryOperator;
import java.util.function.LongToIntFunction;

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
        copyToArr(arr, start, end - start);
        return Arrays.toString(arr);
    }
    
    public <E extends IntElement<E>> String getString(long start, long end, E e)
    {
        long cap = end - start;
        rangeCheckBound(start, end, arrayCapacityLimitString);
        StringBuilder builder = new StringBuilder();
        for(long i = start; i<start + cap; i++)
        {
            E ee = e.newInstance();
            ee.setInt(get(i));            
            builder.append(ee).append(", ");
        }
        return builder.substring(0, builder.length()-2);
    }
    
    public NativeInteger fill(int val) {
        for (long i = 0, len = capacity(); i < len; i++)
            set(i, val);
        return this;
    }
    
    public NativeInteger fill(int val, long n)
    {
        RangeCheck.rangeCheckBound(0, n, capacity());
        for (long i = 0, len = n; i < len; i++)
            set(i, val);
        return this;
    }
    
    public NativeInteger iterateRange(LongToIntFunction operator)
    {
        for (long i = 0, len = capacity(); i < len; i++)
            set(i, operator.applyAsInt(i));
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
            return NativeInteger.this.getString(0, arrayCapacityLimitString);
        else
            return NativeInteger.this.getString(0, capacity());
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
            freeMemory();
            this.address = n.address;
            this.capacityBytes = n.capacityBytes;
        }        
        else
        {
            NativeInteger n = new NativeInteger(capacity);
            copyMemory(null, address(), null, n.address(), capacityBytes);
            freeMemory();
            this.address = n.address;
            this.capacityBytes = n.capacityBytes;
        }
    }
    
    public <E extends IntElement<E>> E get(long index, E e)
    {
        e.setInt(get(index));
        return e;
    }
    
    public <E extends IntElement<E>> void set(long index, E e)
    {
        this.set(index, e.getInt());
    }
    
    public static interface IntElement<E extends IntElement<E>>
    {
        public int getInt();
        public void setInt(int value);
        public E newInstance();
    }
}
