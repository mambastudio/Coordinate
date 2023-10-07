/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.type;

import coordinate.utility.RangeCheckArray;
import java.util.UUID;

/**
 *
 * @author jmburu
 */
public class LayoutArray extends LayoutMemory{
    private final long byteSize;
    private final LayoutMemory memoryLayout;
    private final String name;
    
    private LayoutArray(long elementCount, LayoutMemory memoryLayout)
    {
        this.byteSize = memoryLayout.byteSizeAggregate() * elementCount;
        this.memoryLayout = memoryLayout;
        this.name = UUID.randomUUID().toString();
    }
    
    private LayoutArray(String name, long elementCount, LayoutMemory memoryLayout)
    {
        this.byteSize = memoryLayout.byteSizeAggregate() * elementCount;
        this.memoryLayout = memoryLayout;
        this.name = name;
    }
    
    public static LayoutArray arrayLayout(long elementCount, LayoutMemory memory)
    {
        RangeCheckArray.validateIndexSize(elementCount, Long.MAX_VALUE);
        return new LayoutArray(elementCount, memory);
    }
    
    public static LayoutArray createArray(LayoutMemory memory)
    {
        return arrayLayout(1, memory);
    }
    
    @Override
    public final long byteSizeElement()
    {
        return memoryLayout.byteSizeAggregate();
    }

    @Override
    public long byteSizeAggregate() {
        return byteSize;
    }

    @Override
    public LayoutMemory withId(String name) {
        return new LayoutArray(name, elementCount(), memoryLayout);
    }
    
    public LayoutMemory getLayoutMemory()
    {
        return memoryLayout;
    }

    @Override
    public String getId() {
        return name;
    }    
}
