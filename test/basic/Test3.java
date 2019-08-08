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
public class Test3 {
    public static void main(String... args)
    {
        /*
        int decimal = Integer.parseInt("1111111111111111", 2);
        System.out.println(Integer.bitCount(decimal));
        System.out.println(Integer.toString(decimal, 16));
        System.out.println(decimal);
        
        int value = 0;
        value = value | 334;
        value = value | (34242 << 16);
        
        System.out.println(value & 0xFFFF);
        System.out.println((value >> 16) & 0xFFFF);
        */
        int value = 0;
        value = setBoolean(value, true);
        System.out.println(getBoolean(value));
        value = setPower(value, 30000);
        System.out.println(getPower(value));
        
        
    }
    
    public static int setPower(int value, int power)
    {
        return value | (power << 1);
    }
    
    public static int getPower(int value)
    {
        return value >> 1;
    }
    
    public static int setBoolean(int value, boolean bvalue)
    {
        return value = value | (bvalue ? 1 : 0);
    }
    
    public static boolean getBoolean(int value)
    {
        return (value & 1) == 1;
    }    
}
