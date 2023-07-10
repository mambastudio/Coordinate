/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.shape;

import example.BBox;
import example.Point3f;
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
        BBox box = new BBox(new Point3f(1, 0, 0), new Point3f(2, 1, 1));
        Tri tri = new Tri(new Point3f(4, 0, 0), new Point3f(0, 4, 0), new Point3f(0, 0, 3f));
        System.out.println(tri.planeBoxIntersection(box));
    }
}
