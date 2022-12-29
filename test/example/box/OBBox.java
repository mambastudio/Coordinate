/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.box;

import coordinate.shapes.OrientedBoundingBox;
import example.Point3f;
import example.Ray;
import example.Vector3f;

/**
 *
 * https://jcgt.org/published/0007/03/04/
 */
public class OBBox extends OrientedBoundingBox<Point3f, Vector3f, Ray>{
    public OBBox()
    {
        this(new Point3f(), new Vector3f(), 0, new Vector3f(0, 1, 0));        
    }
    
    public OBBox(Point3f center, Vector3f radius, float degrees, Vector3f axis)
    {
        super(center, radius, degrees, axis);        
    }
}
