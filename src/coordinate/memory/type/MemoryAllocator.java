/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.type;

import static coordinate.unsafe.UnsafeUtils.copyMemory;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import coordinate.utility.RangeCheckArray;
import coordinate.utility.Sweeper;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author user
 */
public class MemoryAllocator {
    public static MemoryRegion allocateHeap(int capacity, int blockBytes)
    {
        return new MemoryHeapImpl(capacity, blockBytes);
    }
    
    public static MemoryRegion allocateHeap(LayoutMemory layout)
    {       
        if(layout.byteSizeAggregate() > Integer.MAX_VALUE - 8)
            throw new OutOfMemoryError("memory heap is quite large");
        return new MemoryHeapImpl(layout);
    }
    
    public static MemoryRegion allocateNative(long capacity, long blockBytes)
    {
        return new MemoryNativeImpl(capacity, blockBytes);
    }
    
    public static MemoryRegion allocateNative(LayoutMemory layout)
    {        
        return new MemoryNativeImpl(layout);
    }
    
    public static MemoryRegion allocateNative(long capacity, long blockBytes, boolean initialise)
    {
        return new MemoryNativeImpl(capacity, blockBytes, initialise);
    }
    
    public static MemoryRegion allocateNative(LayoutMemory layout, boolean initialise)
    {        
        return new MemoryNativeImpl(layout, initialise);
    }
    
    public static MemoryRegion allocateNativeAddress(long byteCapacity, long address)
    {        
        return new MemoryNativeAddressImpl(address, byteCapacity);
    }
    
    protected static class MemoryHeapImpl implements MemoryRegion<MemoryHeapImpl>
    {                
        private ByteBuffer buffer;

        private MemoryHeapImpl(int capacity)
        {            
            buffer = ByteBuffer.allocate(capacity).order(ByteOrder.nativeOrder());
        }

        private MemoryHeapImpl(LayoutMemory layout)
        {            
            buffer = ByteBuffer.allocate((int) layout.byteSizeAggregate()).order(ByteOrder.nativeOrder());
        }

        private MemoryHeapImpl(int capacity, int blockBytes)
        {
            buffer = ByteBuffer.allocate(capacity * blockBytes).order(ByteOrder.nativeOrder());
        }
        
        private MemoryHeapImpl(ByteBuffer buffer)
        {
            if(buffer.order().equals(ByteOrder.nativeOrder()))
                throw new UnsupportedOperationException("buffer in the constructor is not native");
            this.buffer = buffer;
        }

        @Override
        public boolean isNative() {
            return false;
        }
        
        @Override
        public long byteCapacity() {
            return buffer.capacity();
        }

        @Override
        public MemoryHeapImpl offset(long offset) {
            RangeCheckArray.validateIndexRange(offset , 0, byteCapacity());
            buffer.position((int) offset);
            return new MemoryHeapImpl(buffer.slice());
        }
                
        @Override
        public MemoryHeapImpl offset(long byteOffset, long byteSize) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void copyFrom(MemoryRegion m, long byteCapacity) {
            RangeCheckArray.validateRangeSize(0, byteCapacity, m.byteCapacity());
            RangeCheckArray.validateRangeSize(0, byteCapacity, byteCapacity());
            for(long i = 0; i<byteCapacity; i++)
                set(LayoutValue.JAVA_BYTE, i, m.get(LayoutValue.JAVA_BYTE, i));
        }

        @Override
        public void copyTo(MemoryRegion m, long byteCapacity) {
            RangeCheckArray.validateRangeSize(0, byteCapacity, m.byteCapacity());
            RangeCheckArray.validateRangeSize(0, byteCapacity, byteCapacity());
            for(long i = 0; i<byteCapacity; i++)
                m.set(LayoutValue.JAVA_BYTE, i, get(LayoutValue.JAVA_BYTE, i));
        }

        @Override
        public void swap(MemoryHeapImpl m) {
            ByteBuffer temp = m.buffer;
            m.buffer = buffer;
            buffer = temp;        
        }

        @Override
        public long address() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public long address(long offset) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public int getInt(long offset)
        {
            return get(LayoutValue.JAVA_INT, offset);
        }

        public byte getByte(long offset)
        {
            return get(LayoutValue.JAVA_BYTE, offset);
        }

        @Override
        public float get(LayoutValue.OfFloat layout, long offset) {
            return buffer.getFloat((int) offset);
        }

        @Override
        public byte get(LayoutValue.OfByte layout, long offset) {
            return buffer.get((int) offset);
        }

        @Override
        public int get(LayoutValue.OfInteger layout, long offset) {
            return buffer.getInt((int) offset);
        }

        @Override
        public long get(LayoutValue.OfLong layout, long offset) {
            return buffer.getLong((int) offset);
        }

        @Override
        public boolean get(LayoutValue.OfBoolean layout, long offset) {
            return buffer.get((int) offset) > 0;
        }

        @Override
        public char get(LayoutValue.OfChar layout, long offset) {
            return buffer.getChar((int) offset);
        }

        @Override
        public short get(LayoutValue.OfShort layout, long offset) {
            return buffer.getShort((int) offset);
        }

        @Override
        public double get(LayoutValue.OfDouble layout, long offset) {
            return buffer.getDouble((int) offset);
        }

        public void setInt(long offset, int value)
        {
            set(LayoutValue.JAVA_INT, offset, value);
        }

        public void setByte(long offset, byte value)
        {
            set(LayoutValue.JAVA_BYTE, offset, value);
        }

        @Override
        public void set(LayoutValue.OfFloat layout, long offset, float value) {
            buffer.putFloat((int) offset, value);
        }

        @Override
        public void set(LayoutValue.OfByte layout, long offset, byte value) {
            
            buffer.put((int) offset, value);
        }

        @Override
        public void set(LayoutValue.OfInteger layout, long offset, int value) {
            buffer.putInt((int) offset, value);
        }

        @Override
        public void set(LayoutValue.OfLong layout, long offset, long value) {
            buffer.putLong((int) offset, value);
        }

        @Override
        public void set(LayoutValue.OfBoolean layout, long offset, boolean value) {
            buffer.put((int) offset, (byte) (value ? 1 : 0));
        }

        @Override
        public void set(LayoutValue.OfChar layout, long offset, char value) {
            buffer.putChar((int) offset, value);
        }

        @Override
        public void set(LayoutValue.OfShort layout, long offset, short value) {
            buffer.putShort((int) offset, value);
        }

        @Override
        public void set(LayoutValue.OfDouble layout, long offset, double value) {
            buffer.putDouble((int) offset, value);
        }
        
        @Override
        public void fill(byte value) {
            for(long i = 0; i<byteCapacity(); i++)
                set(LayoutValue.JAVA_BYTE, i, value);
        }    

        @Override
        public void dispose() {
            throw new UnsupportedOperationException("this is a heap buffer and not native"); //To change body of generated methods, choose Tools | Templates.
        }    

        @Override
        public void initSweeper() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    protected static class MemoryNativeAddressImpl implements MemoryRegion<MemoryNativeAddressImpl>  
    {
        private long address;
        private long byteCapacity; 

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
        
        private MemoryNativeAddressImpl(long address, long byteCapacity)
        {
            RangeCheckArray.validateIndexSize(byteCapacity, Long.MAX_VALUE);
            this.address = address;
            this.byteCapacity = byteCapacity; 
        }
                
        private MemoryNativeAddressImpl(MemoryNativeAddressImpl memory, long byteOffset)
        {
            RangeCheckArray.validateIndexSize(byteOffset, memory.byteCapacity());
            this.address = memory.address(byteOffset);
            this.byteCapacity = memory.byteCapacity - byteOffset;
        }
        
        private MemoryNativeAddressImpl(MemoryNativeAddressImpl memory, long byteOffset, long byteSize)
        {
            RangeCheckArray.validateIndexSize(byteOffset,            memory.byteCapacity());
            RangeCheckArray.validateRangeSize(byteOffset, byteOffset + byteSize, memory.byteCapacity());
            this.address = memory.address(byteOffset);
            this.byteCapacity = byteSize;
        }
                
        @Override
        public void initSweeper()
        {
            //For garbage collection
            Sweeper.getSweeper().register(this, ()->{
                System.out.println("memory region disposing");
                dispose();
            });
        }
        
        @Override
        public boolean isNative() {
            return true;
        }
        
        @Override
        public long byteCapacity() {
            return byteCapacity;
        }

        @Override
        public MemoryNativeAddressImpl offset(long byteOffset) {
            return new MemoryNativeAddressImpl(this, byteOffset);
        }
                
        @Override
        public MemoryNativeAddressImpl offset(long byteOffset, long byteSize) {
            return new MemoryNativeAddressImpl(this, byteOffset, byteSize);
        }
                
        @Override
        public void copyFrom(MemoryRegion m, long n) {
            RangeCheckArray.validateRangeSize(0, n,  m.byteCapacity());
            RangeCheckArray.validateRangeSize(0, n,  byteCapacity());
            if(m.isNative())                                   
                copyMemory(null, m.address(), null, address(), n);               
            else
                for(long i = 0; i < n; i++)
                    set(LayoutValue.JAVA_BYTE, i, m.get(LayoutValue.JAVA_BYTE, i));
        }

        @Override
        public void copyTo(MemoryRegion m, long n) {
            RangeCheckArray.validateRangeSize(0, n,  m.byteCapacity());
            RangeCheckArray.validateRangeSize(0, n,  byteCapacity());
            if(m.isNative())                                   
                copyMemory(null, address(), null, m.address(), n);      
            else
                for(long i = 0; i < n; i++)
                    m.set(LayoutValue.JAVA_BYTE, i, get(LayoutValue.JAVA_BYTE, i));
        }

        @Override
        public final void fill(byte value) {
            long remaining = byteCapacity % 8;
            
            // Fill the aligned part efficiently
            getUnsafe().setMemory(address, byteCapacity - remaining, value);

            // Fill the remaining unaligned bytes individually
            for (int i = 0; i < remaining; i++) {
                getUnsafe().setMemory(address + byteCapacity - remaining + i, 1, value);
            }
        }

        @Override
        public void swap(MemoryNativeAddressImpl m) {
            long tempAddress = address;
            long tempCapacityBytes = byteCapacity;

            address = m.address;
            byteCapacity = m.byteCapacity;
            m.address = tempAddress;
            m.byteCapacity = tempCapacityBytes;
        }

        @Override
        public long address() {
            return address;
        }

        @Override
        public long address(long offset) {
            return address() + offset;
        }

        @Override
        public float get(LayoutValue.OfFloat layout, long offset) {
            return getUnsafe().getFloat(address(offset));
        }

        @Override
        public byte get(LayoutValue.OfByte layout, long offset) {
            return getUnsafe().getByte(address(offset));
        }

        @Override
        public boolean get(LayoutValue.OfBoolean layout, long offset) {
            return getUnsafe().getByte(address(offset))>0;
        }

        @Override
        public char get(LayoutValue.OfChar layout, long offset) {
            return getUnsafe().getChar(address(offset));
        }

        @Override
        public short get(LayoutValue.OfShort layout, long offset) {
            return getUnsafe().getShort(address(offset));
        }

        @Override
        public int get(LayoutValue.OfInteger layout, long offset) {
            return getUnsafe().getInt(address(offset));
        }

        @Override
        public long get(LayoutValue.OfLong layout, long offset) {
            return getUnsafe().getLong(address(offset));
        }

        @Override
        public double get(LayoutValue.OfDouble layout, long offset) {
            return getUnsafe().getDouble(address(offset));
        }

        @Override
        public void set(LayoutValue.OfFloat layout, long offset, float value) {            
            getUnsafe().putFloat(address(offset), value);
        }

        @Override
        public void set(LayoutValue.OfByte layout, long offset, byte value) {
            getUnsafe().putByte(address(offset), value);
        }

        @Override
        public void set(LayoutValue.OfBoolean layout, long offset, boolean value) {
            getUnsafe().putByte(address(offset), (byte) (value ? 1 : 0));
        }

        @Override
        public void set(LayoutValue.OfChar layout, long offset, char value) {
            getUnsafe().putChar(address(offset), value);
        }

        @Override
        public void set(LayoutValue.OfShort layout, long offset, short value) {
            getUnsafe().putShort(address(offset), value);
        }

        @Override
        public void set(LayoutValue.OfInteger layout, long offset, int value) {           
            getUnsafe().putInt(address(offset), value);           
        }

        @Override
        public void set(LayoutValue.OfLong layout, long offset, long value) {
            getUnsafe().putLong(address(offset), value);
        }

        @Override
        public void set(LayoutValue.OfDouble layout, long offset, double value) {
            getUnsafe().putDouble(address(offset), value);            
        }

        @Override
        public void dispose() {
            if(address()!=0)
            {
                getUnsafe().freeMemory(address());
                address = 0;
            }
        }  

    }
    
    protected static class MemoryNativeImpl implements MemoryRegion<MemoryNativeImpl>  
    {
        private long address;
        private long byteCapacity; 

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
        
        private MemoryNativeImpl(long byteCapacity, boolean initialise)
        {         
            RangeCheckArray.validateIndexSize(byteCapacity, Long.MAX_VALUE);
            this.address = getUnsafe().allocateMemory(byteCapacity);
            this.byteCapacity = byteCapacity;
            if(initialise)
                fill((byte)0); //(byte)0 is to initialise to 0
        }
        
        private MemoryNativeImpl(long byteCapacity)
        {         
            this(byteCapacity, true);
        }
                
        private MemoryNativeImpl(long capacity, long blockBytes)
        {
            this(capacity * blockBytes);
        }
        
        private MemoryNativeImpl(long capacity, long blockBytes, boolean initialise)
        {
            this(capacity * blockBytes, initialise);
        }

        private MemoryNativeImpl(LayoutMemory layout)
        {            
            this(layout.byteSizeAggregate());
        }
        
        private MemoryNativeImpl(LayoutMemory layout, boolean initialise)
        {            
            this(layout.byteSizeAggregate(), initialise);
        }
        
        private MemoryNativeImpl(MemoryNativeImpl memory, long byteOffset)
        {
            RangeCheckArray.validateIndexSize(byteOffset, memory.byteCapacity());
            this.address = memory.address(byteOffset);
            this.byteCapacity = memory.byteCapacity - byteOffset;
        }
        
        private MemoryNativeImpl(MemoryNativeImpl memory, long byteOffset, long byteSize)
        {
            RangeCheckArray.validateIndexSize(byteOffset           , memory.byteCapacity());
            RangeCheckArray.validateRangeSize(byteOffset, byteOffset + byteSize, memory.byteCapacity());
            this.address = memory.address(byteOffset);
            this.byteCapacity = byteSize;
        }
                
        @Override
        public void initSweeper()
        {
            //For garbage collection
            Sweeper.getSweeper().register(this, ()->{
                System.out.println("memory region disposing");
                dispose();
            });
        }
        
        @Override
        public boolean isNative() {
            return true;
        }
        
        @Override
        public long byteCapacity() {
            return byteCapacity;
        }

        @Override
        public MemoryNativeImpl offset(long byteOffset) {
            return new MemoryNativeImpl(this, byteOffset);
        }
        
        @Override
        public MemoryNativeImpl offset(long byteOffset, long byteSize) {
            return new MemoryNativeImpl(this, byteOffset, byteSize);
        }
        
        @Override
        public void copyFrom(MemoryRegion m, long n) {
            RangeCheckArray.validateRangeSize(0, n,  m.byteCapacity());
            RangeCheckArray.validateRangeSize(0, n,  byteCapacity());
            if(m.isNative())                                   
                copyMemory(null, m.address(), null, address(), n);               
            else
                for(long i = 0; i < n; i++)
                    set(LayoutValue.JAVA_BYTE, i, m.get(LayoutValue.JAVA_BYTE, i));
        }

        @Override
        public void copyTo(MemoryRegion m, long n) {
            RangeCheckArray.validateRangeSize(0, n,  m.byteCapacity());
            RangeCheckArray.validateRangeSize(0, n,  byteCapacity());
            if(m.isNative())                                   
                copyMemory(null, address(), null, m.address(), n);      
            else
                for(long i = 0; i < n; i++)
                    m.set(LayoutValue.JAVA_BYTE, i, get(LayoutValue.JAVA_BYTE, i));
        }

        @Override
        public void fill(byte value) {
            long remaining = byteCapacity % 8;
            
            // Fill the aligned part efficiently
            getUnsafe().setMemory(address, byteCapacity - remaining, value);

            // Fill the remaining unaligned bytes individually
            for (int i = 0; i < remaining; i++) {
                getUnsafe().setMemory(address + byteCapacity - remaining + i, 1, value);
            }
        }

        @Override
        public void swap(MemoryNativeImpl m) {
            long tempAddress = address;
            long tempCapacityBytes = byteCapacity;

            address = m.address;
            byteCapacity = m.byteCapacity;
            m.address = tempAddress;
            m.byteCapacity = tempCapacityBytes;
        }

        @Override
        public long address() {
            return address;
        }

        @Override
        public long address(long offset) {
            return address() + offset;
        }

        @Override
        public float get(LayoutValue.OfFloat layout, long offset) {
            return getUnsafe().getFloat(address(offset));
        }

        @Override
        public byte get(LayoutValue.OfByte layout, long offset) {
            return getUnsafe().getByte(address(offset));
        }

        @Override
        public boolean get(LayoutValue.OfBoolean layout, long offset) {
            return getUnsafe().getByte(address(offset))>0;
        }

        @Override
        public char get(LayoutValue.OfChar layout, long offset) {
            return getUnsafe().getChar(address(offset));
        }

        @Override
        public short get(LayoutValue.OfShort layout, long offset) {
            return getUnsafe().getShort(address(offset));
        }

        @Override
        public int get(LayoutValue.OfInteger layout, long offset) {
            return getUnsafe().getInt(address(offset));
        }

        @Override
        public long get(LayoutValue.OfLong layout, long offset) {
            return getUnsafe().getLong(address(offset));
        }

        @Override
        public double get(LayoutValue.OfDouble layout, long offset) {
            return getUnsafe().getDouble(address(offset));
        }

        @Override
        public void set(LayoutValue.OfFloat layout, long offset, float value) {            
            getUnsafe().putFloat(address(offset), value);
        }

        @Override
        public void set(LayoutValue.OfByte layout, long offset, byte value) {
            getUnsafe().putByte(address(offset), value);
        }

        @Override
        public void set(LayoutValue.OfBoolean layout, long offset, boolean value) {
            getUnsafe().putByte(address(offset), (byte) (value ? 1 : 0));
        }

        @Override
        public void set(LayoutValue.OfChar layout, long offset, char value) {
            getUnsafe().putChar(address(offset), value);
        }

        @Override
        public void set(LayoutValue.OfShort layout, long offset, short value) {
            getUnsafe().putShort(address(offset), value);
        }

        @Override
        public void set(LayoutValue.OfInteger layout, long offset, int value) {           
            getUnsafe().putInt(address(offset), value);           
        }

        @Override
        public void set(LayoutValue.OfLong layout, long offset, long value) {
            getUnsafe().putLong(address(offset), value);
        }

        @Override
        public void set(LayoutValue.OfDouble layout, long offset, double value) {
            getUnsafe().putDouble(address(offset), value);            
        }
        
        @Override
        public void dispose() {
            if(address()!=0)
            {
                getUnsafe().freeMemory(address());
                address = 0;
            }
        }  
    }
}
