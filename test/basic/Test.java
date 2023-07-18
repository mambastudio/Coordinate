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
        
        System.out.println(round_up(5.1f, 2));
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
    
    public static long round_up(float numToRound, long multiple) 
    {
        return ((long)numToRound + multiple - 1) & -multiple;
    }
}
