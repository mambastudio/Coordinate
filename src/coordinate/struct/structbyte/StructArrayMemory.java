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
public class StructArrayMemory extends StructAbstractMemory<byte[]> {

    @Override
    protected ByteBuffer getLocalByteFromGlobalBuffer(ByteOrder order) {
        if(this.getGlobalBuffer() == null)
            return null;           
        ByteBuffer buffer = ByteBuffer.wrap(getGlobalBuffer(), (int) getGlobalIndex(), getByteSize());
        return buffer.order(order);
    }

    @Override
    protected void setGlobalBufferFromFieldValues() {
        if(getGlobalBuffer() == null || getGlobalIndex() == -1) return;
        byte [] array = getFieldValuesAsArray();               
        System.arraycopy(array, 0, this.getGlobalBuffer(), (int) getGlobalIndex(), getByteSize());
    }

}
