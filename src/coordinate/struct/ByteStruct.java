/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author user
 */
public abstract class ByteStruct {
    
    private byte[] globalArray = null;
    private int globalArrayIndex = -1;
    private int[] offsets;
    
    protected final int getGlobalArrayIndex()
    {
        return globalArrayIndex;
    }
    
    protected final void setGlobalArray(byte[] globalArray, int index)
    {
        this.globalArray = globalArray;
        this.globalArrayIndex = index * getSize(); 
    }
    
    protected void setOffsets(int[] offsets)
    {
        this.offsets = offsets;
    }
    
    protected int[] getOffsets()
    {
        return offsets;
    }
    
    protected final void refreshGlobalArray()
    {
        if(globalArray == null || globalArrayIndex == -1) return;
        byte [] array = getArray();               
        System.arraycopy(array, 0, globalArray, globalArrayIndex, getSize());
        
    }
    
    public final void skipByte(ByteBuffer bb, int skip) {
        bb.position( bb.position() + skip);
    }
     
    protected final ByteBuffer getLocalByteBuffer(ByteOrder order)
    {
        if(globalArray == null)
            return null;        
        ByteBuffer buffer = ByteBuffer.wrap(globalArray, globalArrayIndex, getSize());
        return buffer.order(order);
    }
    
    protected final ByteBuffer getEmptyLocalByteBuffer(ByteOrder order)
    {
        byte[] array = new byte[getSize()];
        ByteBuffer buffer = ByteBuffer.wrap(array);
        return buffer.order(order);
    }
    
    public int getSize()
    {        
        return offsets[offsets.length - 1];
    }
    
    public abstract void  initFromGlobalArray();
    public abstract byte [] getArray();      
    
    public final byte[] getGlobalArray()
    {
        return globalArray;
    }
}
