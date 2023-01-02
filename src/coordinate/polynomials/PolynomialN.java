/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.polynomials;

import coordinate.function.RootCallBackFloat;
import coordinate.polynomials.rootfinder.RootFinder;
import coordinate.polynomials.rootfinder.RootFinderNewton;

/**
 *
 * @author user
 */
public class PolynomialN extends Polynomials{

    public PolynomialN(int N, boolean boundingError, RootFinder rootFinder, RootCallBackFloat rootCallBack)
    {
        super(N, boundingError, rootFinder, rootCallBack);
    }
    
    public PolynomialN(int N)
    {
        this(N, false, new RootFinderNewton(N, false), null);
    }

    @Override
    public int roots(float[] roots, float[] coef, float x0, float x1, float xError) 
    {
        if      (N == 1) return toInt(new PolynomialLinear().roots(roots, coef, x0, x1));
        else if (N == 2) return new PolynomialQuadratic().roots(roots, coef, x0, x1, xError);
        else if (N == 3) return new PolynomialCubic().roots( roots, coef, x0, x1, xError);
	else if (coef[N] == 0 ) return new PolynomialN(N-1).roots( roots, coef, x0, x1, xError); //check here on recursion
        else
        {
            float deriv[] = new float[N];
            derivative(deriv, coef);
            float derivRoots[] = new float[N-1];
            int nd = new PolynomialN(N-1).roots(derivRoots, deriv, xError);
            if(toBoolean(N & 1) || ( (N & 1) == 0 && nd > 0)) 
            {
                int nr = 0;
                float xa = derivRoots[0];
                float ya = eval( coef, xa );
                if (IsDifferentSign(coef[N],ya) != toBoolean(N & 1) ) {
                    roots[0] = rootFinder.findOpenMin(coef, deriv, xa, ya, xError);
                    nr = 1;
                }
                for ( int i=1; i<nd; ++i ) {
                    float xb = derivRoots[i];
                    float yb = eval(coef, xb);
                    if(IsDifferentSign(ya,yb))
                    {
                        roots[nr++] = rootFinder.findClosed(coef, deriv, xa, xb, ya, yb, xError);
                    }
                    xa = xb;
                    ya = yb;
                }
                if(IsDifferentSign(coef[N],ya)) 
                {
                    roots[nr++] = rootFinder.findOpenMax(coef, deriv, xa, ya, xError);
                }
                return nr;
            } 
            else 
            {
                if(toBoolean(N & 1))
                {
                    roots[0] = rootFinder.findOpen(coef, deriv, xError);
                    return 1;
                } 
                else 
                    return 0;	// this should not happen
            }
        }        
    }

    @Override
    public int roots(float[] roots, float[] coef, float xError) {
        if      (N == 1) return toInt(new PolynomialLinear().roots(roots, coef));
        else if (N == 2) return new PolynomialQuadratic().roots(roots, coef, xError);
        else if (N == 3) return new PolynomialCubic().roots( roots, coef, xError);
	else if (coef[N] == 0 ) return new PolynomialN(N-1).roots( roots, coef, xError); //check here on recursion
        else
        {
            float deriv[] = new float[N];
            derivative(deriv, coef );
            float derivRoots[] = new float[N-1];
            int nd = new PolynomialN(N-1).roots( derivRoots, deriv, xError );
            if(toBoolean(N & 1) || ( (N & 1) == 0 && nd > 0)) 
            {
                int nr = 0;
                float xa = derivRoots[0];
                float ya = eval( coef, xa );
                if (IsDifferentSign(coef[N],ya) != toBoolean(N & 1) ) 
                {
                    roots[0] = rootFinder.findOpenMin(coef, deriv, xa, ya, xError);
                    nr = 1;
                }
                for(int i=1; i<nd; ++i ) 
                {
                    float xb = derivRoots[i];
                    float yb = eval(coef, xb);
                    if(IsDifferentSign(ya,yb)) 
                    {
                        roots[nr++] = rootFinder.findClosed(coef, deriv, xa, xb, ya, yb, xError);
                    }
                    xa = xb;
                    ya = yb;
                }
                if(IsDifferentSign(coef[N],ya)) 
                {
                    roots[nr++] = rootFinder.findOpenMax(coef, deriv, xa, ya, xError);
                }
                return nr;
            } 
            else 
            {
                if(toBoolean(N & 1)) {
                    roots[0] = rootFinder.findOpen(coef, deriv, xError);
                    return 1;
                } 
                else 
                    return 0;	// this should not happen
            }
        }
    }

    @Override
    public boolean firstRoot(final float[] roots, float[] coef, float x0, float x1, float xError) {
        if      (N == 1) return new PolynomialLinear().roots(roots, coef, x0, x1);
        else if (N == 2) return new PolynomialQuadratic().firstRoot(roots, coef, x0, x1, xError);
        else if (N == 3) return new PolynomialCubic().firstRoot(roots, coef, x0, x1, xError);
	else if (coef[N] == 0 ) return new PolynomialN(N-1).firstRoot(roots, coef, x0, x1, xError); //check here on recursion
        else
        {
            float[] x00 = {x0};
            float[] y00 = {eval(coef, x00[0])};
            float deriv[] = new float[N];
            derivative(deriv, coef);            
            boolean done = new PolynomialN(N-1).forEachRoot((xa)->{
                float ya = eval(coef, xa);
                if(IsDifferentSign(y00[0],ya))
                {
                    roots[0] = rootFinder.findClosed( coef, deriv, x00[0], xa, y00[0], ya, xError );
                    return true;
                }
                x00[0] = xa;
                y00[0] = ya;
                return false;
            }, deriv, x00[0], x1, xError );
            if(done) 
                return true;
            else 
            {
                float y1 = eval( coef, x1);
                if(IsDifferentSign(y00[0],y1)) 
                {
                    roots[0] = rootFinder.findClosed(coef, deriv, x00[0], x1, y00[0], y1, xError);
                    return true;
                } 
                else 
                    return false;
            }
        }
    }

    @Override
    public boolean firstRoot(float[] roots, float[] coef, float xError) {
        if      (N == 1) return new PolynomialLinear().roots(roots, coef);
        else if (N == 2) return new PolynomialQuadratic().firstRoot(roots, coef,xError);
        else if (N == 3) return new PolynomialCubic().firstRoot(roots, coef, xError);
	else if (coef[N] == 0 ) return new PolynomialN(N-1).firstRoot(roots, coef, xError); //check here on recursion
        else
        {            
            float x0[] = {Float.NEGATIVE_INFINITY};
            float y0[] = {(((N&1)==0) ^ (coef[N]<0)) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY};
            boolean firstInterval[] = {true};
            float deriv[] = new float[N];
            derivative(deriv, coef);
            boolean done = new PolynomialN(N-1).forEachRoot((xa) -> {
                float ya = eval(coef, xa);
                if (IsDifferentSign(y0[0],ya)) 
                {
                    if(firstInterval[0]) 
                    {
                        roots[0] = rootFinder.findOpenMin(coef, deriv, xa, ya, xError);
                    } 
                    else 
                    {
                        roots[0] = rootFinder.findClosed( coef, deriv, x0[0], xa, y0[0], ya, xError);
                    }
                    return true;
                }
                firstInterval[0] = false;
                x0[0] = xa;
                y0[0] = ya;
                return false;
            }, deriv, xError );
            if ( done ) 
                return true;
            else {
                if((N&1)==1) 
                {
                    if (firstInterval[0]) 
                    {
                        roots[0] = rootFinder.findOpen(coef, deriv, xError);
                    } 
                    else 
                    {
                        roots[0] = rootFinder.findOpenMax(coef, deriv, x0[0], y0[0], xError);
                    }
                    return true;
                } 
                else 
                    return false;
            }
        }
    }

    @Override
    public boolean hasRoot(float[] coef, float x0, float x1, float xError) {
        if      (N == 2 )return new PolynomialQuadratic().hasRoot( coef, x0, x1, xError);
	else if (N == 3 ) return new PolynomialCubic().hasRoot( coef, x0, x1, xError);
	else if (coef[N] == 0) return new PolynomialN(N-1).hasRoot(coef, x0, x1, xError);
	else 
        {
            float y0 = eval(coef, x0);
            float y1 = eval(coef, x1);
            if ( IsDifferentSign(y0,y1) ) return true;
            boolean foundRoot = false;
            float[] deriv = new float[N];
            derivative(deriv, coef);
            return new PolynomialN(N-1).forEachRoot((xa)->{
                float ya = eval( coef, xa );
                return ( IsDifferentSign(y0,ya) );
            }, deriv, x0, x1, xError);
	}
    }

    @Override
    public boolean hasRoot(float[] coef, float xError) {
        if      (N == 2) return new PolynomialQuadratic().hasRoot(coef, xError);
	else if (N == 3) return new PolynomialCubic().hasRoot(coef, xError);
	else if (coef[N] == 0) return new PolynomialN(N-1).hasRoot(coef, xError);
	else if ((N&1) == 1 ) return true;
	else 
        {
            float y0 = ( coef[N]<0 ) ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
            float[] deriv = new float[N];
            derivative( deriv, coef );
            return new PolynomialN(N-1).forEachRoot((xa )->{
                float ya = eval( coef, xa );
                return IsDifferentSign(y0,ya);
            }, deriv, xError );
	}
    }

    @Override
    public boolean forEachRoot(RootCallBackFloat roots, float[] coef, float x0, float x1, float xError) {
        if      (N == 2) return new PolynomialQuadratic().forEachRoot(roots, coef, x0, x1, xError);
	else if (N == 3) return new PolynomialCubic().forEachRoot(roots, coef, x0, x1, xError);
	else if (coef[N] == 0) return new PolynomialN(N-1).forEachRoot(roots, coef, x0, x1, xError );
	else
        {
            float[] x00 = {x0};
            float[] y0 = {eval( coef, x00[0] )};
            float[] deriv = new float[N];
            derivative(deriv, coef);
            boolean done = new PolynomialN(N-1).forEachRoot((xa)->{
                    float ya = eval(coef, xa);
                    if (IsDifferentSign(y0[0],ya)) 
                    {
                        float[] root = new float[1];
                        root[0] = rootFinder.findClosed( coef, deriv, x00[0], xa, y0[0], ya, xError );
                        if (roots.call(root[0])) 
                            return true;
                    }
                    x00[0] = xa;
                    y0[0] = ya;
                    return false;
            }, deriv, x00[0], x1, xError );
            if(done) 
                return true;
            else 
            {
                float y1 = eval(coef, x1);
                if(IsDifferentSign(y0[0],y1)) 
                {
                    return roots.call( rootFinder.findClosed( coef, deriv, x00[0], x1, y0[0], y1, xError ) );
                }
                return false;
            }
	}
    }

    @Override
    public boolean forEachRoot(RootCallBackFloat callback, float[] coef, float xError) {
        if      (N == 2) return new PolynomialQuadratic().forEachRoot(callback, coef, xError);
	else if (N == 3 ) return new PolynomialCubic().forEachRoot( callback, coef, xError);
	else if (coef[N] == 0 ) return new PolynomialN(N-1).forEachRoot(callback, coef, xError);
	else 
        {
            float[] x0 = {Float.NEGATIVE_INFINITY};
            float[] y0 = {( ((N&1)==0) ^ (coef[N]<0) ) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY};
            boolean[] firstInterval = {true};
            float[] deriv = new float[N];
            derivative( deriv, coef );
            boolean done = new PolynomialN(N-1).forEachRoot((xa)->{
                float ya = eval(coef, xa);
                if(IsDifferentSign(y0[0],ya)) 
                {
                    float[] root = new float[1];
                    if(firstInterval[0]) 
                    {
                        root[0] = rootFinder.findOpenMin(coef, deriv, xa, ya, xError);
                    } 
                    else 
                    {
                        root[0] = rootFinder.findClosed(coef, deriv, x0[0], xa, y0[0], ya, xError);
                    }
                    if (callback.call(root[0])) 
                        return true;
                }
                firstInterval[0] = false;
                x0[0] = xa;
                y0[0] = ya;
                return false;
            }, deriv, xError );
            if(done) 
                return true;
            else 
            {
                if (IsDifferentSign(y0[0],coef[N]) )
                {
                    float[] root = new float[1];
                    if((N&1)==1) 
                    {
                        if ( firstInterval[0] ) 
                        {
                                root[0] = rootFinder.findOpen(coef, deriv, xError);
                        } else {
                                root[0] = rootFinder.findOpenMax(coef, deriv, x0[0], y0[0], xError);
                        }
                    } 
                    else 
                    {
                        root[0] = rootFinder.findOpenMax(coef, deriv, x0[0], y0[0], xError);
                    }
                    return callback.call(root[0]);
                }
                return false;
            }
	}
    }    
}
