/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.surface;

import coordinate.generic.VCoord;
import coordinate.utility.Value1Df;
import coordinate.utility.Value2Df;
import static java.lang.Float.max;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 *
 * @author user
 * @param <V>
 */
public abstract class Microfacet<V extends VCoord> {
    public enum MType{
        GGX, Phong, Beckmann
    }
    
    public abstract MType getType();
    public abstract float getAlpha();
    public abstract float getAlphaU();
    public abstract float getAlphaV();
    
    //microfacet distribution (m is microsurface normal)
    public abstract float evaluate(V m);
    //sample H which is the half vector between wi and wo
    public abstract V sample(V wi, Value2Df sample, Value1Df pdf);
    //pdf of sampling wi given wi and m
    public abstract float pdf(V wi, V m);
    
    // Input Ve: view direction
    // Input alpha_x, alpha_y: roughness parameters
    // Input U1, U2: uniform random numbers
    // Output Ne: normal sampled with PDF D_Ve(Ne) = G1(Ve) * max(0, dot(Ve, Ne)) * D(Ne) / Ve.z
    // VNDF Sampling
    public final V sampleVisible(V wi, Value2Df sample)
    {
        float m_alphaU = this.getAlphaU(), m_alphaV = this.getAlphaV();
        
        // Section 3.2: transforming the view direction to the hemisphere configuration
        V Vh    = (V) wi.newV(m_alphaU * wi.get('x'), m_alphaV * wi.get('y'), wi.get('z')).normalize();

        // Section 4.1: orthonormal basis (with special case if cross product is zero)
        float lensq     = Vh.get('x') * Vh.get('x') + Vh.get('y') * Vh.get('y');
        //Vector3_b T1       = select((float4)(1, 0, 0, 0), (float4)(-Vh.y, Vh.x, 0, 0) * rsqrt(lensq), (int4)((lensq > 0) <<31)); 
        V T1    = lensq > 0 ? (V)wi.newV(-Vh.get('y'), Vh.get('x'), 0).div((float)Math.sqrt(lensq)) : (V)wi.newV(1, 0, 0);
        V T2    = (V) Vh.cross(T1);

        // Section 4.2: parameterization of the projected area
        float r         = (float) sqrt(sample.x);
        float phi       = (float) (2.0 * PI * sample.y);
        float t1        = (float) (r * cos(phi));
        float t2        = (float) (r * sin(phi));
        float s         = (float) (0.5 * (1.0 + Vh.get('z')));
        t2              = (float) ((1.0 - s)*sqrt(1.0 - t1*t1) + s*t2);

        // Section 4.3: reprojection onto hemisphere
        //Vector3_b Nh    = t1*T1 + t2*T2 + sqrt(max(0.f, 1.f - t1*t1 - t2*t2))*Vh;
        V Nh    = (V) T1.mul(t1).add(T2.mul(t2)).add(Vh.mul((float)sqrt(max(0.f, 1.f - t1*t1 - t2*t2))));

        // Section 3.4: transforming the normal back to the ellipsoid configuration
        V Ne       = (V) wi.newV(m_alphaU * Nh.get('x'), m_alphaV * Nh.get('y'), max(0.f, Nh.get('z'))).normalize();
        return Ne;
    }
    
    public final float pdfVisible(V wi, V m)
    {
        if(Frame.cosTheta(wi) == 0)
            return 0.0f;

        float G1 = G1(wi);
        if(!Float.isFinite(G1))
            return 0.0f;
        return VNDF(wi, m);
    }
    
     //distribution of visible normals(VNDF) or Dv(N)
    protected float VNDF(V v, V m)
    {
       return G1(v)* max(0.001f, v.dot(m)) * evaluate(m)/max(0.001f, v.get('z'));
    }
    
    protected float G1(V w)
    {
        return 1.f/(1.f + lambda(w, this.getAlphaU(), this.getAlphaV()));
    }
    
    //trowbridge-reitz distribution lambda
    protected float lambda(V w, float ax, float ay)
    {
        float absTanTheta = abs(Frame.tanTheta(w));
        float a           = alpha(w, ax, ay);
        float a2tan2theta = (a * absTanTheta) * (a * absTanTheta);
        float lam         = (float) ((-1.f + sqrt(1.f + a2tan2theta))/2);
        return Float.isInfinite(absTanTheta) ? 0 : lam ;
    }
    
    protected float alpha(V w, float ax, float ay)
    {
        return (float) sqrt(Frame.cos2Phi(w) * ax * ax + Frame.sin2Phi(w) * ay * ay);
    }
}
