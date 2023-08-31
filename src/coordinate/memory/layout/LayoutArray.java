/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.layout;

import java.util.UUID;

/**
 *
 * @author jmburu
 */
public class LayoutArray extends LayoutMemory{
    
    private final int byteSize;
    private final LayoutMemory memoryLayout;
    private final String name;
    
    private LayoutArray(int size, LayoutMemory memoryLayout)
    {
        this.byteSize = memoryLayout.byteSize() * size;
        this.memoryLayout = memoryLayout;
        this.name = UUID.randomUUID().toString();
    }
    
    private LayoutArray(String name, int size, LayoutMemory memoryLayout)
    {
        this.byteSize = memoryLayout.byteSize() * size;
        this.memoryLayout = memoryLayout;
        this.name = name;
    }
    
    public static LayoutArray createArray(int size, LayoutMemory memory)
    {
        return new LayoutArray(size, memory);
    }
    
    @Override
    public final int byteSizeElement()
    {
        return memoryLayout.byteSize();
    }

    @Override
    public int byteSize() {
        return byteSize;
    }

    @Override
    public LayoutMemory withId(String name) {
        return new LayoutArray(name, byteSize/memoryLayout.byteSize(), memoryLayout);
    }

    @Override
    public String getId() {
        return name;
    }
    
}
