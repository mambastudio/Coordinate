package coordinate.memory.type;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import coordinate.memory.type.LayoutMemory.PathElement;
import java.util.Objects;

/**
 *
 * @author user
 * @param <Struct>
 */
public abstract class StructBase<Struct extends StructBase<Struct>> {
    
    private final LayoutGroup layout;
    private final MemoryRegion memory;
    
    protected StructBase()
    {
        layout = initLayout();
        memory = MemoryAllocator.allocateHeap(layout);        
        initValueStates();
    }
    
    public final long sizeOf()
    {
        return getLayout().byteSizeAggregate();
    }
    
    public final MemoryRegion memory()
    {        
        return memory;
    }
    
    public final MemoryRegion updateMemory()
    {
        fieldToMemory();
        return memory;
    }
    
    public final void putMemory(MemoryRegion memory)
    {
        Objects.requireNonNull(memory);
        MemoryRegion.checkSameByteCapacity(this.memory, memory);
        this.memory.copyFrom(memory, memory.byteCapacity());        
        memoryToField();        
    }
    
    protected abstract void fieldToMemory();
    protected abstract void memoryToField();
    
    public abstract Struct newInstance();
    public abstract Struct copy();
       
    protected abstract void initValueStates();    
    protected abstract LayoutGroup initLayout();  
    
    public final LayoutGroup getLayout() 
    {
        return layout;
    }
        
    public ValueState valueState(PathElement... elements)
    {
        ValueState value = getLayout().valueState(elements);        
        return value;
    }
}
