/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.cache;

import coordinate.struct.StructAbstractCache;
import coordinate.struct.structbyte.StructBufferMemory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author user
 * @param <T>
 */
public class StructBufferCache<T extends StructBufferMemory> extends StructAbstractCache<T, ByteBuffer> {
    
    public StructBufferCache(Class<T> clazz, int size)
    {
        super(clazz, size);
    }

    @Override
    protected void initBuffer() {
        this.setBuffer(ByteBuffer.allocate(struct.getByteSize() * (int)size).order(ByteOrder.nativeOrder()));
    }

    @Override
    public long getByteBufferSize() {
        return getBuffer().capacity();
    }

    @Override
    public boolean canGetByteBuffer() {
        return true;
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return getBuffer();
    }
}
