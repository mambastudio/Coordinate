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
    protected boolean boundingError;
    protected RootFinder rootFinder;
    protected RootCallBackFloat rootCallBack;

    protected Polynomials(
            int N,
            boolean boundingError,
            RootFinder rootFinder,
            RootCallBackFloat rootCallBack) 
    { 
        this.N = N;
        this.boundingError = boundingError;
        this.rootFinder = rootFinder;
        this.rootCallBack = rootCallBack;
    }
    
    //! Evaluates the given polynomial of degree `N` at `x`.
    //! 
    //! The coefficients in the `coef` array must be in the order of increasing degrees.
    public abstract float eval(            
            float[]     coef,       // coef[N+1] polynomial's coefficients
            float       x
    );
    
    //! Evaluates the given polynomial and its derivative at `x`.
    //! 
    //! This function does not require computing the derivative of the polynomial,
    //! but it is slower than evaluating the polynomial and its precomputed derivative separately.
    //! Therefore, it is not recommended when the polynomial and it derivative are computed repeatedly.
    //! The coefficients in the `coef` array must be in the order of increasing degrees.
    public abstract float evalWithDeriv(
            Value1Df    derivativeValue,
            float[]     coef,       // coef[N+1] polynomial's coefficients
            float       x
    );
    
    //! Computes the polynomial's derivative and sets the coefficients of the `deriv` array.
    //! 
    //! Note that a degree N polynomial's derivative is degree `N-1` and has `N` coefficients.
    //! The coefficients are in the order of increasing degrees.
    public abstract void derivative(
            float[]     deriv,      // deriv[N]
            float[]     coef        // coef[N+1] polynomial's coefficients
    );
    
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
    public abstract void deflate(
            float[]     defPoly,    // defPoly[N]
            float[]     coef,       // coef[N+1] polynomial's coefficients
            float       root
    );
    
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
    public abstract void inflate(
            float[]     infPoly,    // inflPoly[N+2]
            float[]     coef,       // coef[N+1] polynomial's coefficients
            float       root
    );
    
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
            RootCallBackFloat               roots,   // roots[N] to be returned
            float[]                         coef,   // coef[N+1] polynomial's coefficients
            float                           xMin,   // minimum x value for the interval
            float                           xMax,   // maxinum x value for the interval
            float                           xError  // the error threshold
    );
    
    public abstract boolean forEachRoot(
            RootCallBackFloat               roots,  // roots[N] to be returned
            float[]                         coef,   // coef[N+1] polynomial's coefficients           
            float                           xError  // the error threshold
    );
}
