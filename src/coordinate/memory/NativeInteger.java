/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory;

import static coordinate.unsafe.UnsafeUtils.getUnsafe;

/**
 *
 * @author jmburu
 */
public class NativeInteger extends MemoryAddress<NativeInteger, int[]>{

    public NativeInteger(NativeInteger pointer) {
        super(pointer);
    }

    public NativeInteger(long size)
    {
        super(size);
    }

    public NativeInteger(NativeInteger pointer, long offset) {
        super(pointer, offset);
    }
     
    @Override
    public void dispose()
    {
        System.out.println("native array garbage collected");
        getUnsafe().freeMemory(address());
    }
    
    public void set(long offset, int value)
    {
        rangeCheck(offset, capacity());
        getUnsafe().putInt(address() + toAmountBytes(offset), value);
    }
    
    public int get(long offset)
    {
        rangeCheck(offset, capacity());
        return getUnsafe().getInt(address() + toAmountBytes(offset));
    }
    
    @Override
    public int sizeOf() {
        return 4;
    }

    @Override
    public NativeInteger getMemory(long offset) {
        rangeCheck(offset, capacity());
        return new NativeInteger(this, offset);
    }
}
