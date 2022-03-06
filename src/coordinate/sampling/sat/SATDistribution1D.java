/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.sampling.sat;

import coordinate.utility.Value1Df;
import static java.lang.Math.max;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class SATDistribution1D {
    private final int n;
    private final SAT sat;
    
    public SATDistribution1D(float... f)
    {
        sat = new SAT(f.length, 1);
        sat.setArray(f);
        n = f.length;
    }
    
    public void setRegionX(int minX, int maxX)
    {
        sat.setRegion(minX, 0, maxX, 1);
    }
    
    public float sampleContinuous(float u, float[] pdf, int[] off)
    {
        Value1Df pdfValue = new Value1Df();
        float u_ = sampleContinuous(u, pdfValue, off);
        if(pdf != null)
            pdf[0] = pdfValue.x;
        return u_;
    }
    
    public float sampleContinuous(float u, Value1Df pdf, int[] off)
    {
        float[] pdfS = new float[1];
        float[] uvS = new float[2];
        int[] offS = new int[2];
        sat.sampleContinuous(u, 1, uvS, offS, pdfS);
        if(pdf!=null)
            pdf.x = pdfS[0];
        if(off!= null)
            off[0] = offS[0];
        return uvS[0];
    }
    
    public int sampleDiscreteConditional(float u)
    {
        return sat.sampleDiscreteConditional(u, 0);
    }
    
    public float sampleContinuousConditional(float u)
    {
        return sat.sampleContinuousConditional(u, 0, null, new float[]{0});
    }
    
    public int sampleDiscrete(float u, float[] pdf)
    {        
        return sat.sampleDiscreteConditional(u, 0, pdf);
    }
    
    public float getFuncIntConditional()
    {
        return sat.getFuncIntConditional(0);
    }
        
    public float[] cdf()
    {
        float arr[] = new float[n + 1];
        for(int i = 0; i < arr.length; i++)
            arr[i] = sat.getConditional(i, 0);
        return arr;
    }
    
    public String cdfString()
    {
        return Arrays.toString(cdf());
    }
}
