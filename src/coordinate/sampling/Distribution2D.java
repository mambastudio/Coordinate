/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.sampling;


import coordinate.utility.Value1Df;
import coordinate.utility.Value2Df;
import coordinate.utility.Value2Di;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Distribution2D{
    private final ArrayList<Distribution1D> pConditionalV;
    private final Distribution1D pMarginal;
    
    public Distribution2D(float[] func, int nu, int nv) 
    {
        pConditionalV = new ArrayList<>(nv);
        for (int v = 0; v < nv; ++v) {
            // Compute conditional sampling distribution for $\tilde{v}$
            pConditionalV.add(new Distribution1D(func, v * nu, nu));
        }
        
        // Compute marginal sampling distribution $p[\tilde{v}]$
        float[] marginalFunc;
        marginalFunc = new float[nv];
        for (int v = 0; v < nv; ++v) {
            marginalFunc[v] = pConditionalV.get(v).funcInt;
        }        
        pMarginal = new Distribution1D(marginalFunc, 0, nv);        
    }
    
    public Value2Df sampleContinous(float u0, float u1)
    {
        return sampleContinuous(u0, u1, null);
    }
    
    public Value2Df sampleContinuous(float u0, float u1, Value1Df pdf)
    {        
        Value2Df uv = new Value2Df();
        sampleContinuous(u0, u1, uv, pdf);
        return uv;
    }
    
    public void sampleContinuous(float u0, float u1, Value2Df uv, Value1Df pdf)
    {
        float[] uvTemp = new float[2];
        
        sampleContinuous(u0, u1, uvTemp, pdf);
        
        uv.x = uvTemp[0];
        uv.x = uvTemp[1];
    }
    
    public void sampleContinuous(float u0, float u1, float[] uv,
            Value1Df pdf) 
    {        
        Value1Df pdfTemp = new Value1Df();        
        float[] pdfs = new float[2];
        
        int[] v = new int[1];
        uv[1] = pMarginal.sampleContinuous(u1, pdfTemp, v);
        pdfs[1] = pdfTemp.x;
        uv[0] = pConditionalV.get(v[0]).sampleContinuous(u0, pdfTemp);
        pdfs[0] = pdfTemp.x;
        
        if(pdf != null)
            pdf.x = pdfs[0] * pdfs[1];
    }
    
    public Value2Di sampleDiscrete(float u0, float u1)
    {
        return sampleDiscrete(u0, u1, null);
    }
    
    public Value2Di sampleDiscrete(float u0, float u1, Value1Df pdf)
    {
        Value2Di uv = new Value2Di();
        sampleDiscrete(u0, u1, uv, pdf);
        return uv;
    }
    
    public void sampleDiscrete(float u0, float u1, Value2Di uv, Value1Df pdf)
    {
        int[] uvTemp = new int[2];
        
        sampleDiscrete(u0, u1, uvTemp, pdf);
                
        uv.x = uvTemp[0];
        uv.y = uvTemp[1];
    }
    
    public void sampleDiscrete(float u0, float u1, int[] uv,
            Value1Df pdf)
    {
        Value1Df pdfTemp = new Value1Df();
        float[] pdfs = new float[2];
        
        uv[1] = pMarginal.sampleDiscrete(u1, pdfTemp);        
        pdfs[1] = pdfTemp.x;
        uv[0] = pConditionalV.get(uv[1]).sampleDiscrete(u0, pdfTemp);        
        pdfs[0] = pdfTemp.x;
       
        if(pdf != null)
            pdf.x = pdfs[0] * pdfs[1];
    }
    
    public float pdfDiscrete(Value2Di uv)
    {
        return Distribution2D.this.pdfDiscrete(uv.x, uv.y);
    }
    
    public float pdfContinuous(Value2Df uv)
    {
        return Distribution2D.this.pdfContinuous(uv.x, uv.y);
    }
    
    public float pdfDiscrete(int u, int v)
    {       
        float pdfU = pConditionalV.get(v).pdfDiscrete(u);
        float pdfV = pMarginal.pdfDiscrete(v);
                
        return pdfU * pdfV;        
    }
    
    public float pdfContinuous(float u, float v) 
    {        
        int iv = (int)(v * pMarginal.count);
        
        float pdfU = pConditionalV.get(iv).pdfContinuous(u);
        float pdfV = pMarginal.pdfContinuous(v);
        
        return pdfU * pdfV;
    }
    
    public String pMarginalFuncString()
    {
        return pMarginal.funcString();
    }
    
    public String pMarginalCdfString()
    {
        return pMarginal.cdfString();
    }
    
    public String pConditionalVFuncString()
    {
        StringBuilder builder = new StringBuilder();
        
        for(int i = 0; i<pConditionalV.size(); i++)
        {
            Distribution1D dist = pConditionalV.get(i);
            
            if(i == pConditionalV.size()-1)
                builder.append(dist.funcString());
            else
                builder.append(dist.funcString()).append("\n");
        }      
        
        return builder.toString();
    }
    
    public String pConditionalVCdfString()
    {
        StringBuilder builder = new StringBuilder();
        
        for(Distribution1D dist : pConditionalV)        
            builder.append(dist.cdfString()).append("\n");
                    
        return builder.toString();
    }
}
