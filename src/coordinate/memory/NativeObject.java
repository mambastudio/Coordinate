/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory;

import coordinate.memory.NativeObject.Element;
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
public class NativeObject<T extends Element> extends MemoryAddress<NativeObject<T>, byte[]>{
    private T t;
    private final Class<T> clazz;
    
    public NativeObject(Class<T> clazz, long capacity)
    {
        super(capacity);
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
        System.out.println("native array garbage collected");
        if(address()!=0)
            getUnsafe().freeMemory(address());
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
        return clazz;
    }
    
    public T get(long offset)
    {
        rangeCheck(offset, capacity());
        T t = newInstance();
        byte[] b = new byte[sizeOf()];
        this.copyFrom(b, offset);
        t.putBytes(b);
        return t;
    }
    
    public void set(long index, T t)
    {
        
    }
    
    private T newInstance()
    {
        if(t == null)
        {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(NativeObject.class.getName()).log(Level.SEVERE, null, ex);
            }
            throw new UnsupportedOperationException("unable to create new instance");
        }
        else
            return (T) t.newInstance();
    }
    
    public static interface Element<E extends Element<E>>
    {
        public int sizeOf();
        public byte[] getBytes();
        public void putBytes(byte[] bytes);
        public E newInstance();
    }
}
