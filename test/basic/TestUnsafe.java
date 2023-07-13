/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;


import coordinate.unsafe.UnsafeByteBuffer;
import coordinate.unsafe.UnsafeUtils;
import java.nio.IntBuffer;
import java.util.Arrays;
import coordinate.memory.NativeInteger;

/**
 *
 * @author jmburu
 */
public class TestUnsafe {
    public static void main(String... string)
    {
        test2();
    }
    
    public static void test1()
    {
        UnsafeByteBuffer ubuffer = new UnsafeByteBuffer(UnsafeUtils.getIntCapacity(5));
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
        n1.put(src1, 0);
        
        NativeInteger n2 = new NativeInteger(5);
        n2.put(src2, 0);
        
        n1.swap(n2);
        
        IntBuffer buf = n1.getDirectIntBuffer(0, 5).asIntBuffer();
        int[] bufArr = new int[5];
        buf.get(bufArr);
        System.out.println(Arrays.toString(bufArr));
        
        

    }
}
