/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.layout;

import coordinate.utility.RangeCheck;
import java.util.UUID;

/**
 *
 * @author jmburu
 */
public class LayoutArray extends LayoutMemory{
    private final int elementCount;
    private final int byteSize;
    private final LayoutMemory memoryLayout;
    private final String name;
    
    private LayoutArray(int elementCount, LayoutMemory memoryLayout)
    {
        this.elementCount = elementCount;
        this.byteSize = memoryLayout.byteSizeAggregate() * elementCount;
        this.memoryLayout = memoryLayout;
        this.name = UUID.randomUUID().toString();
    }
    
    private LayoutArray(String name, int elementCount, LayoutMemory memoryLayout)
    {
        this.elementCount = elementCount;
        this.byteSize = memoryLayout.byteSizeAggregate() * elementCount;
        this.memoryLayout = memoryLayout;
        this.name = name;
    }
    
    public static LayoutArray arrayLayout(int elementCount, LayoutMemory memory)
    {
        RangeCheck.rangeAboveZero(elementCount);
        return new LayoutArray(elementCount, memory);
    }
    
    public static LayoutArray createArray(LayoutMemory memory)
    {
        return arrayLayout(1, memory);
    }
    
    @Override
    public final int byteSizeElement()
    {
        return memoryLayout.byteSizeAggregate();
    }

    @Override
    public int byteSizeAggregate() {
        return byteSize;
    }

    @Override
    public LayoutMemory withId(String name) {
        return new LayoutArray(name, elementCount, memoryLayout);
    }
    
    public LayoutMemory getLayoutMemory()
    {
        return memoryLayout;
    }

    @Override
    public String getId() {
        return name;
    }
    
    public int elementCount()
    {
        return elementCount;
    }
}
