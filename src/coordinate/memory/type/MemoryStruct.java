package coordinate.memory.type;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import coordinate.memory.type.MemoryAllocator.MemoryNativeImpl;
import coordinate.utility.RangeCheck;
import coordinate.utility.RangeCheckArray;
import coordinate.utility.RangeLong;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <T>
 */
public final class MemoryStruct<T extends StructBase> implements StructCache<T, MemoryStruct<T>>{
    private MemoryRegion memory;
    private long size;    
    private final T abstractT;
    private final int maximumStringCount = 3000;
    
    public MemoryStruct(T t)
    {
        this(t, 1);
        set(0, t);
    }
    
    public MemoryStruct(T t, long size)
    {        
        Objects.requireNonNull(t);
        RangeCheckArray.validateIndexSize(size, Long.MAX_VALUE);
        this.memory = MemoryAllocator.allocateNative(size, t.sizeOf());
        this.abstractT = t;
        this.size = size;
        
        
    }
    
    public MemoryStruct(T t, long size, boolean initialise)
    {        
        Objects.requireNonNull(t);
        RangeCheckArray.validateIndexSize(size, Long.MAX_VALUE);
        this.memory = MemoryAllocator.allocateNative(size, t.sizeOf(), initialise);
        this.abstractT = t;
        this.size = size;
    }
    
    public MemoryStruct(T t, MemoryRegion memory)
    {        
        Objects.requireNonNull(t);
        Objects.requireNonNull(memory);
        this.memory = memory;
        this.abstractT = t;
        if((memory.byteCapacity() % t.sizeOf()) != 0)
            throw new UnsupportedOperationException("memory region size is not a multiple of size of struct");
        this.size = memory.byteCapacity() / t.sizeOf();
    }
    
    public MemoryStruct(T t, long size, long address)
    {        
        Objects.requireNonNull(t);
        RangeCheckArray.validateIndexSize(size, Long.MAX_VALUE);
        this.memory = MemoryAllocator.allocateNativeAddress(t.sizeOf() * size, address);
        this.abstractT = t;
        this.size = size;
    }
    
    private MemoryStruct(MemoryStruct<T> m, long index)
    {
        Objects.requireNonNull(m);
        RangeCheckArray.validateIndexSize(index, m.size());        
        this.memory = m.memory.offsetIndex(index, m.abstractT.getLayout());
        this.size = m.size() - index;
        this.abstractT = (T) m.abstractT.newStruct();        
    }
    
    public static<T extends StructBase<T>> MemoryStruct<T> allocateHeap(T t, long size)
    {
        MemoryRegion memory = MemoryAllocator.allocateHeap(LayoutArray.arrayLayout(size, t.getLayout()));
        return new MemoryStruct(t, memory);
    }
    
    public static<T extends StructBase<T>> MemoryStruct<T> allocateNative(T t, long size, boolean initialised)
    {
        return new MemoryStruct(t, size, initialised);
    }
      
    
    public void reallocate(long size)
    {        
        if(memory.isNative())
        {
            RangeCheckArray.validateIndexSize(size, Long.MAX_VALUE);
            free();
            this.memory = MemoryAllocator.allocateNative(size, abstractT.sizeOf());
            this.size = size;
        }
        else
        {
            RangeCheckArray.validateIndexSize(size, Integer.MAX_VALUE);
            free();
            this.memory = MemoryAllocator.allocateHeap(Math.toIntExact(size), Math.toIntExact(abstractT.sizeOf()));
            this.size = size;
        }
    }
    
    @Override
    public T get(long index)
    {                        
        T newT = (T) abstractT.newStruct();
        newT.memoryToField(offsetIndex(index).getMemory());
        return newT;
    }
    
    
    @Override
    public void set(long index, T newT)
    {
        newT.fieldToMemory(offsetIndex(index).getMemory());
    }
        
    @Override
    public String getString(RangeLong range, Function<T, ?> function)
    {
        RangeCheckArray.validateRangeSize(range.low(), range.high(), size());
        StringJoiner joiner = new StringJoiner(", ", "[", "]");       
        for(long i = range.low(); i < range.high(); i++)
            joiner.add(""+function.apply(get(i)));
        return joiner.toString();
    }
    
    
    @Override
    public String getString(RangeLong range)
    {
        RangeCheckArray.validateRangeSize(range.low(), range.high(), size());
        StringJoiner joiner = new StringJoiner(", ", "[", "]");       
        for(long i = range.low(); i < range.high(); i++)
            joiner.add(""+get(i));
        return joiner.toString();
    }
    
    
    @Override
    public String toString()
    {
        return getString(new RangeLong(0, Math.min(size(), maximumStringCount)));
    }
    
    public final ByteBuffer getDirectByteBufferPoint()
    {
       return this.getDirectByteBuffer(0, 1);
    }
    
    public final ByteBuffer getDirectByteBuffer(int index, int capacity)
    {
        RangeCheckArray.validateIndexSize(index, size());
        RangeCheckArray.validateRangeSize(0, capacity, size());
        
        long byteCapacity   = LayoutArray.arrayLayout(capacity, abstractT.getLayout()).byteSizeAggregate();
        long byteOffset     = LayoutArray.arrayLayout(capacity, abstractT.getLayout()).offset();
        
        ByteBuffer bb = ByteBuffer.allocateDirect((int) byteCapacity).order(ByteOrder.nativeOrder());
        try {
            MemoryNativeImpl.addressField.setLong(bb, address() + byteOffset);
            MemoryNativeImpl.capacityField.setInt(bb, (int) byteCapacity); 
           
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(MemoryStruct.class.getName()).log(Level.SEVERE, null, ex);
        }             
        return bb;        
    }
    
    
    @Override
    public MemoryStruct<T> offsetIndex(long index)
    {        
        return new MemoryStruct(this, index);        
    }
    
    public MemoryStruct<T> offsetLast()
    {        
        return new MemoryStruct(this, size - 1);        
    }
    
    
    @Override
    public void copyFrom(MemoryStruct<T> m)
    {
        memory.copyFrom(m.memory, m.memory.byteCapacity());
    }
    
    public void copyFrom(MemoryStruct<T> m, long n)
    {
        memory.copyFrom(m.memory, abstractT.sizeOf() * n);
    }
    
    @Override
    public void copyTo(MemoryStruct<T> m)
    {
        memory.copyTo(m.memory, m.memory.byteCapacity());
    }
    
    public void copyTo(MemoryStruct<T> m, long n)
    {
        memory.copyTo(m.memory, abstractT.sizeOf() * n);
    }
    
     public MemoryStruct<T> fill(T val) {
        for (long i = 0, len = size(); i < len; i++)
            set(i, (T) val.copyStruct());
        return this;
    }
    
    public MemoryStruct<T> fill(T val, long n)
    {
        RangeCheck.checkBound(0, n, size());
        for (long i = 0, len = n; i < len; i++)
            set(i, (T) val.copyStruct());
        return this;
    }
    
    @Override
    public MemoryStruct<T> copy()
    { 
        MemoryStruct<T> mem = new MemoryStruct(abstractT.newStruct(), size(), false);
        copyTo(mem);
        return mem;
    }
    
    
    
    @Override
    public void swapElement(long index1, long index2)
    {
        RangeCheckArray.validateIndexSize(index1, size());
        RangeCheckArray.validateIndexSize(index2, size());        
        T temp = get(index1);
        set(index1, get(index2));
        set(index2, temp);
    }
    
    public void swap(MemoryStruct<T> memStruct)
    {
        MemoryRegion tempMem = memory;
        long tempSize = size; 
        
        memory = memStruct.memory;
        size = memStruct.size;
        
        memStruct.memory = tempMem;
        memStruct.size = tempSize;
    }
    
    
    @Override
    public long address()
    {
        return memory.address();
    }
    
    @Override
    public long size()
    {
        return size;
    }
    
    public void dispose()
    {
        memory.dispose();
    }
    
    @Override
    public MemoryRegion getMemory()
    {
        return memory;
    }
    
    @Override
    public T getStructBase()
    {
        return (T) abstractT.newStruct();
    }
    
    
    @Override
    public String getMemoryReadableSize()
    {
        return StructCache.getMemoryReadableSize(abstractT, size());
    }
    
    @Override
    public long getOffsetByte(long offsetIndex)
    {
        RangeCheckArray.validateIndexSize(offsetIndex, size());
        return offsetIndex * abstractT.sizeOf();
    }
    
    @Override
    public void set(T t) {
        set(0, t);
    }

    @Override
    public T get() {
        return get(0);
    }

    @Override
    public long getByteCapacity() {
        return memory.byteCapacity();
    }

    @Override
    public void free() {
        memory.dispose();
    }
}
