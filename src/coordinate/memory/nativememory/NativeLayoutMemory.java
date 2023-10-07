/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.nativememory;

import coordinate.memory.type.LayoutArray;
import coordinate.memory.type.LayoutMemory;
import coordinate.memory.type.LayoutMemory.PathElement;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import coordinate.utility.RangeCheck;
import coordinate.utility.Sweeper;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <M>
 */
public abstract class NativeLayoutMemory<M extends NativeLayoutMemory> 
{
    protected LayoutMemory layout;    
    protected long address;
    
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
    
    protected NativeLayoutMemory(LayoutMemory layout, long address, long length)
    {
        RangeCheck.rangeAboveZero(length);
        this.layout = LayoutArray.arrayLayout(length, layout);
        this.address = address;         
        getUnsafe().setMemory(address, this.layout.byteSizeAggregate(), (byte)0); //(byte)0 is to initialise to 0
        initSweeper();
    }
    
    protected NativeLayoutMemory(LayoutMemory layout, long length)
    {
        this(
                layout, 
                getUnsafe().allocateMemory(LayoutArray.arrayLayout(length, layout).byteSizeAggregate()), 
                length);        
    }
    
    protected NativeLayoutMemory(M memory)
    {
        Objects.requireNonNull(memory);
        //no point of checking bounds since memory parameter will need to be in existance
        this.address = memory.address;
        this.layout = memory.layout;
        
        initSweeper();
    }
    
    protected NativeLayoutMemory(M memory, long offset)
    {
        this(memory, offset, memory.capacity() - offset);        
    }
    
    protected NativeLayoutMemory(M memory, long offset, long capacity)
    {
        RangeCheck.indexCheckBound(offset, 0, memory.capacity());
        RangeCheck.indexCheckBound(offset + capacity, 0, memory.capacity());
        this.address = memory.address(offset);
        this.layout = LayoutArray.arrayLayout(capacity, memory.layout.select(PathElement.sequenceElement(0)));
        
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
    
    private void freeMemory()
    {
        if(address!= 0)
        {
            getUnsafe().freeMemory(address);
            address = 0;
        }
    }
    
    public final long capacity()
    {
        return layout.elementCount();
    }
    
    public final long address()
    {
        return address;
    }
        
    public final long address(long offset)
    {
        RangeCheck.rangeCheck(offset, capacity());
        return address + layout.offset(PathElement.sequenceElement(offset));
    }
    
    public final long byteSizeElement()
    {
        return layout.select(PathElement.sequenceElement(0)).byteSizeElement();
    }
    
    private long byteSizeElementRange(long count)
    {
        RangeCheck.rangeAboveZero(count);
        return byteSizeElement() * count;
    }
        
    public void swap(M m)
    {
        long tempAddress = address;
        LayoutMemory tempSequence = layout;
        
        address = m.address;
        layout = m.layout;
        m.address = tempAddress;
        m.layout = tempSequence;
    }
        
    public final ByteBuffer getDirectByteBufferPoint()
    {
        return this.getDirectByteBuffer(0, 1);
    }
    
    public final ByteBuffer getDirectByteBuffer(long offset, int count)
    {
        RangeCheck.indexCheckBound(offset, 0, capacity());
        RangeCheck.indexCheckBound(offset + count, 0, capacity());
        
        ByteBuffer bb = ByteBuffer.allocateDirect(
                (int) byteSizeElementRange(count)).
                order(ByteOrder.nativeOrder());
        
        try {
            addressField.setLong(bb, address(offset));
            capacityField.setInt(bb, (int) byteSizeElementRange(count)); 
           
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(NativeLayoutMemory.class.getName()).log(Level.SEVERE, null, ex);
        }             
        return bb;
    }
    
    public abstract M offsetMemory(long offset);
    
    public M offsetMemoryLast()
    {
        return offsetMemory(capacity() - 1);
    }    
}
