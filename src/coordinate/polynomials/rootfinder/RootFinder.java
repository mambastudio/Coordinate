/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.polynomials.rootfinder;

/**
 *
 * @author user
 */
public abstract class RootFinder {
    public abstract float findClosed(
            float[] coef,   //coef[N+1]
            float[] deriv,  //deriv[N]
            float   x0, 
            float   x1, 
            float   y0, 
            float   y1, 
            float   xError);
    
    public abstract float findOpen(
            float[] coef,   //coef[N+1]
            float[] deriv,  //deriv[N]
            float   xs, 
            float   ys, 
            float   xr, 
            float   xError);
    
    public abstract float findOpen(
            float[] coef,   //coef[N+1]
            float[] deriv,  //deriv[N]           
            float   xError);
    
    public abstract float findOpenMin(
            float[] coef,   //coef[N+1]
            float[] deriv,  //deriv[N]            
            float   x1,            
            float   y1, 
            float   xError);
    
    public abstract float findOpenMax(
            float[] coef,   //coef[N+1]
            float[] deriv,  //deriv[N]            
            float   x0,            
            float   y0, 
            float   xError);
}
