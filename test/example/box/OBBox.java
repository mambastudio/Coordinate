/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.box;

import coordinate.transform.Transform;
import coordinate.shapes.OrientedBoundingBox;
import coordinate.utility.Value1Df;
import example.Point3f;
import example.Ray;
import example.Vector3f;

/**
 *
 * https://jcgt.org/published/0007/03/04/
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
            Ray ray, 
            Value1Df distance, 
            Vector3f normal, 
            boolean rayCanStartInBox, 
            boolean oriented) 
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
        
        // Ray-plane intersection. For each pair of planes, choose the one that is front-facing
        // to the ray and compute the distance to it.
        Vector3f distanceToPlane = this.radius.mul(winding).sign().asScalar().sub(ray.o);
        if (oriented) {
            distanceToPlane.divAssign(ray.d);
        } else {
            distanceToPlane.mulAssign(ray.inv_d); 
        }
        
        // At most one element of sgn is non-zero now. That element carries the negative sign of the 
        // ray direction as well. Notice that we were able to drop storage of the test vector from registers,
        // because it will never be used again.

        // Mask the distance by the non-zero axis
        // Dot product is faster than this CMOV chain, but doesn't work when distanceToPlane contains nans or infs. 
        distance.x = (sgn.x != 0.0) ? distanceToPlane.x : ((sgn.y != 0.0) ? distanceToPlane.y : distanceToPlane.z);
        
        // Normal must face back along the ray. If you need
        // to know whether we're entering or leaving the box, 
        // then just look at the value of winding. If you need
        // texture coordinates, then use box.invDirection * hitPoint.
        if (oriented) {
            normal.setValue(this.rotation.transformVector(sgn));
        } else {
            normal.setValue(sgn);
        }
        return (sgn.x != 0) || (sgn.y != 0) || (sgn.z != 0);
    }
}
