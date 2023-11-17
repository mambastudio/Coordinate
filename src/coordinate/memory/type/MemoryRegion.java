/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.type;

/**
 *
 * @author user
 * @param <M>
 */
public interface MemoryRegion<M extends MemoryRegion>{     
    public long byteCapacity();
            
    public M offset(long byteOffset);
    default M offset(long byteOffset, long byteAlignment)
    {
        throw new UnsupportedOperationException("This method is not implemented");
    }
    public void copyFrom(MemoryRegion m, long byteAlignment);
    public void copyTo(MemoryRegion m, long byteAlignment);
            
    public static boolean isSameByteCapacity(MemoryRegion m1, MemoryRegion m2)
    {
        return m1.byteCapacity() == m2.byteCapacity();
    }
    
    public static void checkSameByteCapacity(MemoryRegion m1, MemoryRegion m2)
    {
        if(!isSameByteCapacity(m1, m2))
            throw new IllegalArgumentException("memories are not equally same capacity");        
    }
    
    public void fill(byte value);  
    
    public void swap(M m);
    
    public long address();
    public long address(long offset);
    
    public boolean isNative();  
        
    public void dispose();
        
    public float    get(LayoutValue.OfFloat layout,    long offset);
    public byte     get(LayoutValue.OfByte layout,     long offset);
    public boolean  get(LayoutValue.OfBoolean layout,  long offset);
    public char     get(LayoutValue.OfChar layout,     long offset);
    public short    get(LayoutValue.OfShort layout,    long offset);
    public int      get(LayoutValue.OfInteger layout,  long offset);
    public long     get(LayoutValue.OfLong layout,     long offset);
    public double   get(LayoutValue.OfDouble layout,   long offset);
    
    default float    getAtIndex(LayoutValue.OfFloat layout,    long index){return get(layout, index * layout.byteSizeElement());}
    default byte     getAtIndex(LayoutValue.OfByte layout,     long index){return get(layout, index * layout.byteSizeElement());}
    default boolean  getAtIndex(LayoutValue.OfBoolean layout,  long index){return get(layout, index * layout.byteSizeElement());}
    default char     getAtIndex(LayoutValue.OfChar layout,     long index){return get(layout, index * layout.byteSizeElement());}
    default short    getAtIndex(LayoutValue.OfShort layout,    long index){return get(layout, index * layout.byteSizeElement());}
    default int      getAtIndex(LayoutValue.OfInteger layout,  long index){return get(layout, index * layout.byteSizeElement());}
    default long     getAtIndex(LayoutValue.OfLong layout,     long index){return get(layout, index * layout.byteSizeElement());}
    default double   getAtIndex(LayoutValue.OfDouble layout,   long index){return get(layout, index * layout.byteSizeElement());}
    
    public void set(LayoutValue.OfFloat layout,     long offset, float   value);
    public void set(LayoutValue.OfByte layout,      long offset, byte    value);
    public void set(LayoutValue.OfBoolean layout,   long offset, boolean value);
    public void set(LayoutValue.OfChar layout,      long offset, char    value);
    public void set(LayoutValue.OfShort layout,     long offset, short   value);
    public void set(LayoutValue.OfInteger layout,   long offset, int     value);
    public void set(LayoutValue.OfLong layout,      long offset, long    value);    
    public void set(LayoutValue.OfDouble layout,    long offset, double  value); 
    
    default void setAtIndex(LayoutValue.OfFloat layout,     long index, float   value){set(layout, index * layout.byteSizeElement(), value);}
    default void setAtIndex(LayoutValue.OfByte layout,      long index, byte    value){set(layout, index * layout.byteSizeElement(), value);}
    default void setAtIndex(LayoutValue.OfBoolean layout,   long index, boolean value){set(layout, index * layout.byteSizeElement(), value);}
    default void setAtIndex(LayoutValue.OfChar layout,      long index, char    value){set(layout, index * layout.byteSizeElement(), value);}
    default void setAtIndex(LayoutValue.OfShort layout,     long index, short   value){set(layout, index * layout.byteSizeElement(), value);}
    default void setAtIndex(LayoutValue.OfInteger layout,   long index, int     value){set(layout, index * layout.byteSizeElement(), value);}
    default void setAtIndex(LayoutValue.OfLong layout,      long index, long    value){set(layout, index * layout.byteSizeElement(), value);}    
    default void setAtIndex(LayoutValue.OfDouble layout,    long index, double  value){set(layout, index * layout.byteSizeElement(), value);} 

    
}
