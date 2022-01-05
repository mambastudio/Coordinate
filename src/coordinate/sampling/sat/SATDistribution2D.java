/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.sampling.sat;

/**
 *
 * @author user
 */
public class SATDistribution2D {
    private final SAT sat;
    
    public SATDistribution2D(float[] func, int nu, int nv) 
    {
        this.sat = new SAT(nu, nv);
        this.sat.setArray(func);
    }
    
    public void sampleContinuous(float u0, float u1, float[] uv, float[] pdf)
    {       
        sat.sampleContinuous(u0, u1, uv, null, pdf);
    }
    
    public void sampleDiscrete(float u0, float u1, int[] offset, float[] pdf)
    {
        sat.sampleDiscrete(u0, u1, offset, pdf);
    }
    
    public SAT getSat()
    {
        return sat;
    }
}
