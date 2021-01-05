/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.refl;

import coordinate.list.IntList;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class ByteStructLayout {
    public int currentOffset = 0;
    public int currentByteSize = 0;
    
    public int maxByteSize = 0;
    
    public IntList byteSizeValues = new IntList();
    public IntList fieldOffsets = new IntList();
    
    public int[] getArrayFieldOffsets()
    {
        return fieldOffsets.trim();
    }
    
    public void calculateMaxByteSize()
    {
        maxByteSize = Arrays.stream(byteSizeValues.trim()).max().getAsInt();
    }
    
    public void translateOffset(int newOffset)
    {
        for(int i = 0; i<fieldOffsets.size(); i++)
        {
            fieldOffsets.set(i, fieldOffsets.get(i) + newOffset);
        }
    }
    
    public int getLastOffset()
    {
        return fieldOffsets.get(fieldOffsets.size()-1);
    }
}
