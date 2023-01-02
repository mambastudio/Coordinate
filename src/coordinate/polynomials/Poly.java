/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.polynomials;

import coordinate.utility.Value1Df;

/**
 *
 * @author user
 */
public abstract class Poly {
    public abstract int getN();
    public abstract float[] getCoefficients();
    public boolean areCoefficientsLengthValid()
    {
        return getCoefficients().length == getN() + 1; //coefN = N+1
    }
    
    public void throwExceptionIfCoefficientsAreInvalid()
    {
        if(!areCoefficientsLengthValid())
            throw new UnsupportedOperationException("Invalid number of coefficents. "
                    + "This is a degree " +getN()+ " polynomial. This requires " 
                    + getN()+1 +" coefficients. " + "We have " 
                    + getCoefficients().length+ " coefficients.");
    }
    public abstract boolean roots(Value1Df root, float coef[], float x0, float x1);
}
