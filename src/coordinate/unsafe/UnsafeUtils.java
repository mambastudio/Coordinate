/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.unsafe;

import com.sun.jna.Memory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

/**
 *
 * @author user
 */
public class UnsafeUtils {        
    
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
        
    public static ByteBuffer allocatDirectBufferJNA(long cap)
    {
        Memory m = new Memory(cap);
        ByteBuffer buf = m.getByteBuffer(0, m.size()).order(ByteOrder.nativeOrder());
        return buf;
    }
    
    //https://www.sobyte.net/post/2021-10/unsafe-bytebuffer/
    public static ByteBuffer allocateLightDirectBuffer(int cap)
    {
        try {
            Field capacityField = Buffer.class.getDeclaredField("capacity");
            capacityField.setAccessible(true);
            Field addressField = Buffer.class.getDeclaredField("address");
            addressField.setAccessible(true);
            Unsafe unsafe = getUnsafe();
            long address = unsafe.allocateMemory(cap);
            
            //ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());
            unsafe.freeMemory(((DirectBuffer) byteBuffer).address());
            
            addressField.setLong(byteBuffer, address);
            capacityField.setInt(byteBuffer, cap);
                                   
            byteBuffer.clear();
            
           
    
            return byteBuffer;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(UnsafeUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void clean(Buffer bb) {
        if(bb == null) return;
        if(!(bb instanceof ByteBuffer)) return;
        ByteBuffer buf = (ByteBuffer) bb;
        if(!buf.isDirect()) return;
        getUnsafe().freeMemory(((DirectBuffer) buf).address());
        
    }
    
    public static void destroyDirectByteBuffer(ByteBuffer toBeDestroyed) {
        Runnable run = ()->{
        try {
            Method cleanerMethod = toBeDestroyed.getClass().getMethod("cleaner");
            cleanerMethod.setAccessible(true);
            Object cleaner = cleanerMethod.invoke(toBeDestroyed);
            Method cleanMethod = cleaner.getClass().getMethod("clean");
            cleanMethod.setAccessible(true);
            cleanMethod.invoke(cleaner);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(UnsafeUtils.class.getName()).log(Level.SEVERE, null, ex);
        }};
        new Thread(run).start(); 
    }
    
    public static long toAddress(Object obj) {
        Object[] array = new Object[] {obj};
        long baseOffset = getUnsafe().arrayBaseOffset(Object[].class);
        return normalize(getUnsafe().getInt(array, baseOffset));
    }

    public static Object fromAddress(long address) {
        Object[] array = new Object[] {null};
        long baseOffset = getUnsafe().arrayBaseOffset(Object[].class);
        getUnsafe().putLong(array, baseOffset, address);
        return array[0];
    }
    
    public static void copyMemory(Object src, long srcAddress, Object dest, long destAddress, long length)
    {
        long bytesToCopy = length - (length % 8); 
        getUnsafe().copyMemory(src, srcAddress, dest, destAddress, bytesToCopy);
        getUnsafe().copyMemory(src, srcAddress + bytesToCopy, dest, destAddress + bytesToCopy, length - bytesToCopy);
    }
}
