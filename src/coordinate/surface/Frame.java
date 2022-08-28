/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.surface;

import coordinate.generic.VCoord;
import coordinate.utility.Utility;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author user
 * @param <V>
 */
public class Frame<V extends VCoord<V>> implements AbstractFrame<V> {
    private V mX, mY, mZ;
    private V n;
    
    public Frame(V n)
    {
        setFromZ(n);
    }
    
    @Override
    public void setFromZ(V n) {
        this.n = n;
        
        V vX = n.getCoordInstance(); vX.set(1, 0, 0);
        V vY = n.getCoordInstance(); vY.set(0, 1, 0);
       
        V tmpZ = mZ = n.normalize();
        V tmpX = (Math.abs(tmpZ.get('x')) > 0.99f) ? n.newV(0,1,0) : n.newV(1,0,0);
        mY = tmpZ.cross(tmpX).normalize();
        mX = mY.cross(tmpZ);
        
        
        //branchlessONB(z);
    }
    
    @Override
    public V toWorld(V a) {
        return mX.mul(a.get('x')).add(mY.mul(a.get('y'))).add(mZ.mul(a.get('z')));
    }

    @Override
    public V toLocal(V a) {
        V v = n.newV(a.dot(mX), a.dot(mZ), a.dot(mZ));
        return v;
    }

    @Override
    public V binormal(){ return mX; }
    @Override
    public V tangent (){ return mY; }
    @Override
    public V normal  (){ return mZ; }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Frame").append("\n");
        builder.append(mX).append("\n");
        builder.append(mY).append("\n");
        builder.append(mZ).append("\n");
        return builder.toString();
    }
    
    // reflect vector through (0,0,1)
    public static VCoord reflect(VCoord v)
    {
        return v.newV(-v.get('x'), -v.get('y'), v.get('z'));
    }
    
    //v is incidence but points outwards
    //https://graphics.stanford.edu/courses/cs148-10-summer/docs/2006--degreve--reflection_refraction.pdf
    public static VCoord refract(VCoord v, VCoord n, float n1, float n2)
    {
        float nratio = n1/n2;
        float cosI  = v.dot(n); //note v points outwards
        float sinT2 = nratio * nratio * (1.f - cosI * cosI);
        if(sinT2 > 1.0) return null; //TIR (use fresnel equation appropriately to avoid return of null vector)
        float cosT = (float)Math.sqrt(1.f - sinT2);
        return v.neg().mul(nratio).add(n.mul(nratio*cosI - cosT));
    }
    
    public static float cosTheta(VCoord w)
    {
       return w.get('z');
    }

    public static float cos2Theta(VCoord w)
    {
       return w.get('z') * w.get('z');
    }

    public static float absCosTheta(VCoord w)
    {
       return Math.abs(w.get('z'));
    }

    public static float sin2Theta(VCoord w)
    {
       return Math.max(0.f, 1.f - cos2Theta(w));
    }

    public static float sinTheta(VCoord w)
    {
       return (float) Math.sqrt(sin2Theta(w));
    }

    public static float tanTheta(VCoord w)
    {
       return sinTheta(w)/cosTheta(w);
    }

    public static float tan2Theta(VCoord w)
    {
       return sin2Theta(w)/cos2Theta(w);
    }

    public static float cosPhi(VCoord w)
    {
       float sinTheta = sinTheta(w);
       return sinTheta == 0 ? 1 : Utility.clamp(w.get('x')/ sinTheta, -1.f, 1.f);
    }

    public static float sinPhi(VCoord w)
    {
       float sinTheta = sinTheta(w);
       return sinTheta == 0 ? 0 : Utility.clamp(w.get('y')/ sinTheta, -1.f, 1.f);
     }

    public static float cos2Phi(VCoord w)
    {
       return cosPhi(w) * cosPhi(w);
    }

    public static float sin2Phi(VCoord w)
    {
       return sinPhi(w) * sinPhi(w);
    }

    public static VCoord halfVector(VCoord wi, VCoord wo)
    {
       return wi.add(wo).normalize();       
    }

    public static VCoord sphericalToVector(VCoord v, float phi, float theta)
    {
        v.set(
                (float)sin(theta)*(float)cos(phi), 
                (float)sin(theta)*(float)sin(phi),
                (float)cos(theta));
       return v;
    }

    public static VCoord sphericalToVector2(VCoord v, float cosphi, float sinphi, float costheta, float sintheta)
    {
       v.set(sintheta*cosphi,  sintheta*sinphi, costheta);
       return v;
    }

    public static VCoord calculateWoFromH(VCoord wi, VCoord H)
    {
       VCoord wo = H.getCoordInstance();
       //wo.xyz = 2*(dot(wi.xyz, H.xyz) * H.xyz - wi.xyz);
       wo.set(2 * wi.dot(H) * (H.get('x') - wi.get('x')),
              2 * wi.dot(H) * (H.get('y') - wi.get('y')),
              2 * wi.dot(H) * (H.get('z') - wi.get('z')));
       
       return wo;
    }
}
