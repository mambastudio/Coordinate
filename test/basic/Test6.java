/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.struct.StructFloatArray;
import example.Point3f;

/**
 *
 * @author user
 */
public class Test6 {
    public static void main(String... args)
    {
        StructFloatArray<Point3f> pointArray = new StructFloatArray<>(Point3f.class, 2);
        for(Point3f p : pointArray)
        {
            p.set(1, 3, 4);
        }
        
        for(Point3f p : pointArray)
        {
            System.out.println(p);
        }
    }
}
