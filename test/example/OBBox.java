/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import coordinate.model.Transform;
import coordinate.shapes.OrientedBoundingBox;
import coordinate.utility.Value1Df;

/**
 *
 * @author jmburu
 */
public class OBBox extends OrientedBoundingBox<Point3f, Vector3f>{
    public OBBox()
    {
        this.center = new Point3f();
        this.invRadius = new Vector3f();
        this.radius = new Vector3f();
        this.rotation = new Transform();
    }
    
    public OBBox(Point3f center, Vector3f radius, float degrees, Vector3f axis)
    {
        this.center = center;
        this.radius = radius;
        this.invRadius = safeInverse(radius);
        this.rotation = Transform.rotate(degrees, axis);
    }
    
    //line thickness
    public OBBox(Point3f p0, Point3f p2, float width)
    {
        
    }
    
    private float safeInverse(float x) 
    { 
        return (x == 0.0) ? 1e12f : (1.0f / x); 
    }
    private Vector3f safeInverse(Vector3f v) 
    { 
        return new Vector3f(safeInverse(v.x), safeInverse(v.y), safeInverse(v.z));
    }
    
    // vec3 box.radius:       independent half-length along the X, Y, and Z axes
    // mat3 box.rotation:     box-to-world rotation (orthonormal 3x3 matrix) transformation
    // bool rayCanStartInBox: if true, assume the origin is never in a box. GLSL optimizes this at compile time
    // bool oriented:         if false, ignore box.rotation
    public boolean ourIntersectBoxCommon(
            Ray ray, Value1Df distance, 
            Vector3f normal, 
            boolean rayCanStartInBox, 
            boolean oriented, 
            Vector3f _invRayDirection) 
    {
        // Move to the box's reference frame. This is unavoidable and un-optimizable.
        ray.o = this.rotation.transformVector(ray.o.sub(this.center)).asScalar();         
        if (oriented) {
            ray.d = rotation.transformVector(ray.d);
        }
        
        // This "rayCanStartInBox" branch is evaluated at compile time because `const` in GLSL
        // means compile-time constant. The multiplication by 1.0 will likewise be compiled out
        // when rayCanStartInBox = false.
        float winding;
        
        if (rayCanStartInBox) {
            // Winding direction: -1 if the ray starts inside of the box (i.e., and is leaving), +1 if it is starting outside of the box
            winding = ray.o.abs().mul(this.invRadius.asScalar()).maxComponent() < 1.0f ? -1.0f : 1.0f;            
        } else {
            winding = 1.0f;
        }
        
        // We'll use the negated sign of the ray direction in several places, so precompute it.
        // The sign() instruction is fast...but surprisingly not so fast that storing the result
        // temporarily isn't an advantage.
        Vector3f sgn = ray.d.sign().neg();
        return false;
    }
}
