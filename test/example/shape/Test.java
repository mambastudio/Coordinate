/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.shape;

import example.BBox;
import example.Point3f;
import example.Ray;
import example.Tri;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        triangleTest();
    }
    
    public static void triangleTest()
    {
        Tri triangle = new Tri(new Point3f(0, 1.5f, 0), new Point3f(6, 0, 0), new Point3f(0, 0, -4));
        BBox box = new BBox(new Point3f(), new Point3f(1, 1, -1));
        
        System.out.println(triangle.planeBoxIntersection(box));
    }
}
