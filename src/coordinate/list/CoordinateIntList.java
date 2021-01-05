/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.list;

import coordinate.generic.AbstractCoordinateInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <T>
 */
public class CoordinateIntList <T extends AbstractCoordinateInteger> 
{
    private final IntList arrayList;
    private final Class<T> instance;   
    private final int coordinateSize;
        
    public CoordinateIntList(Class<T> clazz)
    {
        arrayList = new IntList();
        instance = clazz;
        coordinateSize = getCoordinateInstance().getSize();
    }
    
    public CoordinateIntList(Class<T> clazz, int capacity)
    {
        arrayList = new IntList(capacity);
        instance = clazz;
        coordinateSize = getCoordinateInstance().getSize();
    }
    
    public void add(CoordinateIntList<T> list)
    {
        for(int i = 0; i<list.size(); i++)
            add(list.get(i));
    }
    
    public void add(T t)
    {
        arrayList.add(t.getArray());
    }
    
    public T get(int index)
    {        
        T t = getCoordinateInstance();
        t.set(arrayList.subArray(getArrayIndex(index), getArrayIndex(index) + coordinateSize));
        return t;
    }
    
    public void set(int index, T t)
    {
        arrayList.set(getArrayIndex(index), t.getArray());
    }
        
    private T getCoordinateInstance()
    {
        try {
            return instance.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(CoordinateIntList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private int getArrayIndex(int index)
    {
        return index * coordinateSize;
    }
    
    public int size()
    {
        return arrayList.size()/coordinateSize;
    }
    
    public int arraySize()
    {
        return arrayList.size();
    }
    
    public int[] getFloatArray()
    {
        return arrayList.trim();
    }    
}
