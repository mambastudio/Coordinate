/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;


import coordinate.unsafe.NativeIntArray;
import coordinate.unsafe.UnsafeByteBuffer;
import coordinate.unsafe.UnsafeUtils;
import java.nio.IntBuffer;
import java.util.Arrays;

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
        NativeIntArray array = new NativeIntArray(5);
        int[] src = new int[]{1, 2, 3, 4, 5};
        array.copyFrom(src, 0);
        
        int[] dst = new int[5];
        array.copyTo(dst, 0);
        
        IntBuffer buf = array.getDirectIntBuffer(1, 4);
        int[] bufArr = new int[4];
        buf.get(bufArr);
        System.out.println(Arrays.toString(bufArr));
        
        
        System.out.println(Arrays.toString(dst));
    }
}
