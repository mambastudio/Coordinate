/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.polynomials;

import coordinate.function.RootCallBackFloat;
import coordinate.polynomials.rootfinder.RootFinder;
import coordinate.utility.Value1Df;

/**
 *
 * @author https://github.com/cemyuksel/cyCodeBase/blob/master/cyPolynomial.h
 */
public abstract class Polynomials {        
    protected int N;   
    protected boolean boundError;
    protected RootFinder rootFinder;
    

    protected Polynomials(
            int N,
            boolean boundingError,
            RootFinder rootFinder,
            RootCallBackFloat rootCallBack) 
    { 
        this.N = N;
        this.boundError = boundingError;
        this.rootFinder = rootFinder;
    }
    
    public boolean IsDifferentSign( float a, float b )    
    { 
        return a<0 != b<0; 
    }	
    
    public float MultSign(float v, float sign)
    {
        return v * (sign<0 ? (float)(-1) : (float)(1));
    }
    
    public int toInt(boolean value)
    {
        if(value) return 1;
        else return 0;
    }
    
    public boolean toBoolean(int value)
    {
        return (value != 0);
    }
    
    //! Evaluates the given polynomial of degree `N` at `x`.
    //! 
    //! The coefficients in the `coef` array must be in the order of increasing degrees.   
    public float eval(
            float[] coef, // coef[N+1] polynomial's coefficients
            float x) 
    {
        float r = coef[N]; 
        for ( int i=N-1; i>=0; --i ) 
            r = r*x + coef[i]; 
        return r;
    }
    
    //! Evaluates the given polynomial and its derivative at `x`.
    //! 
    //! This function does not require computing the derivative of the polynomial,
    //! but it is slower than evaluating the polynomial and its precomputed derivative separately.
    //! Therefore, it is not recommended when the polynomial and it derivative are computed repeatedly.
    //! The coefficients in the `coef` array must be in the order of increasing degrees.    
    public float evalWithDeriv(
            Value1Df derivativeValue, 
            float[] coef,               // coef[N+1] polynomial's coefficients
            float x) {
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
    
    //! Computes the polynomial's derivative and sets the coefficients of the `deriv` array.
    //! 
    //! Note that a degree N polynomial's derivative is degree `N-1` and has `N` coefficients.
    //! The coefficients are in the order of increasing degrees.    
    public void derivative(
            float[] deriv,          // deriv[N]
            float[] coef            // coef[N+1] polynomial's coefficients
    ) 
    {
        deriv[0] = coef[1]; 
        for ( int i=2; i<=N; ++i ) 
            deriv[i-1] = i * coef[i]; 
    }
    
    //! Deflates the given polynomial using one of its known roots.
    //! 
    //! Stores the coefficients of the deflated polynomial in the `defPoly` array.
    //! Let f(x) represent the given polynomial.
    //! This computes the deflated polynomial g(x) of a lower degree such that
    //! 
    //! `f(x) = (x - root) * g(x)`.
    //! 
    //! The given root must be a valid root of the given polynomial.
    //! Note that the deflated polynomial has degree `N-1` with `N` coefficients.
    //! The coefficients are in the order of increasing degrees.    
    public void deflate(
            float[] defPoly,        // defPoly[N]
            float[] coef,           // coef[N+1] polynomial's coefficients
            float root
    ) {
        defPoly[N-1] = coef[N];
	for ( int i=N-1; i> 0; --i ) 
            defPoly[i-1] = coef[i] + root*defPoly[i];
    }
    
    //! Inflates the given polynomial using the given root.
    //! 
    //! Stores the coefficients of the inflated polynomial in the `infPoly` array.
    //! Let f(x) represent the given polynomial.
    //! This computes the inflated polynomial g(x) of a higher degree such that
    //! 
    //! `g(x) = (x - root) * f(x)`.
    //! 
    //! Note that the inflated polynomial has degree `N+1` with `N+2` coefficients.
    //! The coefficients are in the order of increasing degrees.    
    public void inflate(
            float[] infPoly,        // inflPoly[N+2]
            float[] coef,           // coef[N+1] polynomial's coefficients
            float root) {
        infPoly[N+1] = coef[N];
	for ( int i=N-1; i>=0; --i ) 
            infPoly[i+1] = coef[i] - root*infPoly[i+1];
	infPoly[0] = -root*coef[0];
    }
    
    public abstract int roots(
            float[]     roots,      // roots[N] to be returned
            float[]     coef,       // coef[N+1] polynomial's coefficients
            float       xMin,       // minimum x value for the interval
            float       xMax,       // maxinum x value for the interval
            float       xError      // the error threshold
    );
    
    public abstract int roots(
            float[]     roots,      // roots[N] to be returned
            float[]     coef,       // coef[N+1] polynomial's coefficients            
            float       xError      // the error threshold
    );
    
    public abstract boolean firstRoot(
            float[]     roots,      // roots[N] to be returned
            float[]     coef,       // coef[N+1] polynomial's coefficients
            float       xMin,       // minimum x value for the interval
            float       xMax,       // maxinum x value for the interval
            float       xError      // the error threshold
    );
    
    public abstract boolean firstRoot(
            float[]     roots,      // roots[N] to be returned
            float[]     coef,       // coef[N+1] polynomial's coefficients            
            float       xError      // the error threshold
    );
    
    public abstract boolean hasRoot(            
            float[]     coef,       // coef[N+1] polynomial's coefficients
            float       xMin,       // minimum x value for the interval
            float       xMax,       // maxinum x value for the interval
            float       xError      // the error threshold
    );
    
    public abstract boolean hasRoot(            
            float[]     coef,       // coef[N+1] polynomial's coefficients            
            float       xError      // the error threshold
    );
    
    public abstract boolean forEachRoot(
            RootCallBackFloat               callback,   // roots[N] to be returned
            float[]                         coef,   // coef[N+1] polynomial's coefficients
            float                           xMin,   // minimum x value for the interval
            float                           xMax,   // maxinum x value for the interval
            float                           xError  // the error threshold
    );
    
    public abstract boolean forEachRoot(
            RootCallBackFloat               callback,  // roots[N] to be returned
            float[]                         coef,   // coef[N+1] polynomial's coefficients           
            float                           xError  // the error threshold
    );
}
