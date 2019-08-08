/*
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package coordinate.list;

import java.util.logging.Level;
import java.util.logging.Logger;
import coordinate.generic.AbstractCoordinate;


/**
 *
 * @author user
 * @param <T>
 */
public class CoordinateList <T extends AbstractCoordinate>
{
    private final FloatList arrayList;
    private final Class<T> instance;   
    private final int coordinateSize;
        
    public CoordinateList(Class<T> clazz)
    {
        arrayList = new FloatList();
        instance = clazz;
        coordinateSize = getCoordinateInstance().getSize();
    }
    
    public CoordinateList(Class<T> clazz, int capacity)
    {
        arrayList = new FloatList(capacity);
        instance = clazz;
        coordinateSize = getCoordinateInstance().getSize();
    }
    
    public void add(CoordinateList<T> list)
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
            Logger.getLogger(CoordinateList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private int getArrayIndex(int index)
    {
        return index * coordinateSize;
    }
    
    public int size()
    {
        return arrayList.size()/getCoordinateInstance().getSize();
    }
    
    public int arraySize()
    {
        return arrayList.size();
    }
    
    public float[] getFloatArray()
    {
        return arrayList.trim();
    }    
}
