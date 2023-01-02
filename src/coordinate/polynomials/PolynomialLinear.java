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
public class PolynomialLinear{
    public boolean roots(float[] root, float coef[], float x0, float x1) //coef[2]
    {
        if ( coef[1] != (float)(0) ) {
            float r = -coef[0]/coef[1];
            root[0] = r;
            return ( r >= x0 && r <= x1 );
        } 
        else 
        {
            root[0] = (x0 + x1) / 2;
            return coef[0] == 0;
        }
    }
    
    public boolean roots(float[] root, float coef[]) //coef[2]
    {
        root[0] = -coef[0]/coef[1]; 
        return coef[1]!=0; 
    }

}
