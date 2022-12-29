/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author user
 */
public class Value2Df {
    public float x, y;
    
    public Value2Df(){}
    public Value2Df(float x, float y){this.x = x; this.y = y;}
    
    public static Value2Df getRandom()
    {
        return new Value2Df(
                ThreadLocalRandom.current().nextFloat(), 
                ThreadLocalRandom.current().nextFloat());
    }
    
    public static Value2Df getRandomJitter()
    {
        Value2Df random = Value2Df.getRandom();
        random.mulAssign(2);
        random.subAssign(1);
        return random;
    }
    
    public Value2Df abs()
    {
        return new Value2Df(
                Math.abs(x), 
                Math.abs(y));
    }
    
    public Value2Df add(float v)
    {
        return new Value2Df(
                x + v, 
                y + v);
    }
    
    public void addAssign(float v)
    {
        x += v;
        y += v;
    }
    
    public Value2Df add(Value2Df v)
    {
       return new Value2Df(
               x + v.x, 
               y + v.y);
    }
        
    public void addAssign(Value2Df v)
    {
        x += v.x;
        y += v.y;
    }
    
    public Value2Df mul(float v)
    {
        return new Value2Df(
                x * v, 
                y * v);
    }
    
    public void mulAssign(float v)
    {
        x *= v;
        y *= v;
    }
    
    public void mulAssign(Value2Df v)
    {
        x *= v.x;
        y *= v.y;
    }
    
    public Value2Df mul(Value2Df v)
    {
        return new Value2Df(
                x + v.x, 
                y + v.y);
    }
    
    public void subAssign(float v)
    {
        x -= v;
        y -= v;
    }
    
    public boolean allNonZero()
    {
        float array[] = new float[]{x, y};
        boolean result = true;
        int i;
        for(i = 0; i < array.length; ++i)
        {
            result &= array[i]>0;
        }
        return result;
    }
    
    public Value2DBoolean lessThan(Value2Df v)
    {
        Value2DBoolean boolValues = new Value2DBoolean(
                x < v.x, 
                y < v.y);
        return boolValues;
    }
}
