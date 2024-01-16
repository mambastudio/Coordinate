/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.type;

import coordinate.utility.RangeCheckArray;
import coordinate.utility.RangeLong;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 *
 * @author user
 * @param <T>
 * @param <S>
 */

public interface StructCache<T extends StructBase, S extends StructCache<T, S>> {
    
    public void set(T t);    
    public void set(long index, T t);
    public T get(long index);
    public T get();
    default T getLast(){return get(size()-1);}
    
    default String getString(RangeLong range, Function<T, ?> function)
    {
        RangeCheckArray.validateRangeSize(range.low(), range.high(), size());
        StringJoiner joiner = new StringJoiner(", ", "[", "]");       
        for(long i = range.low(); i < range.high(); i++)
            joiner.add(""+function.apply(get(i)));
        return joiner.toString();
    }
    
    default String getString(RangeLong range)
    {
        RangeCheckArray.validateRangeSize(range.low(), range.high(), size());
        StringJoiner joiner = new StringJoiner(", ", "[", "]");       
        for(long i = range.low(); i < range.high(); i++)
            joiner.add(""+get(i));      
        
        return joiner.toString();
    }
                
    public S offsetIndex(long offsetIndex);
    
    public void copyFrom(S m);    
    public void copyTo(S m);
        
    public S copy();    
    
    public void swapElement(long index1, long index2);
    public long address();    
    
    public long size(); 
    
    public long getByteCapacity();
     
    public void free();
    
    default long elementSize()
    {
        return getStructBase().sizeOf();
    }
    
    public MemoryRegion getMemory();
        
    
    public T getStructBase();
    
    default String getMemoryReadableSize()
    {
        return getMemoryReadableSize(getStructBase(), size());
    }    
    
    default long getOffsetByte(long offsetIndex)
    {
        RangeCheckArray.validateIndexSize(offsetIndex, size());
        return offsetIndex * getStructBase().sizeOf();
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
