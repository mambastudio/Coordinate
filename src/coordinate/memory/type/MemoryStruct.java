package coordinate.memory.type;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import coordinate.memory.type.LayoutMemory.PathElement;
import coordinate.memory.type.MemoryAllocator.MemoryNativeImpl;
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
public final class MemoryStruct<T extends StructBase> {
    private final MemoryRegion memory;
    private final LayoutMemory memoryLayout;
    private final T abstractT;
    private final int maximumStringCount = 1000;
    
    public MemoryStruct(T t)
    {
        this(t, 1);
        set(0, t);
    }
    
    public MemoryStruct(T t, long size)
    {        
        Objects.requireNonNull(t);
        RangeCheckArray.validateIndexSize(size, Long.MAX_VALUE);
        this.memoryLayout = LayoutArray.arrayLayout(size, t.getLayout());
        this.memory = MemoryAllocator.allocateNative(this.memoryLayout);
        this.abstractT = t;
    }
    
    public MemoryStruct(T t, long size, boolean initialise)
    {        
        Objects.requireNonNull(t);
        RangeCheckArray.validateIndexSize(size, Long.MAX_VALUE);
        this.memoryLayout = LayoutArray.arrayLayout(size, t.getLayout());
        this.memory = MemoryAllocator.allocateNative(this.memoryLayout, initialise);
        this.abstractT = t;
    }
    
    private MemoryStruct(MemoryStruct<T> m, long offsetIndex)
    {
        Objects.requireNonNull(m);
        RangeCheckArray.validateIndexSize(offsetIndex, m.size());  
        
        //arraySize = m.size() - offsetIndex
        this.memoryLayout = LayoutArray.arrayLayout(m.memoryLayout.elementCount() - offsetIndex, m.abstractT.getLayout());
        //byteOffset = m.offsetIndex(PathElement.sequenceElement(offsetIndex));
        this.memory = m.memory.offset(m.memoryLayout.offset(PathElement.sequenceElement(offsetIndex)), memoryLayout.byteSizeAggregate());
        //all structs have a copy method
        this.abstractT = (T) m.abstractT.copy();        
    }
        
    public T get(long index)
    {        
        LayoutMemory structLayout = memoryLayout.select(LayoutMemory.PathElement.sequenceElement(index));            
        MemoryRegion mm = memory.offset(structLayout.offset(), structLayout.byteSizeAggregate());        
        T newT = (T) abstractT.newInstance();
        newT.putMemory(mm);
        return newT;
    }
    
    public void set(long index, T newT)
    {
        LayoutMemory structLayout = memoryLayout.select(PathElement.sequenceElement(index));        
        MemoryRegion mm = memory.offset(structLayout.offset(), structLayout.byteSizeAggregate());
        newT.updateMemory().copyTo(mm, mm.byteCapacity());
    }
        
    public void forEachSet(BiObjLongFunction<T> function)
    {
        for(long i = 0; i < size(); i++)
            set(i, function.apply(get(i), i));
    }
    
    public<V> void forEachSet(RangeLong range, BiObjLongFunction<T> function)
    {
        RangeCheckArray.validateRangeSize(range.low(), range.high(), size());        
        for(long i = range.low(); i < range.high(); i++)
            set(i, function.apply(get(i), i));
    }
    
    public String getString(RangeLong range, Function<T, ?> function)
    {
        RangeCheckArray.validateRangeSize(range.low(), range.high(), size());
        StringJoiner joiner = new StringJoiner(", ", "[", "]");       
        for(long i = range.low(); i < range.high(); i++)
            joiner.add(""+function.apply(get(i)));
        return joiner.toString();
    }
    
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
    
    public MemoryStruct<T> offsetIndex(long index)
    {        
        return new MemoryStruct(this, index);        
    }
    
    public void copyFrom(MemoryStruct<T> m)
    {
        memory.copyFrom(m.memory, m.memory.byteCapacity());
    }
    public void copyTo(MemoryStruct<T> m)
    {
        memory.copyTo(m.memory, m.memory.byteCapacity());
    }
    
    public MemoryStruct<T> copy()
    { 
        MemoryStruct<T> mem = new MemoryStruct(abstractT.copy(), size(), false);
        copyTo(mem);
        return mem;
    }
    
    public void swapElement(long index1, long index2)
    {
        RangeCheckArray.validateIndexSize(index1, size());
        RangeCheckArray.validateIndexSize(index2, size());        
        T temp = get(index1);
        set(index1, get(index2));
        set(index2, temp);
    }
    
    public long address()
    {
        return memory.address();
    }
    
    public long size()
    {
        return memoryLayout.elementCount();
    }
    
    public LayoutMemory getStructLayout()
    {
        return memoryLayout.select(PathElement.sequenceElement());
    }
    
    public ValueState getState(PathElement... elements)
    {
        return memoryLayout.valueState(elements);
    }
    
    public void dispose()
    {
        memory.dispose();
    }
    
    public MemoryRegion getMemory()
    {
        return memory;
    }
        
    public interface BiObjLongFunction<V extends StructBase>
    {
        V apply(V v, long value);
    }
    
    public T getStructBase()
    {
        return (T) abstractT.copy();
    }
    
    public String getMemoryReadableSize()
    {
        return getMemoryReadableSize(abstractT, size());
    }
    
    public static<T extends StructBase> String getMemoryReadableSize(T t, long size)
    {
        long bytes = LayoutArray.arrayLayout(size, t.getLayout()).byteSizeAggregate();
        
        if (bytes >= 1024 * 1024 * 1024) {
            // Convert to GB
            double gb = (double) bytes / (1024 * 1024 * 1024);
            return String.format("%.2f GB", gb);
        } else if (bytes >= 1024 * 1024) {
            // Convert to MB
            double mb = (double) bytes / (1024 * 1024);
            return String.format("%.2f MB", mb);
        } else if (bytes >= 1024) {
            // Convert to KB
            double kb = (double) bytes / 1024;
            return String.format("%.2f KB", kb);
        } else {
            // Display in bytes
            return bytes + " bytes";
        }
    }
}
