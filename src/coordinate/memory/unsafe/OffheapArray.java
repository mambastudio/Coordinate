/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.unsafe;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;

/**
 *
 * @author user
 */
public class OffheapArray {
    private final static int BYTE = 1;
    private long size;
    private long address;

    public OffheapArray(long size)  {
        try {
            this.size = size;
            address = getUnsafe().allocateMemory(size * BYTE);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            Logger.getLogger(OffheapArray.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Unsafe getUnsafe() throws IllegalAccessException, NoSuchFieldException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }

    public void set(long i, byte value) throws NoSuchFieldException, IllegalAccessException {
        getUnsafe().putByte(address + i * BYTE, value);
    }

    public int get(long idx) throws NoSuchFieldException, IllegalAccessException {
        return getUnsafe().getByte(address + idx * BYTE);
    }

    public long size() {
        return size;
    }
    
    public void freeMemory()  {
        try {
            getUnsafe().freeMemory(address);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            Logger.getLogger(OffheapArray.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
