/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polynomials;

import coordinate.polynomials.PolynomialCubic;
import coordinate.polynomials.PolynomialN;
import coordinate.polynomials.PolynomialQuadratic;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class TestPolynomials {
    public static void main(String... args)
    {
        PolynomialN cubic = new PolynomialN(4);
        float coeff[] = {1080, -126, -123, 7, 3}; //c, b, a for a*x^2 + bx + c
        float roots[] = new float[4];
        
        if(cubic.hasRoot(coeff, 0))
            cubic.roots(roots, coeff, 0);
        
        System.out.println(Arrays.toString(roots));
    }
    
    public void testQuadratic()
    {
        PolynomialQuadratic quadratic = new PolynomialQuadratic();
        float coeff[] = {16, -40, 25}; //c, b, a for a*x^2 + bx + c
        float roots[] = new float[2];
        
        if(quadratic.hasRoot(coeff, 0))
            quadratic.roots(roots, coeff, 0);
        
        System.out.println(Arrays.toString(roots));
    }
}
