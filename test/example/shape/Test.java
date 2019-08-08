/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.shape;

import example.Point3f;
import example.Ray;

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
        Triangle triangle = new Triangle(new Point3f(-1, -1, 0), new Point3f(0, 1, 0), new Point3f(1, 1, 0));
        Ray ray = new Ray();
        float[] tuv = new float[3];
        
        ray.set(0, 0, -2, 0, 0, 1);
        triangle.intersection(ray, tuv);
        System.out.println(tuv[0]);
    }
}
