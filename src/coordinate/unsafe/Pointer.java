/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.unsafe;

import static coordinate.unsafe.UnsafeUtils.getIntCapacity;
import static coordinate.unsafe.UnsafeUtils.getUnsafe;
import coordinate.utility.Sweeper;
import java.lang.reflect.Field;
import java.nio.Buffer;

/**
 *
 * @author user
 */
public abstract class Pointer {
    
    protected final long address;
    protected final long size;    
    
    //buffer fields
    protected static final Field addressField, capacityField;
    static {
    try {
            addressField = Buffer.class.getDeclaredField("address");
            addressField.setAccessible(true);
            capacityField = Buffer.class.getDeclaredField("capacity");
            capacityField.setAccessible(true);

        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }
    
    public Pointer(long size)
    {
        this.size = size;
        address = getUnsafe().allocateMemory(getIntCapacity(size));
        
        initSweeper();
    }
    
    private void initSweeper()
    {
        //For garbage collection
        Sweeper.getSweeper().register(this, ()->dispose());
    }
    
    public abstract void dispose();
}
