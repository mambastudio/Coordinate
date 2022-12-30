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
    protected int N;
    protected boolean boundError;
    
    protected RootFinder(int N, boolean boundError)
    {
        this.N = N;
        this.boundError = boundError;
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
