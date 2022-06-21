/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import sun.misc.Unsafe;

/**
 *
 * @author jmburu
 */
public class UnsafeUtil 
{
    //normalize is a method for casting signed int to unsigned long, for correct address usage
    private static long normalize(int value) 
    {
        if(value >= 0) return value;
        return (~0L >>> 32) & value;
    }
    
    public static long toAddress(Unsafe unsafe, Object obj) 
    {
        Object[] array = new Object[] {obj};
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        return normalize(unsafe.getInt(array, baseOffset));
    }

    public static Object fromAddress(Unsafe unsafe, long address) {
        Object[] array = new Object[] {null};
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        unsafe.putLong(array, baseOffset, address);
        return array[0];
    }
}
