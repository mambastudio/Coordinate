/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.cache;

import coordinate.struct.StructAbstractCache;
import coordinate.struct.structbyte.StructArrayMemory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author user
 * @param <T>
 */
public class StructArrayCache<T extends StructArrayMemory> extends StructAbstractCache<T, byte[]> {
    
    public StructArrayCache(Class<T> clazz, int size)
    {
        super(clazz, size);
    }

    @Override
    protected void initBuffer() {
        this.setBuffer(new byte[struct.getByteSize() * (int)size]);
    }

    @Override
    public long getByteBufferSize() {
        return getBuffer().length;
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return ByteBuffer.wrap(getBuffer()).order(ByteOrder.nativeOrder());
    }
    
}
