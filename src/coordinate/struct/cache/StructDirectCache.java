/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.cache;

import coordinate.struct.StructAbstractCache;
import coordinate.struct.structbyte.StructBufferMemory;
import coordinate.unsafe.UnsafeUtils;
import java.nio.ByteBuffer;

/**
 *
 * @author user
 * @param <T>
 */
public class StructDirectCache <T extends StructBufferMemory> extends StructAbstractCache<T, ByteBuffer> {
    
    public StructDirectCache(Class<T> clazz, int size)
    {
        super(clazz, size);
    }

    @Override
    protected void initBuffer() {
        this.setBuffer(ByteBuffer.allocateDirect(struct.getByteSize() * (int)size));
    }

    @Override
    public long getByteBufferSize() {
        return getBuffer().capacity();
    }
    
    @Override
    public long getAddress()
    {        
        return UnsafeUtils.addressOfDirectBuffer(this.getBuffer());
    }
    
    @Override
    public boolean isNativeBuffer()
    {        
        return true;
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return getBuffer();
    }
}
