/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.list;

import coordinate.function.TriFunction;
import java.util.Arrays;
import java.util.function.BiFunction;

/**
 *
 * @author user
 */
public class ByteList {
     private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    private byte[] array;
    private int size;

    public ByteList()
    {
        array = new byte[10];
        size = 0;
    }
    
    public ByteList(int capacity)
    {
        array = new byte[capacity];
        size = capacity;
    }
    
    public ByteList(byte[] array)
    {
        this.array = array;
        this.size = array.length;
    }

    public void clear()
    {
        array = new byte[10];
        size = 0;
    }
    
    public final void add(byte i)
    {
        ensureCapacity(size + 1);
        array[size++] = i;
        
    }
    
    public final void add(byte... value)
    {
        for(byte i : value)
            add(i);
    }
    
    public final void insert(int index, byte value)
    {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);  
        System.arraycopy(array, index, array, index + 1,
                         size - index);
        array[index] = value;
        size++;
    }
    
    public final void insert(int index, byte... value)
    {
        for(int i = 0; i<value.length; i++)
            insert(index+i, value[i]);
    }
    
    //actual array size still remains same (efficiency purpose perhaps)
    public final byte remove(int index)
    {
        rangeCheck(index);
        byte oldValue = array[index];
        
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(array, index+1, array, index,
                             numMoved);
        size--;
        return oldValue;
    }
    
    public final byte[] remove(int fromIndex, int toIndex)
    {        
        int removeSize = toIndex - fromIndex;
        byte[] arr = new byte[removeSize];
        
        for(int i = 0; i<removeSize; i++)
            arr[i] = remove(fromIndex);
        
        return arr;
    }

    public final void set(int index, byte value)
    {
        array[index] = value;
    }
    
    public final void set(int index, byte... values)
    {
        for(int i = 0; i<values.length; i++)
            set(index + i, values[i]);
    }

    public final byte get(int index)
    {
        return array[index];
    }
   
    public final byte[] subArray(int fromIndex, int toIndex)
    {
        subListRangeCheck(fromIndex, toIndex, size);        
        return Arrays.copyOfRange(array, fromIndex, toIndex);        
    }
    
    public final int size()
    {
        return size;
    }

    public final byte[] trim()
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
    public void forEachSet(TriFunction<Byte, Integer, Integer, Byte> function)
    {
        for(int i = 0; i<size(); i++)
            set(i, function.apply(get(i), i, size()));
    }
    
    //(index, size) return value;
    public static ByteList forEachAdd(int size, BiFunction<Integer, Integer, Byte> function)
    {
        ByteList list = new ByteList();
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
