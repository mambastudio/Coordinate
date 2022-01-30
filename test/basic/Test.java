/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.utility.Value2Di;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        Value2Di xy = new Value2Di(0, 0);
        System.out.println(xy);
        xy.applyJitterFromCenter(1, 1);
        System.out.println(xy);
        
        System.out.println(Math.floorMod(-3, 4));
    }
}
