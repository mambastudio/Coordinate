/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct.newstruct;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <T>
 */
public class StructArrayBuffer<T extends StructByte> implements Iterable<T> {
    
    Class<T> clazz;
    ByteBuffer array;    
    StructByte struct;
        
    public StructArrayBuffer(Class<T> clazz, int size)
    {       
        this.clazz = clazz;
        this.struct = getObject(clazz);        
        this.array = ByteBuffer.allocateDirect(struct.getByteSize() * size);       
    }
        
        
    public int size()
    {
        return getByteArraySize()/struct.getByteSize();
    }
    
    public int getByteArraySize()
    {
        return array.capacity();
    }
        
    public T get(int index)
    {
        
        T t = getObject(clazz);               
        t.setGlobalBuffer(array, index); 
        t.setFieldValuesFromGlobalArray();
        return t;
    }
    
    public void initFrom(T t, int index)
    {        
        t.setGlobalBuffer(array, index);
        t.setFieldValuesFromGlobalArray();
    }
    
    public void set(T t, int index)
    {
        t.setGlobalBuffer(array, index);
        array.position(t.getGlobalArrayIndex());
        array.put(t.getFieldValuesAsArray());
        
    }

    @Override
    public Iterator<T> iterator() {
        return new StructByteArrayIterator(this);
    }
    
    private class StructByteArrayIterator implements Iterator<T>
    {
        int i = 0;
        StructArrayBuffer structArray;
        T t;
        private StructByteArrayIterator(StructArrayBuffer<T> array)
        {
            this.t = array.getObject(clazz);
            this.structArray = array;
        }
        @Override
        public boolean hasNext() {
            return i < size();
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
            Logger.getLogger(StructArrayBuffer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
