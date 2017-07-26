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
public class Utility {
    public static float clamp(float x, float min, float max)
    {
        if (x > max)
            return max;
        if (x > min)
            return x;
        return min;
    }
    
    public static int clamp(int x, int min, int max)
    {
        if (x > max)
            return max;
        if (x > min)
            return x;
        return min;
    }

    public static int max(int a, int b, int c) 
    {
        if (a < b)
            a = b;
        if (a < c)
            a = c;
        return a;
    }

    public static float max(float a, float b, float c) 
    {
        if (a < b)
            a = b;
        if (a < c)
            a = c;
        return a;
    }
    
    public static float min(float a, float b, float c) {
        if (a > b)
            a = b;
        if (a > c)
            a = c;
        return a;
    }

    


    public static boolean isBad(float value)
    {
        if(Float.isInfinite(value))
            return true;
        else return Float.isNaN(value);
    }
    
    public static void debugValue(float value)
    {
        if(Float.isInfinite(value))
            System.out.println("Infinite value detected");
        else if(Float.isNaN(value))
            System.out.println("Nan value detected");
    }
    
    public static void debugArray(float... array)
    {
        for(float value : array)
        {
            debugValue(value);
        }
    }
    
    public static void debugArray(float[][] array)
    {
        for(int i = 0; i<array[i].length; i++)
            debugArray(array[i]);
    }
    
}
