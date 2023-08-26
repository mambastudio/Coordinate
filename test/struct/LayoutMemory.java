/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package struct;

/**
 *
 * @author jmburu
 */
public interface LayoutMemory {
    
    public int byteSize();
    
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
