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
    public static final float PI_F = (float)Math.PI;
    public static final float HALF_PI_F = (float)PI_F/2f;
    public static final float PI_F_TWO = PI_F * PI_F;
    public static final float TWO_PI_F = 2 * PI_F;
    public static final float INV_PI_F = 1.f/PI_F;
    public static final float INV_TWO_PI_F = 1.f/ (2 * PI_F);
    public static final float EPS_COSINE = 1e-6f;
    public static final float EPS_PHONG = 1e-3f;
    public static final float EPS_RAY  =  0.000001f;
    
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
    
    public static float toDegrees(double radians)
    {
        return (float)Math.toDegrees(radians);
    }
    
    public static float toRadians(double degrees)
    {
        return (float)Math.toRadians(degrees);
    }
    
    public static float acosf(float a)
    {
        return (float)Math.acos(a);
    }
    
    public static float asinf(float a)
    {
        return (float)Math.asin(a);
    }
    
    public static float tanf(float a)
    {
        return (float)Math.tan(a);
    }
    
    public static float atanf(float a)
    {
        return (float)Math.atan(a);
    }
    
    public static float atan2f(float a, float b)
    {
        return (float)Math.atan2(a, b);
    }
    
    public static float expf(float a)
    {
        return (float)Math.exp(a);
    }
    public static float sqrtf(float f)
    {
        return (float)Math.sqrt(f);
    }
    
    public static float cosf(float f)
    {
        return (float)Math.cos(f);
    }
    
    public static float sinf(float f)
    {
        return (float)Math.sin(f);
    }
    
    public static float powf(float a, float b)
    {
        return (float)Math.pow(a, b);
    }
    
}
