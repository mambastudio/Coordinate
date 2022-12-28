/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.surface.microfacet;

import coordinate.surface.frame.Frame;
import coordinate.generic.VCoord;
import static coordinate.surface.microfacet.Microfacet.MType.GGX;
import coordinate.utility.Value1Df;
import coordinate.utility.Value2Df;
import static java.lang.Math.PI;

/**
 *
 * @author user
 * @param <V>
 */
public class MicrofacetGGX<V extends VCoord> extends Microfacet<V> {
    private final float ax;
    private final float ay;
    
    public MicrofacetGGX(float ax, float ay)
    {
        this.ax = ax;
        this.ay = ay;
    }

    @Override
    public MType getType() {
        return GGX;
    }

    @Override
    public float getAlpha() {
        return ax; //if isotropic
    }

    @Override
    public float getAlphaU() {
        return ax;
    }

    @Override
    public float getAlphaV() {
        return ay;
    }

    @Override
    public float evaluate(V m) {
        float tan2Theta = Frame.tan2Theta(m);
        float cos4Theta = Frame.cos2Theta(m) * Frame.cos2Theta(m);
        float e         = (Frame.cos2Phi(m) / (ax * ax) +
                           Frame.sin2Phi(m) / (ay * ay)) * tan2Theta;
        float d         = (float) (1.f / (PI * ax * ay * cos4Theta * (1.f + e) * (1.f + e)));
        return Float.isInfinite(tan2Theta) ? 0.f : d;
    }
   
    @Override
    public V sample(V wi, Value2Df sample, Value1Df pdf) 
    {
        V ni = this.sampleVisible(wi, sample);
        if(pdf != null)
            pdf.x = pdf(wi, ni);
        return ni;
    }

    @Override
    public float pdf(V v, V ni) {
        float DvNi = VNDF(v, ni); //or pdfVisible
        float VNi  = v.dot(ni);        
        return VNi == 0.0f ? 0.0f : DvNi/(4 * VNi);
    }
}
