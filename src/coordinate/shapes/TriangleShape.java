/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.shapes;

import coordinate.generic.AbstractRay;
import coordinate.generic.SCoord;
import coordinate.generic.VCoord;
import coordinate.utility.Value1Df;
import coordinate.utility.Value2Df;
import static java.lang.Math.abs;
import java.util.Optional;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 * @param <R>
 * @param <B>
 */
public interface TriangleShape<
        S extends SCoord, 
        V extends VCoord, 
        R extends AbstractRay<S, V, R>,
        B extends AlignedBBoxShape<S, V, R, B>>
     extends GenericShape<S, V, R> 
{
    public S p1();    
    public S p2();    
    public S p3();
    
    public Optional<V> n1();    
    public Optional<V> n2();    
    public Optional<V> n3();
    
    public B getBound();
                
    default V n()
    {
        V e1 = (V) p2().sub(p1());  
        V e2 = (V) p3().sub(p1());

        return (V) e1.cross(e2).normalize();
    }
    
    default V n(Value2Df uv)
    {
        if(n1().isPresent() && n2().isPresent() && n3().isPresent())    
        {
            V n1 = (V) n1().get(), n2 = (V) n2().get(), n3 = (V) n3().get();
            return (V) n1.mul(1 - uv.x - uv.y).add(n2.mul(uv.x).add(n3.mul(uv.y)));
        }          
        else{
            return n();
        }
    }
        
    default V e1()
    {
        return (V) p2().sub(p1());
    }
    
    default V e2()
    {
        return (V) p3().sub(p1());
    }
    
    @Override
    default boolean intersect(R r)
    {
        return this.intersect(r, null);
    }
    
    @Override
    default boolean intersect(R r, float[] tuv)
    {
        return this.mollerIntersection(r, tuv, p1(), p2(), p3());
    }
    
    default boolean mollerIntersection(R r, float[] tuv, S p1, S p2, S p3)
    {
        V e1, e2, h, s, q;
        double a, f, b1, b2;

        e1  = (V) p2.sub(p1);
        e2  = (V) p3.sub(p1);
        h   = (V) r.getDirection().cross(e2);
        a   = e1.dot(h);

        if (a > -0.0000001 && a < 0.0000001)
            return false;

        f = 1/a;
        
        s = (V) r.getOrigin().sub(p1);
	b1 = f * s.dot(h);

        if (b1 < 0.0 || b1 > 1.0)
            return false;

        q =  (V) s.cross(e1);
	b2 = f * r.getDirection().dot(q);

	if (b2 < 0.0 || b1 + b2 > 1.0)
            return false;

	float t = (float) (f * e2.dot(q));
        
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
    
    default boolean triangleBoxIntersection(
            AlignedBBoxShape<S, V, R, ?> aabb)
    {
        return triangleBoxIntersection(aabb.getCenter(), aabb.getHalfExtents(), p1(), p2(), p3());
    }
    
    
    /**
     * 
     * @param boxcenter
     * @param boxhalfsize
     * @param tv0
     * @param tv1
     * @param tv2
     * @return 
     * 
     * Concept and code from Tomas Akenine-Moller based on "Fast 3D Triangle-Box Overlap Testing"
     * https://fileadmin.cs.lth.se/cs/Personal/Tomas_Akenine-Moller/code/tribox3.txt
     * https://fileadmin.cs.lth.se/cs/Personal/Tomas_Akenine-Moller/code/tribox_tam.pdf 
     * 
     */
    default boolean triangleBoxIntersection(
            S boxcenter, V boxhalfsize, S tv0, S tv1, S tv2) {        
        /*    use separating axis theorem to test overlap between triangle and box */
	/*    need to test for overlap in these directions: */
	/*    1) the {x,y,z}-directions (actually, since we use the AABB of the triangle */
	/*       we do not even need to test these) */
	/*    2) n of the triangle */
	/*    3) crossproduct(edge from tri, {x,y,z}-directin) */
	/*       this gives 3x3=9 more tests */
        
        S v0, v1, v2; //localised to origin relative to box center
        float fex, fey, fez;
	Value1Df min = new Value1Df(), max = new Value1Df();
	V e0, e1, e2;
        
        /* This is the fastest branch on Sun */
	/* move everything so that the boxcenter is in (0,0,0) */
	v0 = (S) tv0.subS(boxcenter);
	v1 = (S) tv1.subS(boxcenter);
	v2 = (S) tv2.subS(boxcenter);
        
        
        /* compute triangle edges */
	e0 = (V) v1.sub(v0);
	e1 = (V) v2.sub(v1);
	e2 = (V) v0.sub(v2);
        
	/* Bullet 3:  */
	/*  test the 9 tests first (this was faster) */
	fex = abs(e0.get('x'));
	fey = abs(e0.get('y'));
	fez = abs(e0.get('z'));
        
        if (!axisTestX01(e0.get('z'), e0.get('y'), fez, fey, v0, v2, boxhalfsize, min, max))
		return false;  
        if (!axisTestY02(e0.get('z'), e0.get('x'), fez, fex, v0, v2, boxhalfsize, min, max))
		return false; 
	if (!axisTestZ12(e0.get('y'), e0.get('x'), fey, fex, v1, v2, boxhalfsize, min, max))
		return false; 
        
        fex = abs(e1.get('x'));
	fey = abs(e1.get('y'));
	fez = abs(e1.get('z'));

	if (!axisTestX01(e1.get('z'), e1.get('y'), fez, fey, v0, v2, boxhalfsize, min, max))
		return false; 
	if (!axisTestY02(e1.get('z'), e1.get('x'), fez, fex, v0, v2, boxhalfsize, min, max))
		return false;
	if (!axisTestZ0(e1.get('y'), e1.get('x'), fey, fex, v0, v1, boxhalfsize, min, max))
		return false;
        
        fex = abs(e2.get('x'));
	fey = abs(e2.get('y'));
	fez = abs(e2.get('z'));
	if (!axisTestX2(e2.get('z'), e2.get('y'), fez, fey, v0, v1, boxhalfsize, min, max))
		return false;
	if (!axisTestY1(e2.get('z'), e2.get('x'), fez, fex, v0, v1, boxhalfsize, min, max))
		return false;
	if (!axisTestZ12(e2.get('y'), e2.get('x'), fey, fex, v1, v2, boxhalfsize, min, max))
		return false;
        
        /* Bullet 1: */
	/*  first test overlap in the {x,y,z}-directions */
	/*  find min, max of the triangle each direction, and test for overlap in */
	/*  that direction -- this is equivalent to testing a minimal AABB around */
	/*  the triangle against the AABB */
        
        /* test in X-direction */
	findMinMax(v0.get('x'), v1.get('x'), v2.get('x'), min, max);
	if (min.x > boxhalfsize.get('x') || max.x < -boxhalfsize.get('x'))
		return false;
        
        /* test in Y-direction */
	findMinMax(v0.get('y'), v1.get('y'), v2.get('y'), min, max);
	if (min.x > boxhalfsize.get('y') || max.x < -boxhalfsize.get('y'))
		return false;

	/* test in Z-direction */
	findMinMax(v0.get('z'), v1.get('z'), v2.get('z'), min, max);
	if (min.x > boxhalfsize.get('z') || max.x < -boxhalfsize.get('z'))
		return false;

	/* Bullet 2: */
	/*  test if the box intersects the plane of the triangle */
	/*  compute plane equation of triangle: n*x+d=0 */	
        /*  box and triangle overlaps */   
        return planeBoxIntersection(v0,boxhalfsize);
    }
    
    default void findMinMax(float x0, float x1, float x2, Value1Df min, Value1Df max) {
        min.x = max.x = x0;
        if (x1 < min.x)
                min.x = x1;
        if (x1 > max.x)
                max.x = x1;
        if (x2 < min.x)
                min.x = x2;
        if (x2 > max.x)
                max.x = x2;
    }
    
    /*======================== X-tests ========================*/
    default boolean axisTestX01(
            float a, float b, float fa, float fb, 
            S v0, S v2, V boxhalfsize, Value1Df min, Value1Df max)
    {        
        float p0 = a * v0.get('y') - b * v0.get('z');
	float p2 = a * v2.get('y') - b * v2.get('z');
	if (p0 < p2) {
            min.x = p0;
            max.x = p2;
	} else {
            min.x = p2;
            max.x = p0;
	}
	float rad = fa * boxhalfsize.get('y') + fb * boxhalfsize.get('z');
        
	return !(min.x > rad || max.x < -rad);
    }
    
    default boolean axisTestX2(
            float a, float b, float fa, float fb, 
            S v0, S v1, V boxhalfsize, Value1Df min, Value1Df max) 
    {
        float p0, p1;
        p0 = a * v0.get('y') - b * v0.get('z');
        p1 = a * v1.get('y') - b * v1.get('z');
        if (p0 < p1) {
            min.x = p0;
            max.x = p1;
        } else {
            min.x = p1;
            max.x = p0;
        }
        float rad = fa * boxhalfsize.get('y') + fb * boxhalfsize.get('z');
        return !(min.x > rad || max.x < -rad);
    }
    
    /*======================== Y-tests ========================*/
    default boolean axisTestY02(
            float a, float b, float fa, float fb, 
            S v0, S v2, V boxhalfsize, Value1Df min, Value1Df max)
    {
        float p0, p2;
        p0 = -a * v0.get('x') + b * v0.get('z');
	p2 = -a * v2.get('x') + b * v2.get('z');
	if (p0 < p2) {
            min.x = p0;
            max.x = p2;
	} else {
            min.x = p2;
            max.x = p0;
	}
	float rad = fa * boxhalfsize.get('x') + fb * boxhalfsize.get('z');
	return !(min.x > rad || max.x < -rad);
    }
    
    default boolean axisTestY1(
            float a, float b, float fa, float fb, 
            S v0, S v1, V boxhalfsize, Value1Df min, Value1Df max) 
    {
        float p0, p1;
        p0 = -a * v0.get('x') + b * v0.get('z');
        p1 = -a * v1.get('x') + b * v1.get('z');
        if (p0 < p1) {
            min.x = p0;
            max.x = p1;
        } else {
            min.x = p1;
            max.x = p0;
        }
        float rad = fa * boxhalfsize.get('x') + fb * boxhalfsize.get('z');
        return !(min.x > rad || max.x < -rad);
    }
    
    /*======================== Z-tests ========================*/
    default boolean axisTestZ12(
            float a, float b, float fa, float fb, 
            S v1, S v2, V boxhalfsize, Value1Df min, Value1Df max)
    {
        float p1, p2;
        p1 = a * v1.get('x') - b * v1.get('y');
	p2 = a * v2.get('x') - b * v2.get('y');
	if (p2 < p1) {
            min.x = p2;
            max.x = p1;
	} else {
            min.x = p1;
            max.x = p2;
	}
	float rad = fa * boxhalfsize.get('x') + fb * boxhalfsize.get('y');
	return !(min.x > rad || max.x < -rad);
    }
    
    default boolean axisTestZ0(
            float a, float b, float fa, float fb, 
            S  v0, S v1, V  boxhalfsize, Value1Df min, Value1Df max) 
    {
        float p0, p1;
        p0 = a * v0.get('x') - b * v0.get('y');
        p1 = a * v1.get('x') - b * v1.get('y');
        if (p0 < p1) {
            min.x = p0;
            max.x = p1;
        } else {
            min.x = p1;
            max.x = p0;
        }
        float rad = fa * boxhalfsize.get('x') + fb * boxhalfsize.get('y');
        return !(min.x > rad || max.x < -rad);
    }
    
    
    //real-time rendering 4th edition by Tomas et al
    default boolean planeBoxIntersection(AlignedBBoxShape<S, V, R, ?> aabb)
    {
        S c         = aabb.getCenter();
        V h         = aabb.getHalfExtents();        
        float e     = h.get('x') * Math.abs(n().get('x')) + h.get('y') * Math.abs(n().get('y')) + h.get('z') * Math.abs(n().get('z'));
        float s     = TriangleShape.this.n().dot(c) - n().dot(p1()); //c.n + d
                
        return !(s - e > 0 || s + e < 0);
    }
    
    //if the bounding box center is translated to (0, 0, 0) with v0 of triangle
    default boolean planeBoxIntersection(S localV0, V halfExtents)
    {        
        float e     = halfExtents.get('x') * Math.abs(n().get('x')) + halfExtents.get('y') * Math.abs(n().get('y')) + halfExtents.get('z') * Math.abs(n().get('z'));
        float s     = - n().dot(localV0); //c.n + d
        
        return !(s - e > 0 || s + e < 0);
    }
       
    default String getString()
    {
        return "Tri: p1=" +p1()+ " p2=" +p2()+ " p3=" +p3();
    }
}
