/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

/**
 *
 * @author jmburu
 * 
 * https://www.sobyte.net/post/2021-10/unsafe-bytebuffer/
 * http://mishadoff.com/blog/java-magic-part-4-sun-dot-misc-dot-unsafe/
 * https://www.baeldung.com/java-unsafe
 * 
 */
public class UnsafeByteBuffer {
    private Unsafe unsafe;
    
    private final long address;
    private final long capacity;
    private long position;
    private long limit;
    
    public UnsafeByteBuffer(long capacity)
    {
        try {
            createUnsafe();
            
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(UnsafeByteBuffer.class.getName()).log(Level.SEVERE, null, ex);
        }        
        this.capacity = capacity;
        this.address = unsafe.allocateMemory(capacity);
        this.position = 0;
        this.limit = capacity;
    }
    
    public UnsafeByteBuffer(int capacity)
    {
        this((long)capacity);
    }
    
    private void createUnsafe() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        unsafe = (Unsafe) f.get(null);
    }
    
    public long remaining() {
        return limit - position;
    }

    public void put(ByteBuffer heapBuffer) {
        int remaining = heapBuffer.remaining();
        //copyMemory(Object src, long srcAddress, Object dest, long destAddress, int length)
        unsafe.copyMemory(heapBuffer.array(), 16, null, address + position, remaining);
        position += remaining;
    }

    public void put(byte b) {
        unsafe.putByte(address + position, b);
        position++;
    }

    public void putInt(int i) {
        unsafe.putInt(address + position, i);
        position += 4;
    }

    public byte get() {
        byte b = unsafe.getByte(address + position);
        position++;
        return b;
    }
    
    public ByteBuffer getHeap(byte[] src)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(src.length);
        long laddress = ((DirectBuffer)byteBuffer).address();
        unsafe.copyMemory(src, 16, null, laddress, src.length);
        return ByteBuffer.wrap(src);
    }

    public int getInt() {
        int i = unsafe.getInt(address + position);
        position += 4;
        return i;
    }

    public long position() {
        return position;
    }

    public void position(long position) {
        this.position = position;
    }

    public void limit(long limit) {
        this.limit = limit;
    }

    public void flip() {
        limit = position;
        position = 0;
    }

    public void clear() {
        position = 0;
        limit = capacity;
    }
    
    public long getAddress()
    {
        return address;
    }
    
    public void freeMemory()
    {
        unsafe.freeMemory(address);
    }
}
