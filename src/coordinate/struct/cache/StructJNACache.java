/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.cache;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import coordinate.struct.StructAbstractCache;
import coordinate.struct.structbyte.StructBufferMemory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author user
 * @param <T>
 */
public class StructJNACache<T extends StructBufferMemory> extends StructAbstractCache<T, ByteBuffer> {
    private Memory m;
    
    public StructJNACache(Class<T> clazz, long size)
    {
        super(clazz, size);
        
    }
    
    @Override
    protected void initBuffer() {
        m = new Memory(struct.getByteSize() * size); //investigate why this fails in the constructor       
        ByteBuffer buf = m.getByteBuffer(0, m.size()).order(ByteOrder.nativeOrder());   
        System.out.println("byte size " +this.struct.getByteSize()+ struct.getClass().getSimpleName());
        this.setBuffer(buf);
    }

    @Override
    public long getByteBufferSize() {
        return getBuffer().capacity();
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return this.getBuffer();
    }
    
    @Override
    public boolean isNativeBuffer()
    {        
        return true;
    }
    
    @Override
    public long getAddress()
    {
        return Pointer.nativeValue(m.getPointer(0));
    }
    
    public Memory getMemory()
    {
        return m;
    }
}
