/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.layout;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 *
 * @author user
 * 
 * Heavily inspired by Panama java. This actually avoids reflection and thus let's the user develop their own 
 * struct API independently
 * 
 */
public final class LayoutGroup extends LayoutMemory{
    private final LinkedHashMap<String, LayoutMemory> fields = new LinkedHashMap();
    private int[]   fieldByteSizes;
    private int[]   fieldOffsets;
    private int     fieldMaxBytes;
    
    private int groupSize;
    
    private final String name;
    
    private LayoutGroup()
    {
        this.name = UUID.randomUUID().toString();
    }
    
    private LayoutGroup(String name)
    {
        this.name = name;
    }
    
    public static LayoutGroup createGroup(LayoutMemory... memoryLayouts)
    {
        LayoutGroup group = new LayoutGroup();
        for(LayoutMemory memoryLayout : memoryLayouts)
            group.fields.put(memoryLayout.getId(), memoryLayout);
        
        group.init();
        
        return group;
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
        calculateFieldByteSizes();
        calculateFieldOffsets();    
    }
    
    private void calculateFieldByteSizes()
    {
        fieldByteSizes = new int[fields.size()];
        int i = 0;
        for(String string : fields.keySet())
        {
            fieldByteSizes[i] = fields.get(string).byteSize();
            if(fieldByteSizes[i] > fieldMaxBytes)
                fieldMaxBytes = fieldByteSizes[i];
            i++;
        }        
    }
    
    private void calculateFieldOffsets()
    {
        fieldOffsets = new int[fields.size()];
        int offset = 0;
        int i = 0;
        
        for(String string : fields.keySet())
        {                        
            LayoutMemory memoryLayout = fields.get(string);            
            offset = this.computeAlignmentOffset(offset, memoryLayout.byteSizeElement());            
            fieldOffsets[i] = offset;
            offset += memoryLayout.byteSize();    
            i++;
        }
        
        groupSize = computeAlignmentOffset(offset, fieldMaxBytes);
    }

    @Override
    public int byteSize() {
        return groupSize;
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
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("offsets : ").append(Arrays.toString(fieldOffsets)).append("\n");
        builder.append("size    : ").append(byteSize());
        return builder.toString();
    }
}
