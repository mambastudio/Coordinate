/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory;

import coordinate.memory.NativeObject.Element;
import static coordinate.unsafe.UnsafeUtils.copyMemory;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmburu
 * @param <T>
 */
public class NativeObject<T extends Element<T>> extends MemoryAddress<NativeObject<T>, byte[]>{
    private T t;
    
    public NativeObject(Class<T> clazz, long capacity)
    {
        super(clazz, capacity);
        this.clazz = clazz;
    }
    
    public NativeObject(NativeObject<T> pointer, long offset) {
        super(pointer, offset);
        clazz = pointer.getClazz();
    }

    @Override
    protected NativeObject<T> copyStateSize() {
        return new NativeObject(clazz, capacity());
    }

    @Override
    public void dispose() {        
        if(address()!=0)
        {
            getUnsafe().freeMemory(address());
            address = 0;
        }
    }

    @Override
    public int sizeOf() {
        if(t == null)
            t = newInstance();
        return t.sizeOf();
    }

    @Override
    public NativeObject<T> offsetMemory(long offset) {
        rangeCheck(offset, capacity());
        return new NativeObject(this, offset);
    }
    
    public NativeObject<T> fill(T t) {
        for (long i = 0, len = capacity(); i < len; i++)
            set(i, t.copy());
        return this;
    }

    @Override
    public String getString(long start, long end) {
        long cap = end - start;
        rangeCheckBound(start, end, arrayCapacityLimitString);
        // Use Array native method to create array
        // of a type only known at run time
        // https://stackoverflow.com/questions/529085/how-to-create-a-generic-array-in-java
        @SuppressWarnings("unchecked")
        final T[] arr = (T[]) Array.newInstance(get(0).getClass(), (int) cap);
        for(int i = 0; i<arr.length; i++)
            arr[i] = get(i);
        return Arrays.toString(arr);
    }
    
    public Class<T> getClazz()
    {
        return (Class<T>) clazz;
    }
    
    public T get(long offset)
    {
        rangeCheck(offset, capacity());
        T element = newInstance();
        byte[] b = new byte[sizeOf()];
        this.copyToArr(b, offset, 1);
        element.putBytes(b);
        return element;
    }
    
    public void set(long offset, T t)
    {
        rangeCheck(offset, capacity());
        byte[] b = t.getBytes();
        this.copyFromArr(b, offset, 1);
    }
    
    private T newInstance()
    {
        if(t == null)
        {
            try {                
                t = (T) clazz.newInstance();
                return t.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(NativeObject.class.getName()).log(Level.SEVERE, null, ex);
            }
            throw new UnsupportedOperationException("unable to create new instance");
        }
        else
            return t.newInstance();
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
    public void resize(long capacity) {
        rangeAboveZero(capacity);               
        if(capacity < capacity())     
        {
            NativeObject<T> n = new NativeObject(clazz, capacity);
            copyMemory(null, address(), null, n.address(), toAmountBytes(capacity));
            dispose();
            this.address = n.address;
            this.capacityBytes = n.capacityBytes;
        }        
        else
        {
            NativeObject<T> n = new NativeObject(clazz, capacity).fill(newInstance());
            copyMemory(null, address(), null, n.address(), capacityBytes);
            dispose();
            this.address = n.address;
            this.capacityBytes = n.capacityBytes;
        }  
    }
    
    public static interface Element<E extends Element<E>>
    {
        public int sizeOf();
        public byte[] getBytes();
        public void putBytes(byte[] bytes);
        public E newInstance();
        public E copy();
    }
}
