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

import coordinate.function.TriFunction;
import java.util.Arrays;
import java.util.function.BiFunction;

/**
 *
 * @author user
 */
public class FloatList {
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    private float[] array;
    private int size;

    public FloatList()
    {
        array = new float[10];
        size = 0;
    }
    
    public FloatList(int capacity)
    {
        if(capacity <= 0)
            array = new float[10];
        else
            array = new float[capacity];
        size = 0;
    }

    public void clear()
    {
        array = new float[10];
        size = 0;
    }
    
    public final void add(float i)
    {
        ensureCapacity(size + 1);
        array[size++] = i;
        
    }
    
    public final void add(float... value)
    {
        for(float i : value)
            add(i);
    }
    
    public final void insert(int index, float value)
    {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);  
        System.arraycopy(array, index, array, index + 1,
                         size - index);
        array[index] = value;
        size++;
    }
    
    public final void insert(int index, float... value)
    {
        for(int i = 0; i<value.length; i++)
            insert(index+i, value[i]);
    }
    
    //actual array size still remains same (efficiency purpose perhaps)
    public final float remove(int index)
    {
        rangeCheck(index);
        float oldValue = array[index];
        
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(array, index+1, array, index,
                             numMoved);
        size--;
        return oldValue;
    }
    
    public final float[] remove(int fromIndex, int toIndex)
    {        
        int removeSize = toIndex - fromIndex;
        float[] arr = new float[removeSize];
        
        for(int i = 0; i<removeSize; i++)
            arr[i] = remove(fromIndex);
        
        return arr;
    }

    public final void set(int index, float value)
    {
        array[index] = value;
    }
    
    public final void set(int index, float... values)
    {
        for(int i = 0; i<values.length; i++)
            set(index + i, values[i]);
    }

    public final float get(int index)
    {
        return array[index];
    }
   
    public final float[] subArray(int fromIndex, int toIndex)
    {
        subListRangeCheck(fromIndex, toIndex, size);        
        return Arrays.copyOfRange(array, fromIndex, toIndex);        
    }
    
    public final int size()
    {
        return size;
    }

    public final float[] trim()
    {
        if(size < array.length)
            array = Arrays.copyOf(array, size);        
        return array;
    }
    
    private void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }
    
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("index out of bound " +index);
    }
    
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException("index out of bound " +index);
    }
    
    private void ensureCapacity(int minCapacity)
    {
        if(minCapacity - array.length > 0)
            grow(minCapacity);
    }
    
    private void grow(int minCapacity)
    {
        int oldCapacity = array.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        array = Arrays.copyOf(array, newCapacity);
    }
    
    private int hugeCapacity(int minCapacity) 
    {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
    
    //(oldValue, index, size) return value;
    public void forEachSet(TriFunction<Float, Integer, Integer, Float> function)
    {
        for(int i = 0; i<size(); i++)
            set(i, function.apply(get(i), i, size()));
    }
    
    //(index, size) return value;
    public static FloatList forEachAdd(int size, BiFunction<Integer, Integer, Float> function)
    {
        FloatList list = new FloatList();
        for(int i = 0; i<size; i++)
            list.add(function.apply(i, size));
        return list;
    }
    
    @Override
    public String toString()
    {
        return Arrays.toString(trim());
    }
}
