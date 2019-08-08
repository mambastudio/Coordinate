/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.list;

import java.util.Arrays;

/**
 *
 * @author user
 */
public class CharList {
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private int BUFSIZE = 30;
    
    private char[] array;
    private int size;

    public CharList()
    {
        array = new char[BUFSIZE];
        size = 0;
    }
    
    public CharList(int capacity)
    {
        this.BUFSIZE = capacity;
        array = new char[capacity];        
        size = 0;
    }   
    
    public void setArrayDirectly(char... array)
    {
        this.array = array;
        size = this.array.length;
    }

    public void clear()
    {
        array = new char[BUFSIZE];
        size = 0;
    }
    
    public final void add(char i)
    {
        ensureCapacity(size + 1);
        array[size++] = i;
        
    }
    
    public final void add(char... value)
    {
        for(char i : value)
            add(i);
    }
    
    public final void insert(int index, char value)
    {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);  
        System.arraycopy(array, index, array, index + 1,
                         size - index);
        array[index] = value;
        size++;
    }
    
    public final void insert(int index, char... value)
    {
        for(int i = 0; i<value.length; i++)
            insert(index+i, value[i]);
    }
    
    public final char remove(int index)
    {
        rangeCheck(index);
        char oldValue = array[index];
        
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(array, index+1, array, index,
                             numMoved);
        size--;
        return oldValue;
    }

    public final char[] remove(int fromIndex, int toIndex)
    {        
        int removeSize = toIndex - fromIndex;
        char[] arr = new char[removeSize];
        
        for(int i = 0; i<removeSize; i++)
            arr[i] = remove(fromIndex);
        
        return arr;
    }
    
    public final void set(int index, char value)
    {
        array[index] = value;
    }

    public final char get(int index)
    {
        return array[index];
    }

    public final int size()
    {
        return size;
    }

    public final char[] trim()
    {
        if(size < array.length)
            array = Arrays.copyOf(array, size);        
        return array;
    }
    
    public final char[] subArray(int fromIndex, int toIndex)
    {
        subListRangeCheck(fromIndex, toIndex, size);        
        return Arrays.copyOfRange(array, fromIndex, toIndex);        
    }
    
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("index out of bound " +index);
    }
    
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException("index out of bound " +index);
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
    
    @Override
    public String toString()
    {
        return Arrays.toString(trim());
    }
}
