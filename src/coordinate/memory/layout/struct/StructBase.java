/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.layout.struct;

import coordinate.memory.layout.LayoutGroup;
import coordinate.memory.layout.LayoutMemory.PathElement;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 *
 * @author user
 * @param <Struct>
 */
public abstract class StructBase<Struct extends StructBase<Struct>> {
    
    protected final LayoutGroup layout;
    protected final ByteBuffer buffer;
    
    public StructBase()
    {
        layout = initLayout();
        buffer = ByteBuffer.allocate(layout.byteSizeAggregate());
    }
    
    public final int sizeOf()
    {
        return getLayout().byteSizeAggregate();
    }
    
    public final byte[] getBytes()
    {
        toBuffer();
        return buffer.array();
    }
    
    public final void putBytes(byte[] bytes)
    {
        Objects.requireNonNull(bytes);
        if(bytes.length == buffer.capacity())
        {
            buffer.clear();
            buffer.put(bytes);
            fromBuffer();
        }
        else
            throw new UnsupportedOperationException("bytes and buffer not equal");
    }
    
    public abstract void toBuffer();
    public abstract void fromBuffer();
    
    public abstract Struct newInstance();
    public abstract Struct copy();
    
    protected abstract LayoutGroup initLayout();   
    protected final LayoutGroup getLayout()
    {
        return layout;
    }
        
    public ValueState valueState(PathElement... elements)
    {
        return getLayout().valueState(buffer, elements);
    }
}
