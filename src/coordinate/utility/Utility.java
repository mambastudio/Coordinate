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
    
    public static float check(float value)
    {
      if(Float.isInfinite(value)||Float.isNaN(value)|| value<0.000001f)
          return 0.f;
      else
          return value;
    }
    
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
    
    // fractional error in math formula less than 1.2 * 10 ^ -7.
    // although subject to catastrophic cancellation when z in very close to 0
    // from Chebyshev fitting formula for erf(z) from Numerical Recipes, 6.2
    //https://introcs.cs.princeton.edu/java/21function/ErrorFunction.java.html
    public static double erf(double z) {
        double t = 1.0 / (1.0 + 0.5 * Math.abs(z));

        // use Horner's method
        double ans = 1 - t * Math.exp( -z*z   -   1.26551223 +
                                            t * ( 1.00002368 +
                                            t * ( 0.37409196 + 
                                            t * ( 0.09678418 + 
                                            t * (-0.18628806 + 
                                            t * ( 0.27886807 + 
                                            t * (-1.13520398 + 
                                            t * ( 1.48851587 + 
                                            t * (-0.82215223 + 
                                            t * ( 0.17087277))))))))));
        if (z >= 0) return  ans;
        else        return -ans;
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
    
    
    //https://computergraphics.stackexchange.com/questions/7738/how-to-assign-calculate-triangle-texture-coordinates
    //also sunflow Texture.java
    public static Value2Di getSphericalGridXY(int width, int height, VCoord d)
    {
        Value2Df uv = Utility.getLongLatUV(d);
        
        uv.x = frac(uv.x);
        uv.y = frac(uv.y);
        
        return new Value2Di((int)(uv.x * (width - 1)), (int)(uv.y * (height - 1)));
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
    
    //https://github.com/fpsunflower/sunflow/blob/15fa9c6cc6729934181bb877e67f1d1c13679f89/src/org/sunflow/core/light/ImageBasedLight.java#L242
    public static Value2Df getLongLatUV(VCoord vec)
    {
        float u, v;
        
        // assume lon/lat format
        double phi, theta;
        phi = Math.acos(vec.get('y'));
        theta = Math.atan2(vec.get('z'), vec.get('x'));
        u = (float) (0.5 - 0.5 * theta / Math.PI);
        v = (float) (phi / Math.PI);
        
        return new Value2Df(u, v);
    }
    
    public static final float frac(float x) {
        return x < 0 ? x - (int) x + 1 : x - (int) x;
    }
    
    
    public static Value2Df sampleUniformTriangle(Value2Df aSamples)
    {
        float term = (float) Math.sqrt(aSamples.x);

        return new Value2Df(1.f - term, aSamples.y * term);
    }
    
    
    public static float cosHemispherePdfW(VCoord aNormal,
                                   VCoord aDirection)
    {
        return Math.max(0.f, aNormal.dot(aDirection)) * INV_PI_F;
    }
    
    //////////////////////////////////////////////////////////////////////////
    // Utilities for converting PDF between Area (A) and Solid angle (W)
    // WtoA = PdfW * cosine / distance_squared
    // AtoW = PdfA * distance_squared / cosine

    public static float pdfWtoA(
        float aPdfW,
        float aDist,
        float aCosThere)
    {
        return aPdfW * Math.abs(aCosThere) / (float)Math.pow(aDist, 2);
    }

    public static float pdfAtoW(
        float aPdfA,
        float aDist,
        float aCosThere)
    {
        return aPdfA * (float)Math.pow(aDist, 2) / Math.abs(aCosThere);
    }
    
    // Mis power (1 for balance heuristic)
    public static float mis(float aPdf) 
    {
        return aPdf;
    }

    // Mis weight for 2 pdfs
    public static float mis2(float aSamplePdf, float aOtherPdf) 
    {
        return mis(aSamplePdf) / (mis(aSamplePdf) + mis(aOtherPdf));
    }

    //log2
    public static long log2(long x )
    {
        if( x == 0 )
            throw new UnsupportedOperationException("value should be zero");
        return 63 - Long.numberOfLeadingZeros( x );
    }
    
    //https://jameshfisher.com/2018/03/30/round-up-power-2/ 
    public static long next_pow2(long x)
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
