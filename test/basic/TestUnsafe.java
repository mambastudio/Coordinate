/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;


import coordinate.unsafe.UnsafeByteBuffer;
import java.util.Arrays;
import coordinate.memory.NativeInteger;
import coordinate.memory.NativeObject;
import coordinate.memory.NativeObject.Element;
import coordinate.memory.functions.ParallelNative;
import coordinate.memory.functions.SerialNative;
import java.nio.ByteBuffer;

/**
 *
 * @author jmburu
 */
public class TestUnsafe {
    public static void main(String... string)
    {
        test8();
    }
    
    public static void test1()
    {
        UnsafeByteBuffer ubuffer = new UnsafeByteBuffer(5 * 4);
        ubuffer.putInt(3, 5, 89, 34, 8);        
        ubuffer.position(0);        
        System.out.println(Arrays.toString(ubuffer.getIntArray(5)));
        
        ubuffer.freeMemory();
    }
    
    public static void test2()
    {
        
        int[] src1 = new int[]{1, 2, 3, 4, 5};
        int[] src2 = new int[]{5, 4, 3, 2, 1};
        
        NativeInteger n1 = new NativeInteger(5);
        n1.copyFrom(src1, 0);
        
        NativeInteger n2 = new NativeInteger(5);
        n2.copyFrom(src2, 0);
        
        n1.swap(n2);
     
        System.out.println(n1);
    }
    
    //swap
    public static void test3()
    {
        NativeInteger n = new NativeInteger(13).fill(4);   
        NativeInteger n1 = n.offsetMemory(5).fill(5);
        System.out.println(n);
        System.out.println(n1);
        NativeInteger n2 = new NativeInteger(8).fill(2);
        n1.swap(n2);
        System.out.println(n);
        System.out.println(n1);
        System.out.println(n2);
    }
    
    //scan/prefixsum
    public static void test4()
    {
        NativeInteger n = new NativeInteger(10).fillRandomRange(0, 5);
        System.out.println(n);
        int value = SerialNative.reduce(n);
        System.out.println(value);
        NativeInteger scanned = new NativeInteger(10);
        SerialNative.exclusiveScan(n, scanned);
        System.out.println(scanned);
    }
    
    //copy 
    public static void test5()
    {
        NativeInteger n = new NativeInteger(10).fillRandomRange(0, 5);
        System.out.println(n);
        NativeInteger copy = n.copy();
        System.out.println(copy);
    }
    
    //partition
    public static void test6()
    {
        NativeInteger n = new NativeInteger(10).fillRandomRange(0, 5);
        System.out.println(n);      
        NativeInteger flags = new NativeInteger(10).fillRandomRange(0, 1);
        System.out.println(flags);
        NativeInteger output = new NativeInteger(10);
        ParallelNative.partition(n, output, 10, flags);
        System.out.println(output);
        
    }
    
    //sort
    public static void test7()
    {
        NativeInteger n = new NativeInteger(10).fillRandomRange(0, 5);
        System.out.println(n);
        ParallelNative.sort(n, (a, b) -> a > b);
        System.out.println(n);
    }
    
    public static void test8()
    {
        NativeObject<Josto> n = new NativeObject(Josto.class, 3);
        n.set(0, new Josto(3, 3));
        n.set(1, new Josto(13, 25));
        n.set(2, new Josto());
        Josto josto = n.get(1);
        System.out.println(n);
    }
    
    public static class Josto implements Element<Josto>
    {
        int a, b;
        
        public Josto()
        {
            a = b = 0;
        }
        
        public Josto(int a, int b)
        {
            this.a = a; this.b = b;
        }
        
        @Override
        public int sizeOf() {
            return 8;
        }

        @Override
        public byte[] getBytes() {
            ByteBuffer buf = ByteBuffer.allocate(sizeOf());
            buf.putInt(a);
            buf.putInt(b);
            return buf.array();
        }

        @Override
        public void putBytes(byte[] bytes) {
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            a = buf.getInt();
            b = buf.getInt();
        }

        @Override
        public Josto newInstance() {
            return new Josto();
        }
        
        @Override
        public final String toString() {
            return String.format("(%3d, %3d)", a, b);
        }
    }
}
