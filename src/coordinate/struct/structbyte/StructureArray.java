/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.structbyte;

import coordinate.list.ByteList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <T>
 */
public class StructureArray<T extends Structure> implements Iterable<T> {
    
    Class<T> clazz;
    ByteList array;
    int size;
    
    Structure struct;
    
    public StructureArray(Class<T> clazz)
    {
        this.clazz = clazz;
        this.struct = getObject(clazz);
        this.array = new ByteList();
        this.size = 0;
    }
    
    public StructureArray(Class<T> clazz, int size)
    {       
        this.clazz = clazz;
        this.struct = getObject(clazz);        
        this.array = new ByteList(struct.getByteSize() * size);
        this.size = size;        
    }
    
    public StructureArray(Class<T> clazz, byte[] array)
    {        
        this.clazz = clazz;
        this.struct = getObject(clazz);
        this.array = new ByteList(array);
        this.size = array.length/struct.getByteSize();
    }
    
    public StructureArray(Class<T> clazz, ByteList array)
    {        
        this.clazz = clazz;
        this.struct = getObject(clazz);
        this.array = array;
        this.size = array.size()/struct.getByteSize();
    }
    
    public void add(T t)
    {
        //current index is now size of struct array
        int currentIndex = size;
        array.add(t.getFieldValuesAsArray());
        
        //increment size
        size++;
        
        //just in case you operate the object outside here
        t.setGlobalArray(array.trim(), currentIndex);
      
    }
    
    public int size()
    {
        return size;
    }
    
    public int getByteArraySize()
    {
        return array.size();
    }
    
    public byte[] getArray()
    {
        return array.trim();
    }
    
    public T get(int index)
    {
        T t = getObject(clazz);               
        t.setGlobalArray(array.trim(), index);
        t.setFieldValuesFromGlobalArray();
        return t;
    }
    
    public void initFrom(T t, int index)
    {        
        t.setGlobalArray(array.trim(), index);
        t.setFieldValuesFromGlobalArray();
    }
    
    public void set(T t, int index)
    {
        t.setGlobalArray(array.trim(), index);
        System.arraycopy(t.getFieldValuesAsArray(), 0, array.trim(), t.getGlobalArrayIndex(), t.getByteSize());
    }

    @Override
    public Iterator<T> iterator() {
        return new StructFloatArrayIterator(this);
    }
    
    private class StructFloatArrayIterator implements Iterator<T>
    {
        int i = 0;
        StructureArray structArray;
        T t;
        private StructFloatArrayIterator(StructureArray<T> array)
        {
            this.t = array.getObject(clazz);
            this.structArray = array;
        }
        @Override
        public boolean hasNext() {
            return i < size;
        }

        @Override
        public T next() {            
            structArray.initFrom(t, i); i++;
            return t;
        }        
    }    
    
    private T getObject(Class<T> clazz)
    {
        try {
            T t = clazz.newInstance();
            return t;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(StructureArray.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
