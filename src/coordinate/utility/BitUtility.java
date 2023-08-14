/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

/**
 *
 * @author user
 */
public class BitUtility {
    public static int int_mask()
    {
        //bit count is always 32
        return -1;
    }
    public static int int_mask(int size)
    {
        checkSizeInRange32(size);
        return (size == 32) ? -1 : (1 << size) - 1;
    }
    
    public static int int_mask_until(int index)
    {
        checkIndexInRange32(index);
        return (index == 31) ? -1 : (1 << (index + 1)) - 1;
    }
    
    //how rgb color works
    public static int apply_bits_at(int index, int value, int output)
    {
        checkIndexInRange32(index);
        return output | value << index;        
    }
    
    //how rgb color works
    public static int get_bits_at(int index, int value, int size)
    {
        checkIndexInRange32(index);
        return (value >> index) & int_mask(size);
    }
    
    public static int get_bits_from(int index, int value)
    {
        checkIndexInRange32(index);
        return get_bits_at(index, value, 32 - index);
    }
    
    private static void checkIndexInRange32(int index)
    {       
        if(index < 0 || index > 31)
            throw new UnsupportedOperationException("index is out of range");
    }
    
    private static void checkSizeInRange32(int size)
    {
        if(size < 1 || size > 32)
            throw new UnsupportedOperationException("index is out of range");
    }
    
    //log2
    private static long log2(long x )
    {
        if( x == 0 )
            throw new UnsupportedOperationException("value should be zero");
        return 63 - Long.numberOfLeadingZeros( x );
    }
    
    //https://jameshfisher.com/2018/03/30/round-up-power-2/ 
    private static long next_pow2(long x)
    {                
        return x == 1 ? 1 : 1<<(64 - Long.numberOfLeadingZeros(x-1));
    }
    
    public static long next_log2(long size)
    {
        return log2(next_pow2(size));        
    }
        
    public static long previous_multipleof(long numToRound, long multiple) 
    {
        return next_multipleof(numToRound, multiple) - multiple;
    }
    
    //https://stackoverflow.com/questions/3407012/rounding-up-to-the-nearest-multiple-of-a-number
    public static long next_multipleof(long numToRound, long multiple)
    {
        if(multiple == 0 )
            throw new UnsupportedOperationException("multiple should not be zero");
        return ((numToRound + multiple - 1) / multiple) * multiple;
    }
}
