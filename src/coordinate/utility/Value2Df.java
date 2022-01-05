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
    
    public static Value2Df getRng()
    {
        return new Value2Df(
                ThreadLocalRandom.current().nextFloat(), 
                ThreadLocalRandom.current().nextFloat());
    }
}
