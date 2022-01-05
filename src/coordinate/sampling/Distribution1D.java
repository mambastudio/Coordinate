/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.sampling;

import static coordinate.utility.Utility.clamp;
import static coordinate.utility.Utility.debugArray;
import coordinate.utility.Value1Df;
import static java.lang.Math.max;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Distribution1D {
    float [] func, cdf;
    public float funcInt;
    int count;
    
    public Distribution1D(float... f)
    {
        this(f, 0, f.length);
    }
    
    public Distribution1D(float [] f, int offset, int n)
    {
        count = n;
        func = new float[n];
        System.arraycopy(f, offset, func, 0, n);
        cdf = new float[n + 1];
        
        
        //Compute integral of step function at xi
        cdf[0] = 0;
        for(int i = 1; i < count+1; ++i)
            cdf[i] = cdf[i-1] + func[i-1] / n;
              
        //Transform step function integral into CDF
        funcInt = cdf[count];
        if(funcInt > 0)
        {
            for (int i = 1; i < n+1; ++i)
                cdf[i] /= funcInt;
        }    
        
       debugArray(cdf);
    }
    
    public float sampleContinuous(float u, Value1Df pdf, int[] off)
    {
        // Find surrounding CDF segments and _offset_
        int ptr = upper_bound(cdf, 0, count, u);
        int offset = max(0, ptr - 1);
        if (off != null) {
            off[0] = offset;
        }
                
        // Compute offset along CDF segment
        float du = (u - cdf[offset]) / (cdf[offset + 1] - cdf[offset]);
        
        // Compute PDF for sampled offset
        if (pdf != null) {
            pdf.x = func[offset] / funcInt;       
        }
        

        // Return $x\in{}[0,1)$ corresponding to sample
        return (offset + du) / count;        
    }
    
    public float pdfDiscrete(int u)
    {
        int offset = clamp(u, 0, count-1);  
        
        if(funcInt == 0)
            return 0f;
        else
            return func[offset] / (funcInt * count);   
    }
    
    public float pdfContinuous(float u) 
    {
        // Find surrounding CDF segments and _offset_
        int ptr = upper_bound(cdf, 0, count, u);
        int offset = max(0, ptr - 1);
        
        // Compute PDF for sampled offset
        if(funcInt == 0)
            return 0f;
        else
            return func[offset] / funcInt;        
    }
    
    public float sampleContinuous(float u, Value1Df pdf)
    {
        return sampleContinuous(u, pdf, null);
    }
    
    public int sampleDiscrete(float u, Value1Df pdf)
    {
        // Find surrounding CDF segments and _offset_
        int ptr = upper_bound(cdf, 0, count, u);
        int offset = max(0, ptr - 1);
        
        if (pdf != null) {
            pdf.x = func[offset] / (funcInt * count);
        }
        return offset;        
    }
    
    public int sampleDiscrete(float u, float[] pdf)
    {
        Value1Df pdfV = new Value1Df();
        int index = sampleDiscrete(u, pdfV);
        if(pdf != null)
            pdf[0] = pdfV.x;
        return index;
    }
    
    private int upper_bound(float[] a, int first, int last, float value) 
    {
        int i;
        for (i = first; i < last; i++) {
            if (a[i] > value) {
                break;
            }
        }
        return i;
    }
    
    public String funcString()
    {
        return Arrays.toString(func);
    }
    
    public String cdfString()
    {
        return Arrays.toString(cdf);
    }
    
    public void printlnCDF()
    {
        System.out.println(cdfString());
    }
}
