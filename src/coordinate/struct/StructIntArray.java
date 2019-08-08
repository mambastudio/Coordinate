/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <T>
 */
public class StructIntArray <T extends IntStruct> 
{
    Class<T> clazz;
    int[] array;
    
    public StructIntArray(Class<T> clazz, int size)
    {
        this.clazz = clazz;
        T t = getInstance();
        array = new int[size * t.getSize()];
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
            Logger.getLogger(StructFloatArray.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int[] getArray()
    {
        return array;
    }
    
    public int size()
    {
        return array.length/getInstance().getSize();
    }
}
