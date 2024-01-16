/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.type;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
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
public class LayoutGroup extends LayoutMemory{
    protected final LinkedHashMap<String, LayoutMemory> fields = new LinkedHashMap();  
   
    protected long byteSize;
    
    private final String name;
        
    private LayoutGroup(String name)
    {
        this.name = name;
    }
    
    public static LayoutGroup groupLayout(LayoutMemory... memoryLayouts)
    {
        return createGroup(UUID.randomUUID().toString(), memoryLayouts);
    }
    
    public static LayoutGroup groupValueLayout(LayoutMemory... memoryLayouts)
    {
        return createGroupValue(UUID.randomUUID().toString(), memoryLayouts);
    }
    
    private static LayoutGroup createGroup(String name, LayoutMemory... memoryLayouts)
    {
        LayoutGroup group = new LayoutGroup(name);
        for(LayoutMemory memoryLayout : memoryLayouts)        
            group.fields.put(memoryLayout.getId(), memoryLayout);    
        
        group.init();        
        return group;
    }
    
    private static LayoutGroup createGroupValue(String name, LayoutMemory... memoryLayouts)
    {
        LayoutGroupValue group = new LayoutGroupValue(name);
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
    
    protected void init()
    {
        calculateAlignValues();
        calculateFieldOffsets();    
    }
    
    /**
     * For structs, we find the maximum field value, to know the offset of fields and 
     * struct size calculation
     * 
     * @return 
     **/
    @Override
    public long calculateAlignValues()
    {
        layoutIterator((memlayout)->{
            long tempAlignValue = memlayout.calculateAlignValues();            
            alignValue = tempAlignValue > alignValue ? tempAlignValue : alignValue; //calculate max value
        });
        return alignValue;
    }
    
    protected void calculateFieldOffsets()
    {         
        AtomicLong currentOffset = new AtomicLong();      
        layoutIterator((memlayout)->{
            currentOffset.set(this.computeAlignmentOffset(currentOffset.get(), memlayout.calculateAlignValues()));            
            memlayout.offset = currentOffset.get();            
            currentOffset.addAndGet(memlayout.byteSizeAggregate());  
        });        
        byteSize = computeAlignmentOffset(currentOffset.get(), alignValue);
    }
    

    @Override
    public long byteSizeAggregate() {
        return byteSize;
    }

    @Override
    public LayoutMemory withId(String name) {
        LayoutMemory[] layoutMems = getFields();
        return createGroup(name, layoutMems);
    }
    
    protected LayoutMemory[] getFields()
    {
        LayoutMemory[] layoutMems = new LayoutMemory[fields.size()];
        int i = 0;
        for(String string : fields.keySet())
        {
            layoutMems[i] = fields.get(string);
            i++;
        }
        return layoutMems;
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
        fields.keySet().forEach(string -> {
            consume.accept(fields.get(string));       
        });
    }
    
    @Override
    public String toString()
    {
        int[] fieldOffsets = new int[fields.size()];
        layoutIterator((i, memlayout) -> fieldOffsets[i] = (int) memlayout.offset());
        
        StringBuilder builder = new StringBuilder();
        builder.append("offsets : ").append(Arrays.toString(fieldOffsets)).append("\n");
        builder.append("size    : ").append(byteSizeAggregate());
        return builder.toString();
    }
    
    //this accomodates the various native primitives such as in opencl (float4, float2, int2, etc.)
    public static class LayoutGroupValue extends LayoutGroup
    {
        private LayoutGroupValue(String name)
        {
            super(name);
        }  
        
        @Override
        protected void calculateFieldOffsets()
        {
            super.calculateFieldOffsets();
            //just requires tweaking here
            this.alignValue = this.byteSize; 
        }
        
        @Override
        public LayoutMemory withId(String name) {
            LayoutMemory[] layoutMems = getFields();
            return createGroupValue(name, layoutMems);
        }
    }
}
