/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.shapes.polynomials.roots;

import java.util.function.Function;

/**
 *
 * @author jmburu
 */
public class PolynomialRoots {
    public PolynomialRoots(
            int N,
            float ftype,
            boolean boundingError,
            RootFinderNewton rootFinder,
            Function<Boolean, Float> rootCallBack) 
    { 
        
    }
    
    public int polynomialAllRoots(
           float[] roots,
           float[] coef,
           float xMin,
           float xMax,
           float xError
    )
    {
        return -1;
    }
}
