/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memlayout;

/**
 *
 * @author jmburu
 * 
 * https://courses.cs.washington.edu/courses/cse351/17sp/lectures/CSE351-L14-structs_17sp-ink-day2.pdf
 */
public interface LayoutMemory {
    
    public int byteSize();
    public LayoutMemory withId(String name);
    public String getId();
    
    /**
     * Align is the size of data (e.g. 4-byte data -> int, float), and offset is starting point 
     * or reference starting point.
     * 
     * The returned value is the new starting point or offset of the align data.
     * 
     * https://en.wikipedia.org/wiki/Data_structure_alignment
     * 
     * @param offset
     * @param align
     * @return 
     */
    default int computeAlignmentOffset(int offset, int align)
    {
        return (offset + align - 1) & -align;
    }  
}
