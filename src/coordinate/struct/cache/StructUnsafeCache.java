/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.cache;

import coordinate.struct.StructAbstractCache;
import coordinate.struct.structbyte.StructUnsafeMemory;
import coordinate.unsafe.UnsafeByteBuffer;
import java.nio.ByteBuffer;

/**
 *
 * @author user
 * @param <T>
 */
public class StructUnsafeCache <T extends StructUnsafeMemory> extends StructAbstractCache<T, UnsafeByteBuffer> {
    
    public StructUnsafeCache(Class<T> clazz, long size)
    {
        super(clazz, size);
    }

    @Override
    protected void initBuffer() {
        this.setBuffer(new UnsafeByteBuffer(struct.getByteSize() * size));
    }

    @Override
    public long getByteBufferSize() {
        return getBuffer().capacity();
    }
    
    @Override
    public long getAddress()
    {
         return this.getBuffer().getAddress();
    }
    
    @Override
    public boolean isNativeBuffer()
    {        
        return true;
    }

    @Override
    public ByteBuffer getByteBuffer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
