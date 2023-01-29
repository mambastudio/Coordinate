/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.shapes;

import coordinate.generic.AbstractBound;
import coordinate.generic.AbstractRay;
import coordinate.generic.SCoord;
import coordinate.generic.VCoord;
import coordinate.utility.Value1Df;
import static java.lang.Math.abs;

/**
 *
 * @author user
 */
public class ShapeUtility 
{
    public static<
        S extends SCoord, 
        V extends VCoord, 
        R extends AbstractRay<S, V>, 
        B extends AbstractBound<S, V, R, B>,
        T extends TriangleShape<S, V, R>> boolean triBoxOverlap(B box, T triangle)
    {               
        return triBoxOverlap(box.getCenter(), box.getHalfExtents(), triangle.getP1(), triangle.getP2(), triangle.getP3());
    }
          
    public static<S extends SCoord, V extends VCoord> boolean triBoxOverlap(
            S boxcenter, V boxhalfsize, S tv0, S tv1, S tv2) {        
	/*    use separating axis theorem to test overlap between triangle and box */
	/*    need to test for overlap in these directions: */
	/*    1) the {x,y,z}-directions (actually, since we use the AABB of the triangle */
	/*       we do not even need to test these) */
	/*    2) normal of the triangle */
	/*    3) crossproduct(edge from tri, {x,y,z}-directin) */
	/*       this gives 3x3=9 more tests */
        
        S v0, v1, v2;
        float fex, fey, fez;
	Value1Df min = new Value1Df(), max = new Value1Df();
	V normal, e0, e1, e2;
        
        
        
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
	/*  compute plane equation of triangle: normal*x+d=0 */
	normal = (V) e0.cross(e1);
        /* box and triangle overlaps */   

        
        return planeBoxIntersection(normal,v0,boxhalfsize);
    }
    
    public static void findMinMax(float x0, float x1, float x2, Value1Df min, Value1Df max) {
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
    private static<S extends SCoord, V extends VCoord> boolean axisTestX01(
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
    
    private static<S extends SCoord, V extends VCoord> boolean axisTestX2(
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
    private static<S extends SCoord, V extends VCoord> boolean axisTestY02(
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
    
    private static<S extends SCoord, V extends VCoord> boolean axisTestY1(
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
    private static<S extends SCoord, V extends VCoord> boolean axisTestZ12(
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
    
    private static<S extends SCoord, V extends VCoord> boolean axisTestZ0(
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
    
    
    
    public static<
        S extends SCoord, 
        V extends VCoord,
        R extends AbstractRay<S, V>, 
        B extends AbstractBound<S, V, R, B>,
        T extends TriangleShape<S, V, R>> boolean planeBoxIntersection(T tri, B box)
    {
        V e0            = (V) tri.getP2().sub(tri.getP1());
        V e1            = (V) tri.getP3().sub(tri.getP2());
        V normal        = (V) e0.cross(e1);
        S v0            = (S) tri.getP1().subS(box.getCenter());
        V halfExtents   = box.getHalfExtents();
        
        return planeBoxIntersection(normal, v0, halfExtents);
    }
        
    public static<
        S extends SCoord, 
        V extends VCoord> boolean planeBoxIntersection(V normal, S localV0, V halfExtents)
    {        
        int q;        
        
        S vmin = (S) localV0.newS(0, 0, 0), vmax = (S) localV0.newS(0, 0, 0);
        float v;
        
        for(q = 0; q <= 2; q++)
        {
            v = localV0.get(q);					

            if(normal.get(q)>0.0f)
            {
                vmin.setIndex(q, - halfExtents.get(q) - v);	
                vmax.setIndex(q,   halfExtents.get(q) - v);                 
            }
            else
            {
                vmin.setIndex(q,   halfExtents.get(q) - v);                 
                vmax.setIndex(q, - halfExtents.get(q) - v);   
            }
        }
        
        if(normal.dot(vmin) >  0.0f) return false;        
        return normal.dot(vmax) >= 0.0f;
    }        
}
