/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

import java.util.Arrays;

/**
 *
 * @author user
 */
public class Value3Di {
    public int x, y, z;
    
    public Value3Di()
    {
        x = y = z = 0;
    }
    
    public Value3Di(int v)
    {
        x = y = z = v;
    }
    
    public Value3Di(int x, int y, int z)
    {
        this.x = x; this.y = y; this.z = z;
    }
    
    public Value3Di mul(Value3Di val)
    {
        return new Value3Di(
                x * val.x,
                y * val.y,
                z * val.z
        );
    }
    
    public Value3Di sub(Value3Di val)
    {
        return new Value3Di(
                x - val.x,
                y - val.y,
                z - val.z
        );
    }
    
    public int get(int axis)
    {
        switch (axis) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                return z;
        }    
    }
    
    public void set(int x, int y, int z)
    {
        this.x = x; this.y = y; this.z = z;
    }
    
    public static Value3Di max(Value3Di a, Value3Di b)
    { 
        return new Value3Di(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z)); 
    }
    
    @Override
    public String toString()
    {
        int[] array = new int[]{x, y, z};
        return Arrays.toString(array);
    }
    
    public boolean allNonZero()
    {
        float array[] = new float[]{x, y, z};
        boolean result = true;
        int i;
        for(i = 0; i < array.length; ++i)
        {
            result &= array[i]>0;
        }
        return result;
    }
    
    public Value3Di leftShift(int shift)
    {
        return new Value3Di(
                x << shift,
                y << shift,
                z << shift);
    }
    
    public Value3Di rightShift(int shift)
    {
        return new Value3Di(
                x >> shift,
                y >> shift,
                z >> shift);
    }
    
    public Value3Di and(int shift)
    {
        return new Value3Di(
                x & shift,
                y & shift,
                z & shift);
    }
}
