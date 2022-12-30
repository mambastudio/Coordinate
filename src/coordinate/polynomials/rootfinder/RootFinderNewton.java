/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.polynomials.rootfinder;

import coordinate.polynomials.PolynomialN;
import static coordinate.utility.Utility.clamp;
import static java.lang.Float.isFinite;
import static java.lang.Math.abs;
import static java.lang.Math.nextAfter;

/**
 *
 * @author user
 */
public class RootFinderNewton extends RootFinder {

    public RootFinderNewton(int N, boolean boundError) {
        super(N, boundError);
    }
    
    @Override
    public float findClosed(float[] coef, float[] deriv, float x0, float x1, float y0, float y1, float xError) {
        float ep2 = 2*xError;
	float xr = (x0 + x1) / 2;	// mid point
	if ( x1-x0 <= ep2 ) 
            return xr;
        
        if ( N <= 3 ) 
        {
            float xr0 = xr;
            for ( int safetyCounter=0; safetyCounter<16; ++safetyCounter ) 
            {
                float xn = xr - new PolynomialN(N).eval(coef, xr)/new PolynomialN(2).eval(deriv, xr);
                xn = clamp( xn, x0, x1 );
                if ( abs(xr - xn) <= xError ) return xn;
                xr = xn;
            }
            if (!isFinite(xr) ) 
                xr = xr0;
	}
        
        float yr = new PolynomialN(N).eval(coef, xr);
	float xb0 = x0;
	float xb1 = x1;
        
        while ( true ) 
        {
            boolean side = IsDifferentSign( y0, yr );
            if ( side ) 
                xb1 = xr; 
            else 
                xb0 = xr;
            float dy = new PolynomialN(N-1).eval(deriv, xr);
            float dx = yr / dy;
            float xn = xr - dx;
            if( xn > xb0 && xn < xb1 ) 
            { 
                // valid Newton step
                float stepsize = abs(xr-xn);
                xr = xn;
                if ( stepsize > xError ) 
                {
                    yr = new PolynomialN(N).eval(coef, xr);
                } 
                else 
                {
                    if(boundError) {
                        float xs;
                        if ( xError == 0 ) {
                                xs = nextAfter( side?xb1:xb0, side?xb0:xb1 );
                        } else {
                                xs = xn - MultSign(xError, toInt(side)-1);
                                if ( xs == xn ) 
                                    xs = nextAfter( side?xb1:xb0, side?xb0:xb1 );
                        }
                        float ys = new PolynomialN(N).eval(coef, xs);
                        boolean s = IsDifferentSign( y0, ys );
                        if ( side != s ) 
                            return xn;
                        xr = xs;
                        yr = ys;
                    } 
                    else 
                        break;
                }
            } 
            else 
            { // Newton step failed
                xr = (xb0 + xb1) / 2;
                if ( xr == xb0 || xr == xb1 || xb1 - xb0 <= ep2 ) {
                    if (boundError) {
                        if ( xError == 0 ) {
                                float xm = side ? xb0 : xb1;
                                float ym = new PolynomialN(N).eval(coef, xm);
                                if (abs(ym) < abs(yr) ) 
                                    xr = xm;
                        }
                    }
                    break;
                }
                yr = new PolynomialN(N).eval(coef, xr);
            }
	}
	return xr;
    }


    public float findOpen(boolean openMin, float[] coef, float[] deriv, float xm, float ym, float xr, float xError) {
        float delta = (float)(1);
	float yr = new PolynomialN(N).eval(coef, xr);

	boolean otherside = IsDifferentSign( ym, yr );

        outer:
	while(yr != 0) {
            if ( otherside ) {
                if( openMin ) {
                        return findClosed( coef, deriv, xr, xm, yr, ym, xError );
                } else {
                        return findClosed( coef, deriv, xm, xr, ym, yr, xError );
                }
            } 
            else {
                open_interval:
                while(true)
                {
                    xm = xr;
                    ym = yr;
                    float dy = new PolynomialN(N-1).eval(deriv, xr);
                    float dx = yr / dy;
                    float xn = xr - dx;
                    float dif = openMin ? xr-xn : xn-xr;
                    if ( dif <= 0 && isFinite(xn) ) 
                    {	// valid Newton step
                        xr = xn;
                        if ( dif <= xError ) { // we might have converged
                            if ( xr == xm ) break;
                            float xs = xn - MultSign( xError, -(float)(toInt(openMin)));
                            float ys = new PolynomialN(N).eval(coef, xs);
                            boolean s = IsDifferentSign( ym, ys );
                            if ( s ) 
                                break outer;
                            xr = xs;
                            yr = ys;
                            continue; //goto open_interval
                        }                        
                    } 
                    else 
                    {   // Newton step failed
                        xr = openMin ? xr - delta : xr + delta;
                        delta *= 2;
                    }
                    yr = new PolynomialN(N).eval(coef, xr);
                    otherside = IsDifferentSign( ym, yr );
                    break;
                }
            }
	}
	return xr;
    }

    @Override
    public float findOpen(float[] coef, float[] deriv, float xError) {
        if( (N & 1) == 1)
            throw new UnsupportedOperationException("RootFinderNewton::FindOpen only works for polynomials with odd degrees.");
	final float xr = 0;
	final float yr = coef[0]; // PolynomialEval<N,ftype>( coef, xr );
	if ( IsDifferentSign(coef[N],yr) ) {
		return findOpenMax( coef, deriv, xr, yr, xError );
	} else {
		return findOpenMin( coef, deriv, xr, yr, xError );
	}
    }

    @Override
    public float findOpenMin(float[] coef, float[] deriv, float x1, float y1, float xError) {
        return findOpen(true, coef, deriv, x1, y1, x1 - (float)(1), xError );
    }

    @Override
    public float findOpenMax(float[] coef, float[] deriv, float x0, float y0, float xError) {
        return findOpen(false, coef, deriv, x0, y0, x0 + (float)(1), xError );
    }

}
