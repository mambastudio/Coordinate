/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

import coordinate.generic.VCoord;
import java.util.Random;

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
    
    public static int[] generateRandomIntegers(int size)
    {
        return new Random().ints(size, 0, size).toArray();
    }
    
    // get index from pixel
    public static int getIndex(Value2Di pixel, int width, int height)
    {
        return pixel.x + pixel.y * width;
    }
    
    public static Value2Di getSphericalGridXY(int width, int height, VCoord d)
    {
        float xf, yf;
        int xi, yi;
        float phi, theta;

        //do conversion of vector direction to u v - [0 to env map size] coordinates
        phi = acosf(d.get('y'));
        theta = atan2f(d.get('z'), d.get('x'));
        xf = 0.5f - 0.5f * theta / PI_F;
        yf = phi / PI_F;
        
        xf = xf - (int) xf;
        yf = yf - (int) yf;
        
        if (xf < 0)
            xf++;
        if (yf < 0)
            yf++;
        
        float dx = (float) xf * (width - 1);
        float dy = (float) yf * (height - 1);
        
        xi = (int) dx;
        yi = (int) dy;

        return new Value2Di(xi, yi);
    }

    //sunflow renderer
    public static int getSphericalGridIndex(int width, int height, VCoord d)   
    {
        Value2Di xy = getSphericalGridXY(width, height, d);          
        return getIndex(xy, width, height);
    }
    

    //u = [0, 1] v = [0, 1]
    //phi is z axis which up
    //theta is plane x,y
    //sunflow renderer
    public static VCoord getSphericalDirection(float u, float v, VCoord newD)
    {
        float phi = PI_F * v;
        float theta = u * 2 * PI_F;

        float x     = -sinf(phi) * cosf(theta);
        float y     = cosf(phi);
        float z     = sinf(phi) * sinf(theta);

        newD.set(x, y, z, 0);        
        return newD;

    }
}
