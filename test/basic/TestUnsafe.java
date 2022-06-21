/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

/**
 *
 * @author jmburu
 */
public class TestUnsafe {
    public static void main(String... string)
    {
        UnsafeByteBuffer buffer = new UnsafeByteBuffer((long)Integer.MAX_VALUE * 3);
        buffer.freeMemory();
    }
}
