/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.nativememory;

import static coordinate.unsafe.UnsafeUtils.copyMemory;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import coordinate.utility.RangeCheck;
import coordinate.utility.Sweeper;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <M>
 * @param <A>
 */
public abstract class MemoryAddress<M extends MemoryAddress<M, A>, A> {
    
    protected long address;
    protected long capacityBytes; 
    
    protected int arrayCapacityLimitString = 1000;
    
    protected Class<?> clazz = null;
        
    //buffer fields
    protected static final Field addressField, capacityField;
    static {
    try {
            addressField = Buffer.class.getDeclaredField("address");
            addressField.setAccessible(true);
            capacityField = Buffer.class.getDeclaredField("capacity");
            capacityField.setAccessible(true);

        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }
    
    protected MemoryAddress(long capacity)
    {
        if(capacity < 0)
            throw new UnsupportedOperationException("capacity is less than zero");
        this.address = getUnsafe().allocateMemory(toAmountBytes(capacity));         
        this.capacityBytes = toAmountBytes(capacity);
        getUnsafe().setMemory(address, capacityBytes, (byte)0);
        initSweeper();
    }
    
    protected MemoryAddress(Class<?> clazz, long capacity)
    {
        this.clazz = clazz;
        if(capacity < 0)
            throw new UnsupportedOperationException("capacity is less than zero");
        this.address = getUnsafe().allocateMemory(toAmountBytes(capacity));        
        this.capacityBytes = toAmountBytes(capacity);
        getUnsafe().setMemory(address, capacityBytes, (byte)0);
        initSweeper();
    }
    
    protected MemoryAddress(M memory)
    {
        //no point of checking bounds since memory parameter will not have existed
        this.address = memory.address;
        this.capacityBytes = memory.capacityBytes;
        
        initSweeper();
    }
    
    protected MemoryAddress(M memory, long offset)
    {
        this(memory, offset, memory.capacity() - offset);        
    }
    
    protected MemoryAddress(M memory, long offset, long capacity)
    {
        this.rangeCheckBound(offset, offset + capacity, memory.capacity());
        this.address = memory.address + toAmountBytes(offset);
        this.capacityBytes = toAmountBytes(capacity);
        
        initSweeper();
    }
    
    protected MemoryAddress(Class<?> clazz, M memory, long offset)
    {
        this(clazz, memory, offset, memory.capacity() - offset);        
    }
    
    protected MemoryAddress(Class<?> clazz, M memory, long offset, long capacity)
    {
        this.rangeCheckBound(offset, offset + capacity, memory.capacity());
        this.clazz = clazz;
        this.address = memory.address + toAmountBytes(offset);
        this.capacityBytes = toAmountBytes(capacity);
        
        initSweeper();
    }
    
    private void initSweeper()
    {
        //For garbage collection
        Sweeper.getSweeper().register(this, ()->{
            System.out.println("sfasdf");
            freeMemory();
                });
    }
        
    protected final long toAmountBytes(long amount)
    {
        return amount * sizeOf();
    }
            
    public final long capacity()
    {
        return capacityBytes/sizeOf();
    }
    
    public final long address()
    {
        return address;
    }
        
    public final long address(long offset)
    {
        rangeCheck(offset, capacity());
        return address + toAmountBytes(offset);
    }
    
    public void swap(M m)
    {
        long tempAddress = address;
        long tempCapacityBytes = capacityBytes;
        
        address = m.address;
        capacityBytes = m.capacityBytes;
        m.address = tempAddress;
        m.capacityBytes = tempCapacityBytes;
    }
        
    public final ByteBuffer getDirectByteBufferPoint()
    {
        return MemoryAddress.this.getDirectByteBuffer(0, 1);
    }
    
    public final ByteBuffer getDirectByteBuffer(int offset, int capacity)
    {
        rangeCheck(toAmountBytes(offset) + toAmountBytes(capacity) - 1, this.capacityBytes);
        ByteBuffer bb = ByteBuffer.allocateDirect((int) toAmountBytes(capacity)).order(ByteOrder.nativeOrder());
        try {
            addressField.setLong(bb, address + toAmountBytes(offset));
            capacityField.setInt(bb,     (int) toAmountBytes(capacity)); 
           
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(MemoryAddress.class.getName()).log(Level.SEVERE, null, ex);
        }             
        return bb;
    }
    
    protected final void rangeAboveZero(long index)
    {
        if (index < 1)
            throw new IndexOutOfBoundsException("index out of bound " +index);
    }
    
    //Size should encompass the fromIndex and toIndex 
    protected final void rangeCheck(long offset, long size) {
        if (offset < 0)
            throw new IndexOutOfBoundsException("offset is less than zero " + offset);
        if (offset >= size)
            throw new IndexOutOfBoundsException("offset is beyond range, offset = " + offset + " size = " +size);        
    }    
    
    //Size should encompass the fromIndex and toIndex 
    protected final void rangeCheckBound(long fromIndex, long toIndex, long size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex is less than zero " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex is greater than size, toIndex = " + toIndex + " size = " +size);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }
    
    private Optional<Class<?>> getPrimitiveArrayObject(Object object)
    {
        /* Check if the given object is an array. */
        if (object.getClass().isArray()) {

            Class<?> componentType;
            componentType = object.getClass().getComponentType();
            return Optional.of(componentType);
        }
        return Optional.empty();           
    }
    
    private int getPrimitiveArrayLength(Object object)
    {
        Optional<Class<?>> option = getPrimitiveArrayObject(object);    
        option.orElseThrow(()-> new UnsupportedOperationException("not a primitive array"));
   
        if (option.get().isPrimitive()) {
            if (boolean.class.isAssignableFrom(option.get())) {
                boolean[] arr = (boolean[]) object;
                return arr.length;
            }

            else if (byte.class.isAssignableFrom(option.get())) {
                byte[] arr = (byte[]) object;
                return arr.length;
            }       

            else if (int.class.isAssignableFrom(option.get())) {
                int[] arr = (int[]) object;
                return arr.length;
            }
            
            else if (float.class.isAssignableFrom(option.get())) {
                float[] arr = (float[]) object;
                return arr.length;
            }
        }
        throw new UnsupportedOperationException("primitive array not supported yet");
    }
    
    public void copyFromMem(M m)
    {       
        copyFromMem(m, m.capacity());
    }
    
    public void copyFromMem(M m, long n)
    {       
        if(n == 0)
            return;
        RangeCheck.checkBound(0, n,  m.capacity());
        RangeCheck.checkBound(0, n,  capacity());
        copyMemory(null, m.address(), null, address(), toAmountBytes(n));       
    }
    
    public void copyToMem(M m)
    {       
        copyToMem(m, m.capacity());
    }
    
    public void copyToMem(M m, long n)
    {       
        if(n == 0)
            return;        
        RangeCheck.checkBound(0, n, m.capacity());
        RangeCheck.checkBound(0, n,  capacity());
        copyMemory(null, address(), null, m.address(), toAmountBytes(n));       
    }
    
    //for why we use 16, refer to https://mail.openjdk.org/pipermail/panama-dev/2021-November/015852.html 
    public void copyToArr(A array, long offset, long n)
    {
        if(clazz == null)
        {            
            int length = getPrimitiveArrayLength(array); //not a primitive exception is handled here
            RangeCheck.checkBound(offset, offset + n, capacity()); //range in native memory?
            RangeCheck.checkBound(     0,          n, length);     //range in array?
            copyMemory(null, address() + toAmountBytes(offset), array, 16, toAmountBytes(n));
        }
        else
        {   //we are dealing with objects encoded in bytes therefore array is in bytes
            int length = getPrimitiveArrayLength(array);    
            RangeCheck.checkBound(toAmountBytes(offset), toAmountBytes(offset + n), capacityBytes);//range in native memory?
            RangeCheck.checkBound(                    0,          toAmountBytes(n), length);       //range in array?
            copyMemory(null, address() + toAmountBytes(offset), array, 16, length);
        }
    }
    
    //for why we use 16, refer to https://mail.openjdk.org/pipermail/panama-dev/2021-November/015852.html 
    public void copyFromArr(A array, long offset, long n)
    {
        if(clazz == null)
        {
            int length = getPrimitiveArrayLength(array); //not a primitive exception is handled here
            RangeCheck.checkBound(offset, offset + n, capacity());//range in native memory?
            RangeCheck.checkBound(     0,          n, length);    //range in array?
            copyMemory(array, 16, null, address() + toAmountBytes(offset), toAmountBytes(length));
        }
        else
        {   //we are dealing with objects encoded in bytes therefore array is in bytes
            int length = getPrimitiveArrayLength(array);
            RangeCheck.checkBound(toAmountBytes(offset), toAmountBytes(offset + n), capacityBytes);//range in native memory?
            RangeCheck.checkBound(                    0,          toAmountBytes(n), length);       //range in array?
            copyMemory(array, 16, null, address() + toAmountBytes(offset), length);
        }
    }
    
    public M copy()
    {
        M m = copyStateSize();
        copyMemory(null, address(), null, m.address(), capacityBytes);
        return m;
    }
    
    public final void setArrayCapacityLimitString(int capacity)
    {
        if(capacity < 0)
            throw new UnsupportedOperationException("capacity is less than zero");
        this.arrayCapacityLimitString = capacity;
    }
    
    public final boolean equalSize(M memory)
    {
        return capacity() == memory.capacity();
    }
    
    public final boolean isSame(M memory)
    {
        if(address == 0 || memory.address == 0)
            return false;
        return address == memory.address && capacityBytes == memory.capacityBytes;
    }
    
    public abstract void resize(long capacity);      
    protected abstract M copyStateSize();
    public abstract void freeMemory();
    public abstract int sizeOf();
    public abstract M offsetMemory(long offset);
    public abstract String getString(long start, long end);
    
    public static interface Element<E extends Element<E>>
    {
        public ByteBuffer getBytes();
    }
}
