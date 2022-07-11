/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.unsafe;

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
        
    private final long address;
    private final long capacity;
    private long position;
    private long limit;
    
    
    public UnsafeByteBuffer(long capacity)
    {        
        this.capacity = capacity;
        this.address = UnsafeUtils.getUnsafe().allocateMemory(capacity);
        this.position = 0;
        this.limit = capacity;
    }
            
    public long remaining() {
        return limit - position;
    }

    public void put(byte b) {
        UnsafeUtils.getUnsafe().putByte(address + position, b);
        position++;
    }
    
    public UnsafeByteBuffer put(byte[] src)
    {
        for(int i = 0; i<src.length; i++)
            put(src[i]);
        return this;
    }

    public void putInt(int i) {
        UnsafeUtils.getUnsafe().putInt(address + position, i);
        position += 4;
    }
    
    public void putInt(int... values)
    {
        for(int i = 0; i<values.length; i++)
            putInt(values[i]);
    }

    public byte get() {
        byte b = UnsafeUtils.getUnsafe().getByte(address + position);
        position++;
        return b;
    }
    
    public UnsafeByteBuffer get(byte[] dst)
    {
        for(int i = 0; i<dst.length; i++)
            dst[i] = get();
        return this;
    }
    
    public int getInt() {
        int i = UnsafeUtils.getUnsafe().getInt(address + position);
        position += 4;
        return i;
    }
    
    public int[] getIntArray(int size)
    {
        int[] buffer = new int[size];
        for(int i = 0; i<size; i++)
            buffer[i] = getInt();
        return buffer;
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
    
    public long capacity()
    {
        return capacity;
    }
    
    public long getAddress()
    {
        return address;
    }
    
    public void freeMemory()
    {
        UnsafeUtils.getUnsafe().freeMemory(address);
    }
}
