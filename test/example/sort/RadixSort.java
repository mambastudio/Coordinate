/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.sort;

import java.util.Arrays;

/**
 *
 * @author user
 */
public class RadixSort {
    
    public static void main(String... args)
    {
        int[] array = new int[]{
            301989888,
            306783378,
            613566756,
            153391689,
            603979776,
            150994945,
            301989888,
            306783378,
            613566756,
            153391689,
            603979776,
            150994944
        };
        System.out.println(Arrays.toString(array));
        sort(array);
        System.out.println(Arrays.toString(array));
    }
    public static void sort(int... array)
    {
        int[] tempArray = new int[array.length];
        int bitsPerPass = 6;
        int nBits = 30;
        int nPasses = nBits/bitsPerPass;
        
        for(int pass = 0; pass < nPasses; ++pass)
        {
            int lowBit = pass * bitsPerPass;
            
            int[] in  = ((pass & 1) == 1) ? tempArray : array;
            int[] out = ((pass & 1) == 1) ? array : tempArray;
            
            int nBuckets = 1 << bitsPerPass;
            int bucketCount[] = new int[nBuckets];
            int bitMask = (1 << bitsPerPass) - 1;
            
            for(int p : in)
            {
                int bucket = (p >> lowBit) & bitMask; //(object.value >> lowBit) & bitMask
                ++bucketCount[bucket];
            }
            
            int[] outIndex = new int[nBuckets];
            for(int i = 1; i < nBuckets; ++i)
                outIndex[i] = outIndex[i - 1] + bucketCount[i-1];
            
            for(int p : in)
            {
                int bucket = (p >> lowBit) & bitMask;
                out[outIndex[bucket]++] = p;
            }
        }
        
        if((nPasses & 1) == 1) System.arraycopy(tempArray, 0, array, 0, tempArray.length);
    }
}
