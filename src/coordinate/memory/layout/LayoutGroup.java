/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.layout;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author user
 * 
 * Heavily inspired by Panama java. This actually avoids reflection and let's the user develop their own 
 * struct API independently
 * 
 */
public final class LayoutGroup extends LayoutMemory{
    private final LinkedHashMap<String, LayoutMemory> fields = new LinkedHashMap();  
    private int   fieldMaxBytes;
    
    private int byteSize;
    
    private final String name;
    
    private LayoutGroup()
    {
        this.name = UUID.randomUUID().toString();
    }
    
    private LayoutGroup(String name)
    {
        this.name = name;
    }
    
    public static LayoutGroup groupLayout(LayoutMemory... memoryLayouts)
    {
        LayoutGroup group = new LayoutGroup();
        for(LayoutMemory memoryLayout : memoryLayouts)
            group.fields.put(memoryLayout.getId(), memoryLayout);
        
        group.init();
        
        return group;
    }
    
    public boolean containsField(String name)
    {
        return fields.containsKey(name);
    }
    
    public LayoutMemory getField(String name)
    {
        return fields.get(name);
    }
    
    public static LayoutGroup createGroup(String name, LayoutMemory... memoryLayouts)
    {
        LayoutGroup group = new LayoutGroup(name);
        for(LayoutMemory memoryLayout : memoryLayouts)
        {
            group.fields.put(memoryLayout.getId(), memoryLayout);
        }
        
        group.init();
        
        return group;
    }
    
    private void init()
    {
        calculateMaxByteSize();
        calculateFieldOffsets();    
    }
    
    private void calculateMaxByteSize()
    {
        layoutIterator((memlayout)->{
            fieldMaxBytes = memlayout.byteSizeAggregate() > fieldMaxBytes ? memlayout.byteSizeAggregate() : fieldMaxBytes;
        });       
    }
    
    private void calculateFieldOffsets()
    {       
        
        AtomicInteger currentOffset = new AtomicInteger();      
        layoutIterator((memlayout)->{
            currentOffset.set(this.computeAlignmentOffset(currentOffset.get(), memlayout.byteSizeElement()));            
            memlayout.offset = currentOffset.get();
            currentOffset.addAndGet(memlayout.byteSizeAggregate());  
        });        
        byteSize = computeAlignmentOffset(currentOffset.get(), fieldMaxBytes);
    }

    @Override
    public int byteSizeAggregate() {
        return byteSize;
    }

    @Override
    public LayoutMemory withId(String name) {
        LayoutMemory[] layoutMems = new LayoutMemory[fields.size()];
        int i = 0;
        for(String string : fields.keySet())
        {
            layoutMems[i] = fields.get(string);
            i++;
        }
            
        return createGroup(name, layoutMems);
    }

    @Override
    public String getId() {
        return name;
    }
    
    protected void layoutIterator(BiConsumer<Integer, LayoutMemory> consume)
    {
        int i = 0;
        for(String string : fields.keySet())
        {
            consume.accept(i, fields.get(string));
            i++;
        }
    }
    
    protected void layoutIterator(Consumer<LayoutMemory> consume)
    {       
        for(String string : fields.keySet())        
            consume.accept(fields.get(string));       
    }
    
    @Override
    public String toString()
    {
        int[] fieldOffsets = new int[fields.size()];
        layoutIterator((i, memlayout) -> fieldOffsets[i] = memlayout.offset());
        
        StringBuilder builder = new StringBuilder();
        builder.append("offsets : ").append(Arrays.toString(fieldOffsets)).append("\n");
        builder.append("size    : ").append(byteSizeAggregate());
        return builder.toString();
    }

}
