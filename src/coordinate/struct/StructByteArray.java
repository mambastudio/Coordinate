/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import coordinate.list.ByteList;
import coordinate.utility.StructInfo;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <T>
 */
public class StructByteArray<T extends ByteStruct> implements Iterable<T> {
    Class<T> clazz;
    ByteList array;
    int size;
    int[] offsets;
    
    public StructByteArray(Class<T> clazz, int size)
    {
        StructInfo info = new StructInfo(clazz);
        
        this.clazz = clazz;
        this.offsets = info.offsets();
        this.array = new ByteList(offsets[offsets.length - 1] * size);
        this.size = size;        
    }
    
    public StructByteArray(Class<T> clazz, byte[] array)
    {
        StructInfo info = new StructInfo(clazz);
        
        this.clazz = clazz;
        this.offsets = info.offsets();
        this.array = new ByteList(array);
        this.size = array.length/offsets[offsets.length - 1];
    }
    public StructByteArray(Class<T> clazz, ByteList array)
    {
         StructInfo info = new StructInfo(clazz);
        
        this.clazz = clazz;
        this.offsets = info.offsets();
        this.array = array;
        this.size = array.size()/offsets[offsets.length - 1];
    }
    
    public int size()
    {
        return size;
    }
    
    public int getByteArraySize()
    {
        return array.size();
    }
    
    public T get(int index)
    {
        T t = getInstance();
        t.setOffsets(offsets);
        t.setGlobalArray(array.trim(), index);
        t.initFromGlobalArray();          
        return t;
    }
    
    public void set(T t, int index)
    {
        t.setGlobalArray(array.trim(), index);
        System.arraycopy(t.getArray(), 0, array.trim(), t.getGlobalArrayIndex(), t.getSize());
    }
    
    private T getInstance()
    {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(StructFloatArray.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public byte[] getArray()
    {
        return array.trim();
    }

    @Override
    public Iterator<T> iterator() {
        return new StructFloatArrayIterator(this);
    }
    
    private class StructFloatArrayIterator<T extends ByteStruct> implements Iterator<T>
    {
        int i = 0;
        StructByteArray array;
        private StructFloatArrayIterator(StructByteArray array)
        {
            this.array = array;
        }
        @Override
        public boolean hasNext() {
            return i < size;
        }

        @Override
        public T next() {            
            T t = (T) array.get(i); i++;
            t.setOffsets(offsets);
            return t;
        }        
    }    
}
