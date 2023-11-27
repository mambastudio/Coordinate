package coordinate.memory.type;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 * @param <Struct>
 * 
 * https://gavinray97.github.io/blog/panama-not-so-foreign-memory
 * 
 */
public interface StructBase<Struct extends StructBase<Struct>> {
    
    default long sizeOf()
    {
        return getLayout().byteSizeAggregate();
    }
        
    public void fieldToMemory(MemoryRegion memory);
    public void memoryToField(MemoryRegion memory);
    
    public Struct newStruct();
    public Struct copyStruct();       
    public LayoutMemory getLayout();
}
