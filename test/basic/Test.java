/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.unsafe.UnsafeUtils;
import java.nio.ByteBuffer;

/**
 *
 * @author user
 */
public class Test {
    enum Josto{JOE, MWANGI}
    public static void main(String... args)
    {
        long n = 8;
        System.out.println("size:  " +n);
        System.out.println("pow 2: " +next_log2(n));
        System.out.println(log2(n));
        
        System.out.println(next_multipleof(18, 12));
        System.out.println(next_multipleof(1, 12));
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
    
    private static long next_log2(long size)
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
