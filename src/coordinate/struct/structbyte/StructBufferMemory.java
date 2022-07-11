/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct.structbyte;

import coordinate.struct.StructAbstractMemory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author user
 */
public class StructBufferMemory extends StructAbstractMemory<ByteBuffer>{

    @Override
    protected ByteBuffer getLocalByteFromGlobalBuffer(ByteOrder order) {
        getGlobalBuffer().position((int) getGlobalIndex());
        byte[] array = new byte[getByteSize()];
        getGlobalBuffer().get(array);
        ByteBuffer buffer = ByteBuffer.wrap(array).order(order);
        return buffer;
    }

    @Override
    protected void setGlobalBufferFromFieldValues() {
        if(getGlobalBuffer() == null || getGlobalIndex() == -1) return;
        byte [] array = getFieldValuesAsArray();          
        getGlobalBuffer().position((int) getGlobalIndex());
        getGlobalBuffer().put(array);        
    }

   
}
