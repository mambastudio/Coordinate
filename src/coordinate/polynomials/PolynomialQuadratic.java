/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.polynomials;

import coordinate.function.RootCallBackFloat;
import coordinate.polynomials.rootfinder.RootFinder;
import coordinate.polynomials.rootfinder.RootFinderNewton;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

/**
 *
 * @author jmburu
 */
public class PolynomialQuadratic extends Polynomials{
    
    public PolynomialQuadratic()
    {
        this(2, false, null, null);
    }
    
    public PolynomialQuadratic(boolean boundingError)
    {
        this(2, boundingError, null, null);
    }
    
    public PolynomialQuadratic(boolean boundingError, RootFinder rootFinder)
    {
        this(2, boundingError, rootFinder, null);
    }
    
    public PolynomialQuadratic(boolean boundingError, RootFinder rootFinder, RootCallBackFloat rootCallBack)
    {
        this(2, boundingError, rootFinder, rootCallBack);
    }

    private PolynomialQuadratic(int N, boolean boundingError, RootFinder rootFinder, RootCallBackFloat rootCallBack) {
        super(N, boundingError, rootFinder, rootCallBack);
    }
    
    @Override
    public int roots(float[] roots, float[] coef, float x0, float x1, float xError) {
        float c = coef[0];
	float b = coef[1];
	float a = coef[2];
	float delta = b*b - 4*a*c;
	if ( delta > 0 ) {
		float d = (float) sqrt(delta);
		float q = (float)(-0.5) * ( b + MultSign(d,b) );
		float rv0 = q / a;
		float rv1 = c / q;
		float r0 = min( rv0, rv1 );
		float r1 = max( rv0, rv1 );
		int r0i = toInt(( r0 >= x0 ) & ( r0 <= x1));
		int r1i = toInt((r1 >= x0 ) & ( r1 <= x1));
		roots[ 0 ] = r0;
		roots[r0i] = r1;
		return r0i + r1i;
	} else if ( delta < 0 ) return 0;
	float r0 = (float)(-0.5) * b / a;
	roots[0] = r0;
	return toInt((r0 >= x0 ) & ( r0 <= x1));
    }

    @Override
    public int roots(float[] roots, float[] coef, float xError) {
        float c = coef[0];
	float b = coef[1];
	float a = coef[2];
	float delta = b*b - 4*a*c;
	if ( delta > 0 ) {
		float d = (float) sqrt(delta);
		float q = (float)(-0.5) * ( b + MultSign(d,b) );
		float rv0 = q / a;
		float rv1 = c / q;
		roots[0] = min( rv0, rv1 );
		roots[1] = max( rv0, rv1 );
		return 2;
	} 
        else if ( delta < 0 ) 
            return 0;
	roots[0] = (float)(-0.5) * b / a;
	return toInt(a != 0);
    }

    @Override
    public boolean firstRoot(float[] roots, float[] coef, float x0, float x1, float xError) {
        float c = coef[0];
	float b = coef[1];
	float a = coef[2];
	float delta = b*b - 4*a*c;
	if (delta >= 0) {
            float d = (float) sqrt(delta);
            float q = (float)(-0.5) * ( b + MultSign(d,b) );
            float rv0 = q / a;
            float rv1 = c / q;
            float r0 = min( rv0, rv1 );
            if ( r0 >= x0 ) {
                    roots[0] = r0;
                    return r0 <= x1;
            } else {
                    float r1 = max( rv0, rv1 );
                    roots[0] = r1;
                    return ( r1 >= x0 ) & ( r1 <= x1 );
            }
	}
	return false;
    }

    @Override
    public boolean firstRoot(float[] roots, float[] coef, float xError) {
        float c = coef[0];
	float b = coef[1];
	float a = coef[2];
	float delta = b*b - 4*a*c;
	if ( delta >= 0 ) {
            float d = (float) sqrt(delta);
            float q = (float)(-0.5) * ( b + MultSign(d,b) );
            float rv0 = q / a;
            float rv1 = c / q;
            roots[0] = min( rv0, rv1 );
            return true;
	}
	return false;
    }

    @Override
    public boolean hasRoot(float[] coef, float x0, float x1, float xError) {
        float c = coef[0];
	float b = coef[1];
	float a = coef[2];
	float delta = b*b - 4*a*c;
	if ( delta >= 0 ) {
            float d = (float) sqrt(delta);
            float q = (float)(-0.5) * ( b + MultSign(d,b) );
            float rv0 = q / a;
            float rv1 = c / q;
            float r0 = min( rv0, rv1 );
            float r1 = max( rv0, rv1 );
            if ( r0 >= x0 && r0 <= x1 ) return true;
            if ( r1 >= x0 && r1 <= x1 ) return true;
	}
	return false;
    }

    @Override
    public boolean hasRoot(float[] coef, float xError) {
        return hasRoot(coef, -Float.MAX_VALUE, Float.MAX_VALUE, 0);
    }

    @Override
    public boolean forEachRoot(RootCallBackFloat callback, float[] coef, float x0, float x1, float xError) {
        float c = coef[0];
	float b = coef[1];
	float a = coef[2];
	float delta = b*b - 4*a*c;
	if ( delta >= 0 ) {
            float d = (float) sqrt(delta);
            float q = (float)(-0.5) * ( b + MultSign(d,b) );
            float rv0 = q / a;
            float rv1 = c / q;
            float r0 = min( rv0, rv1 );
            float r1 = max( rv0, rv1 );
            if ( r0 >= x0 && r0 <= x1 ) if ( callback.call(r0) ) return true;
            if ( r1 >= x0 && r1 <= x1 ) return callback.call(r1);
	}
	return false;
    }

    @Override
    public boolean forEachRoot(RootCallBackFloat callback, float[] coef, float xError) {
        float c = coef[0];
	float b = coef[1];
	float a = coef[2];
	float delta = b*b - 4*a*c;
	if(delta >= 0) {
            float d = (float) sqrt(delta);
            float q = (float)(-0.5) * ( b + MultSign(d,b) );
            float rv0 = q / a;
            float rv1 = c / q;
            float r0 = min( rv0, rv1 );
            float r1 = max( rv0, rv1 );
            if (callback.call(r0) ) return true;
            return callback.call(r1);
	}
	return false;
    }
    
}
