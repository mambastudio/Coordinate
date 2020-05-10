/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

/**
 *
 * @author user
 */
public class Test5 {
    public static void main(String... args)
    {
        int value = -3;
        method1(value);
        method2(value);
    }
    
    //negative values are treated as zero
    public static void method1(int value)
    {        
        int x = (value >> 31) - (-value >> 31);
        int m = ((x-0) >> 31);
        int r =  (m & 0) + ((~m) & x);
        System.out.println(r);
    }
    //negative values are treated as one
    public static void method2(int value)
    {
        int x = (value >> 31) - (-value >> 31);
        int m = (x >> 31);
        int a = (x ^ m) - m;
        System.out.println(a);
    }
}
