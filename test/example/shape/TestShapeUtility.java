/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.shape;

import coordinate.shapes.ShapeUtility;
import example.BBox;
import example.Point3f;
import example.Tri;

/**
 *
 * @author user
 */
public class TestShapeUtility {
    
    
    public static void main(String... args)
    {
        testTriangleBox();
    }
    
    public static void testTriangleBox()
    {
        Tri tri = new Tri(new Point3f(0, 0, 5), new Point3f(0, 10f, 0), new Point3f(5f, 0, 0));
        BBox box = new BBox();
        box.include(new Point3f(), new Point3f(2, 2, 2));
        
        boolean overlap = box.triBoxOverlap(tri);
        
        
        System.out.println(overlap);
    }
}
