/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;


import coordinate.unsafe.UnsafeByteBuffer;
import coordinate.unsafe.UnsafeUtils;
import java.util.Arrays;

/**
 *
 * @author jmburu
 */
public class TestUnsafe {
    public static void main(String... string)
    {
        UnsafeByteBuffer ubuffer = new UnsafeByteBuffer(UnsafeUtils.getIntCapacity(5));
        ubuffer.putInt(3, 5, 89, 34, 8);        
        ubuffer.position(0);        
        System.out.println(Arrays.toString(ubuffer.getIntArray(5)));
        
       
        
        ubuffer.freeMemory();
    }
}
