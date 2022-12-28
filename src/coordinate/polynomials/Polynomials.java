/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.polynomials;

import coordinate.function.RootCallBackFloat;
import coordinate.polynomials.rootfinder.RootFinder;

/**
 *
 * @author https://github.com/cemyuksel/cyCodeBase/blob/master/cyPolynomial.h
 */
public abstract class Polynomials {        
    protected int N;
    protected float ftype;
    protected boolean boundingError;
    protected RootFinder rootFinder;
    protected RootCallBackFloat rootCallBack;

    protected Polynomials(
            int N,
            float ftype,
            boolean boundingError,
            RootFinder rootFinder,
            RootCallBackFloat rootCallBack) 
    { 
        this.N = N;
        this.ftype = ftype;
        this.boundingError = boundingError;
        this.rootFinder = rootFinder;
        this.rootCallBack = rootCallBack;
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
