/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.structint;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <T>
 */
public class StructIntArray <T extends IntStruct> implements Iterable<T>
{
    Class<T> clazz;
    int[] array;
    int size;
    
    public StructIntArray(Class<T> clazz, int size)
    {
        this.clazz = clazz;
        T t = getInstance();
        array = new int[size * t.getSize()];
        this.size = size;
    }
    
    public StructIntArray(Class<T> clazz, int[] array)
    {
        this.clazz = clazz;
        T t = getInstance();        
        if((array.length%t.getSize()) != 0)
            throw new UnsupportedOperationException("array length does not much with struct size");
        this.array = array;
        this.size = array.length/t.getSize();
    }
   
    public int size()
    {
        return size;
    }
    
    public void setIntArray(int... array)
    {
        if(this.array.length != array.length) return;
        this.array = array;
    }
    
    public T get(int index)
    {
        T t = getInstance();
        t.setGlobalArray(array, index);
        t.initFromGlobalArray();
        return t;
    }
    
    public void set(T t, int index)
    {
        t.setGlobalArray(array, index);
        System.arraycopy(t.getArray(), 0, array, t.getGlobalArrayIndex(), t.getSize());
    }
    
    private T getInstance()
    {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(StructIntArray.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int[] getArray()
    {
        return array;
    }
    
    public void initFromIndex(int index, T t)
    {
        t.setGlobalArray(array, index);
        t.initFromGlobalArray();
    }
    
    @Override
    public Iterator<T> iterator() {
        return new StructIntArrayIterator<>(this);
    }

    public int getArraySize() {
        return array.length;
    }

    
    private class StructIntArrayIterator<T extends IntStruct> implements Iterator<T>
    {
        int i = 0;
        StructIntArray structIntArray;
        T t = null;
        private StructIntArrayIterator(StructIntArray<T> array)
        {
            t = array.getInstance();
            this.structIntArray = array;
        }
        @Override
        public boolean hasNext() {
            return i < size;
        }

        @Override
        public T next() {
            structIntArray.initFromIndex(i, t);
            i++;
            return t;
        }        
    }    
}
