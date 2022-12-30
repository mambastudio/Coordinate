/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.box;

import coordinate.utility.Value1Df;
import example.Point3f;
import example.Ray;
import example.Vector3f;

/**
 *
 * @author user
 */
public class TestOBBox {
    public static void main(String... args)
    {
        Ray ray = new Ray();
        ray.set(new Point3f(0.9f, 0, 0), new Vector3f(0, 0, 1));
        
        OBBox obb = new OBBox(new Point3f(0, 0, 4), new Vector3f(1, 1, 1), 0, new Vector3f(0, 1, 0));
        Value1Df distance = new Value1Df();
        Vector3f normal = new Vector3f();
        
        System.out.println(obb.ourIntersectBoxCommon(ray, distance, normal, true, true));   
        System.out.println(distance);
        
    }
    
}
