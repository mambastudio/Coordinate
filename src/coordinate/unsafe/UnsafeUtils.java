/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

/**
 *
 * @author user
 */
public class UnsafeUtils {    
    
    private final static int INTSIZE = 4;
    private final static int FLOATSIZE = 4;
    private final static int SHORTSIZE = 2;
    private final static int LONGSIZE = 8;
    private final static int DOUBLESIZE = 8;
    
    
    
    private static Unsafe unsafe = null;
    
    private static Unsafe createUnsafe() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }
    
    public static long addressOfDirectBuffer(ByteBuffer buffer)
    {
        return ((DirectBuffer) buffer).address();
    }
        
    public static Unsafe getUnsafe()
    {
        if(unsafe == null)
            try {
                unsafe = createUnsafe();
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(UnsafeUtils.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return unsafe;
    }
    
    //normalize is a method for casting signed int to unsigned long, for correct address usage.
    private static long normalize(int value) {
        if(value >= 0) return value;
        return (~0L >>> 32) & value;
    }
    
    public static long sizeOf(Object object){
        return getUnsafe().getAddress(
            normalize(getUnsafe().getInt(object, 4L)) + 12L);
    }
    
    public static long getIntCapacity(long capacity)
    {
        return capacity * INTSIZE;
    }
    
    public static long getLongCapacity(long capacity)
    {
        return capacity * LONGSIZE;
    }
    
    public static long getFloatCapacity(long capacity)
    {
        return capacity * FLOATSIZE;
    }
    
    public static long getDoubleCapacity(long capacity)
    {
        return capacity * DOUBLESIZE;
    }
    
    public static long getShortCapacity(long capacity)
    {
        return capacity * SHORTSIZE;
    }
    
}
