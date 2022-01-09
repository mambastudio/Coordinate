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
public class Value4Df {
    public float x, y, z, w;
    
    public Value4Df(){}
    public Value4Df(float x, float y, float z, float w){this.x = x; this.y = y; this.z = z; this.w = w;}
    public Value2Df getXY(){return new Value2Df(x, y);}
    public Value3Df getXYZ(){return new Value3Df(x, y, z);}
    public static Value4Df getRng()
    {
        return new Value4Df(
                ThreadLocalRandom.current().nextFloat(), 
                ThreadLocalRandom.current().nextFloat(), 
                ThreadLocalRandom.current().nextFloat(), 
                ThreadLocalRandom.current().nextFloat());
    }
    
    @Override
    public String toString()
    {
        return String.format("(%3.2f, %3.2f, %3.2f, %3.2f)", x, y, z, w);
    }
}
