/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.polynomials;

import coordinate.function.RootCallBackFloat;
import coordinate.polynomials.rootfinder.RootFinder;
import coordinate.polynomials.rootfinder.RootFinderNewton;
import coordinate.utility.Value1Df;

/**
 *
 * @author user
 */
public class PolynomialN extends Polynomials{

    public PolynomialN(int N, boolean boundingError, RootFinder rootFinder, RootCallBackFloat rootCallBack) {
        super(N, boundingError, rootFinder, rootCallBack);
    }
    
    public PolynomialN(int N)
    {
        this(N, false, new RootFinderNewton(N, false), null);
    }

    @Override
    public float eval(float[] coef, float x) 
    {
        float r = coef[N]; 
        for ( int i=N-1; i>=0; --i ) 
            r = r*x + coef[i]; 
        return r;
    }

    @Override
    public float evalWithDeriv(Value1Df derivativeValue, float[] coef, float x) {
        if ( N < 1 ) 
        { 
            derivativeValue.x = 0; 
            return coef[0]; }
	else 
        {
            float  p = coef[N]*x + coef[N-1];
            float dp = coef[N];
            for ( int i=N-2; i>=0; --i ) 
            {
                dp = dp*x + p;
                p  =  p*x + coef[i];
            }
            derivativeValue.x = dp;
            return p;
	}
    }

    @Override
    public void derivative(float[] deriv, float[] coef) {
        deriv[0] = coef[1]; 
        for ( int i=2; i<=N; ++i ) 
            deriv[i-1] = i * coef[i]; 
    }

    @Override
    public void deflate(float[] defPoly, float[] coef, float root) {
        defPoly[N-1] = coef[N];
	for ( int i=N-1; i> 0; --i ) 
            defPoly[i-1] = coef[i] + root*defPoly[i];
    }

    @Override
    public void inflate(float[] infPoly, float[] coef, float root) {
        infPoly[N+1] = coef[N];
	for ( int i=N-1; i>=0; --i ) 
            infPoly[i+1] = coef[i] - root*infPoly[i+1];
	infPoly[0] = -root*coef[0];
    }

    @Override
    public int roots(float[] roots, float[] coef, float xMin, float xMax, float xError) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int roots(float[] roots, float[] coef, float xError) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean firstRoot(float[] roots, float[] coef, float xMin, float xMax, float xError) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean firstRoot(float[] roots, float[] coef, float xError) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasRoot(float[] coef, float xMin, float xMax, float xError) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasRoot(float[] coef, float xError) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean forEachRoot(RootCallBackFloat roots, float[] coef, float xMin, float xMax, float xError) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean forEachRoot(RootCallBackFloat roots, float[] coef, float xError) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
