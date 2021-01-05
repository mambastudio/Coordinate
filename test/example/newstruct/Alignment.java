/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.newstruct;

import coordinate.list.IntList;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * https://courses.cs.washington.edu/courses/cse351/17sp/lectures/CSE351-L14-structs_17sp-ink-day2.pdf
 * 
 * @author user
 */
public class Alignment {    
    private final IntList offsets;
    private final IntList bytesizes;
    private final IntList fieldsizes;
            
    public Alignment()
    {
        offsets = new IntList();
        bytesizes = new IntList();
        fieldsizes = new IntList();
    }
    
    public void addOffsets(int offset)
    {
        offsets.add(offset);
    }
    
    public void addOffsets(int translate, int... offsets)
    {
        for(int offset : offsets)
            this.offsets.add(translate + offset);
    }
    
    public void addByteSizes(int... bytesizes)
    {
        this.bytesizes.add(bytesizes);
    }
    
    public void addFieldSizes(int fieldsizes)
    {
        this.fieldsizes.add(fieldsizes);
    }
    
    public int[] getOffsets()
    {
        return offsets.trim();
    }
    
    public int[] getByteSizes()
    {
        return bytesizes.trim();
    }
    
    public int[] getFieldSizes()
    {
        return fieldsizes.trim();
    }
    
    public int getLastIndex()
    {
        return offsets.get(offsets.size()-1) + bytesizes.get(bytesizes.size()-1);
    }
    
    public int getSize()
    {
        return (getLastIndex() + getMaxBytes() - 1) & ~(getMaxBytes() - 1);        
    }
    
    public void addAlignment(Alignment alignment)
    {
        this.offsets.add(alignment.getOffsets());
        this.bytesizes.add(alignment.getByteSizes());
        this.fieldsizes.add(alignment.getFieldSizes());
    }
    
    public int getMaxBytes()
    {
        return Arrays.stream(bytesizes.trim()).max().getAsInt(); 
    }
    
    public int getFieldMaxBytes()
    {
        return Arrays.stream(fieldsizes.trim()).max().getAsInt(); 
    }    
}
