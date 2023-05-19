/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.shapes;

import coordinate.generic.AbstractRay;
import coordinate.generic.SCoord;
import coordinate.generic.VCoord;
import coordinate.transform.Transform;
import coordinate.utility.Value1Df;
import coordinate.utility.Value2Df;
import coordinate.utility.Value3DBoolean;

/**
 *
 * @author https://jcgt.org/published/0007/03/04/
 * @param <S>
 * @param <V>
 * @param <R>
 */
public abstract class OrientedBBoxShape<S extends SCoord<S, V>, V extends VCoord<S, V>, R extends AbstractRay<S, V>> implements GenericShape<S, V, R> {
    public S center;
    public V radius;
    public V invRadius;
    public Transform<S, V> rotation;
    
    public OrientedBBoxShape(S center, V radius, float degrees, V axis)
    {
        if(axis.isZero())
            throw new UnsupportedOperationException("axis of the oriented bounding box should not be zero");
        if(axis.hasUndefinedValues())
            throw new UnsupportedOperationException("axis of the oriented bounding box has undefined values " +axis);
        
        this.center = center;
        this.radius = radius;
        this.invRadius = safeInverse(radius);
        this.rotation = Transform.rotate(degrees, axis);
    }
    
    protected final float safeInverse(float x) 
    { 
        return (x == 0.0) ? 1e12f : (1.0f / x); 
    }
    protected final V safeInverse(V v) 
    { 
        V invV = (V) v.newV(safeInverse(v.get('x')), safeInverse(v.get('y')), safeInverse(v.get('z')));
        return invV;
    }

    // vec3 box.radius:       independent half-length along the X, Y, and Z axes
    // mat3 box.rotation:     box-to-world rotation (orthonormal 3x3 matrix) transformation
    // bool rayCanStartInBox: if true, assume the origin is never in a box. GLSL optimizes this at compile time
    // bool oriented:         if false, ignore box.rotation
    public boolean ourIntersectBoxCommon(
            R ray, 
            Value1Df distance, 
            V normal, 
            boolean rayCanStartInBox, 
            boolean oriented) 
    {                
        // Move to the box's reference frame. This is unavoidable and un-optimizable.
        S o = this.rotation.transformVector(ray.getOrigin().sub(this.center)).asScalar();         
        V d = ray.getDirection();        
        if (oriented)
            d = rotation.transformVector(d);       
        ray.set(o, d);
        
        
        // This "rayCanStartInBox" branch is evaluated at compile time because `const` in GLSL
        // means compile-time constant. The multiplication by 1.0 will likewise be compiled out
        // when rayCanStartInBox = false.
        float winding;
        
        if (rayCanStartInBox) {
            // Winding direction: -1 if the ray starts inside of the box (i.e., and is leaving), +1 if it is starting outside of the box
            winding = ray.getOrigin().abs().mul(this.invRadius.asScalar()).maxComponent() < 1.0f ? -1.0f : 1.0f;            
        } else {
            winding = 1.0f;
        }
        
        // We'll use the negated sign of the ray direction in several places, so precompute it.
        // The sign() instruction is fast...but surprisingly not so fast that storing the result
        // temporarily isn't an advantage.
        V sgn = ray.getDirection().sign().neg();
                
        // Ray-plane intersection. For each pair of planes, choose the one that is front-facing
        // to the ray and compute the distance to it.
        V distanceToPlane = this.radius.mul(winding).mul(sgn).asScalar().sub(ray.getOrigin());
        if (oriented) {
            distanceToPlane.divAssign(ray.getDirection());
        } else {
            distanceToPlane.mulAssign(ray.getInverseDirection()); 
        }    
        
        // Perform all three ray-box tests and cast to 0 or 1 on each axis. 
        Value3DBoolean test = new Value3DBoolean(
                TEST(distanceToPlane, ray, 'x', "yz"),
                TEST(distanceToPlane, ray, 'y', "zx"),
                TEST(distanceToPlane, ray, 'z', "xy"));
        
        sgn = test.x ? sgn.newV(sgn.get('x'), 0.0f, 0.0f) : (test.y ? sgn.newV(0.0f, sgn.get('y'), 0.0f) : sgn.newV(0.0f, 0.0f, test.z ? sgn.get('z') : 0.0f));    
                
        // At most one element of sgn is non-zero now. That element carries the negative sign of the 
        // ray direction as well. Notice that we were able to drop storage of the test vector from registers,
        // because it will never be used again.

        // Mask the distance by the non-zero axis
        // Dot product is faster than this CMOV chain, but doesn't work when distanceToPlane contains nans or infs. 
        distance.x = (sgn.get('x') != 0.0) ? distanceToPlane.get('x') : ((sgn.get('y') != 0.0) ? distanceToPlane.get('y') : distanceToPlane.get('z'));
        
        // Normal must face back along the ray. If you need
        // to know whether we're entering or leaving the box, 
        // then just look at the value of winding. If you need
        // texture coordinates, then use box.invDirection * hitPoint.
        if (oriented) {
            normal.setValue(this.rotation.transformVector(sgn));
        } else {
            normal.setValue(sgn);
        }
        
        return (sgn.get('x') != 0) || (sgn.get('y') != 0) || (sgn.get('z') != 0);
    }
    
    //to avoid verbose implementation (this should be for different 3 axis (x, y, z) separately)
    private boolean TEST(V distanceToPlane, R ray, char u, String vw)
    {
        Value2Df rayOriginVW    = new Value2Df(ray.getOrigin().get(vw.charAt(0)), ray.getOrigin().get(vw.charAt(1))); //charAt(0), charAt(1) is xy
        Value2Df rayDirectionVW = new Value2Df(ray.getDirection().get(vw.charAt(0)), ray.getDirection().get(vw.charAt(1)));
        Value2Df boxRadiusVW    = new Value2Df(radius.get(vw.charAt(0)), radius.get(vw.charAt(1)));
        Value1Df distanceToPlaneU = new Value1Df(distanceToPlane.get(u));
        
        return (distanceToPlaneU.x >= 0.0) &&
                rayOriginVW.add(rayDirectionVW.mul(distanceToPlaneU.x)).abs().lessThan(boxRadiusVW).allTrue();
    }
}
