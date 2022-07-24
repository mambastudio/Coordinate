/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <MemoryType>
 * @param <GlobalBuffer>
 */
public abstract class StructAbstractCache<MemoryType extends StructAbstractMemory<GlobalBuffer>, GlobalBuffer>{
    private final Class<MemoryType> clazz;
    private GlobalBuffer buffer;
    protected final long size;
    
    protected final MemoryType struct;
    
       
    protected StructAbstractCache(Class<MemoryType> clazz, long size)
    {       
        this.clazz = clazz;
        this.struct = getObject(clazz);      
        this.size = size;   
        this.initBuffer();
    }
    
    //called by this constructor
    protected abstract void initBuffer();    
    public abstract long getByteBufferSize();    
    public abstract ByteBuffer getByteBuffer();
    
    
    public long getAddress()
    {
        return 0;
    }
    
    public boolean isNativeBuffer()
    {        
        return false;
    }
    
    protected void setBuffer(GlobalBuffer buffer)
    {
        this.buffer = buffer;
    }
    
    protected GlobalBuffer getBuffer()
    {
        return buffer;
    }
        
    public long size()
    {
        return size;
    }
    
    public MemoryType get(long index)
    {
        if(index >= size())
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds. Size is " +size+ "!");
        MemoryType t = getObject(clazz);
        t.setGlobal(buffer, index * struct.getByteSize());
        t.setFieldValuesFromGlobalBuffer();
        return t;
    }
    
    public void initFrom(MemoryType t, int index)
    {        
        t.setGlobal(buffer, index * struct.getByteSize());
        t.setFieldValuesFromGlobalBuffer();
    }
    
    public void setStruct(MemoryType t, int index)
    {
        t.setGlobal(buffer, index * struct.getByteSize());
        t.setGlobalBufferFromFieldValues();        
    }

    
    private MemoryType getObject(Class<MemoryType> clazz)
    {
        try {
            MemoryType t = clazz.newInstance();
            return t;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(StructAbstractCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
