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
public class Value3Df {
    public float x, y, z;
    
    public Value3Df(){}
    public Value3Df(float x, float y, float z){this.x = x; this.y = y; this.z = z;}
    public Value3Df div(float value){return new Value3Df(x/value, y/value, z/value);}
    public Value3Df inverse(){return new Value3Df(1.f/x, 1.f/y, 1.f/z);}
    public Value2Df getXY(){return new Value2Df(x, y);}
    public static Value3Df getRng()
    {
        return new Value3Df(
                ThreadLocalRandom.current().nextFloat(), 
                ThreadLocalRandom.current().nextFloat(), 
                ThreadLocalRandom.current().nextFloat());
    }
}
