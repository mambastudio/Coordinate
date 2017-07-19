/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.shape;

import example.Point3f;
import example.Ray;
import example.Vector3f;

/**
 *
 * @author user
 */
public class Triangle
{
    Point3f p1, p2, p3;
    
    public Triangle(Point3f p1, Point3f p2, Point3f p3)
    {
        this.p1 = p1; this.p2 = p2; this.p3 = p3;
    }
    
    public boolean intersection(Ray r, float[] tuv)
    {
        Vector3f e1, e2, h, s, q;
        double a, f, b1, b2;

        e1 = Point3f.sub(p2, p1);
        e2 = Point3f.sub(p3, p1);
        h = Vector3f.cross(r.d, e2);
        a = Vector3f.dot(e1, h);

        if (a > -0.0000001 && a < 0.0000001)
            return false;

        f = 1/a;
        
        s = Point3f.sub(r.o, p1);
	b1 = f * (Vector3f.dot(s, h));

        if (b1 < 0.0 || b1 > 1.0)
            return false;

        q = Vector3f.cross(s, e1);
	b2 = f * Vector3f.dot(r.d, q);

	if (b2 < 0.0 || b1 + b2 > 1.0)
            return false;

	float t = (float) (f * Vector3f.dot(e2, q));
        
        if(r.isInside(t)) 
        {
            if(tuv != null)
            {
                tuv[0] = t;
                tuv[1] = (float) b1;
                tuv[2] = (float) b2;
            }
            return true;
        }
        else
            return false;
    }
}
