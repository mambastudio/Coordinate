/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

import coordinate.generic.VCoord;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author user
 */
public class Geometry {
    public static float CosTheta(VCoord w)
    {
       return w.get('z');
    }

    public static float Cos2Theta(VCoord w)
    {
       return w.get('z') * w.get('z');
    }

    public static float AbsCosTheta(VCoord w)
    {
       return Math.abs(w.get('z'));
    }

    public static float Sin2Theta(VCoord w)
    {
       return Math.max(0.f, 1.f - Cos2Theta(w));
    }

    public static float SinTheta(VCoord w)
    {
       return (float) Math.sqrt(Sin2Theta(w));
    }

    public static float TanTheta(VCoord w)
    {
       return SinTheta(w)/CosTheta(w);
    }

    public static float Tan2Theta(VCoord w)
    {
       return Sin2Theta(w)/Cos2Theta(w);
    }

    public static float CosPhi(VCoord w)
    {
       float sinTheta = SinTheta(w);
       return sinTheta == 0 ? 1 : Utility.clamp(w.get('x')/ sinTheta, -1.f, 1.f);
    }

    public static float SinPhi(VCoord w)
    {
       float sinTheta = SinTheta(w);
       return sinTheta == 0 ? 0 : Utility.clamp(w.get('y')/ sinTheta, -1.f, 1.f);
     }

    public static float Cos2Phi(VCoord w)
    {
       return CosPhi(w) * CosPhi(w);
    }

    public static float Sin2Phi(VCoord w)
    {
       return SinPhi(w) * SinPhi(w);
    }

    public static VCoord CalculateH(VCoord wi, VCoord wo)
    {
       return wi.add(wo).normalize();       
    }

    public static VCoord SphericalToVector(VCoord v, float phi, float theta)
    {
        v.set(
                (float)sin(theta)*(float)cos(phi), 
                (float)sin(theta)*(float)sin(phi),
                (float)cos(theta));
       return v;
    }

    public static VCoord SphericalToVector2(VCoord v, float cosphi, float sinphi, float costheta, float sintheta)
    {
       v.set(sintheta*cosphi,  sintheta*sinphi, costheta);
       return v;
    }

    public static VCoord CalculateWoFromH(VCoord H, VCoord wi)
    {
       VCoord wo = H.getCoordInstance();
       //wo.xyz = 2*(dot(wi.xyz, H.xyz) * H.xyz - wi.xyz);
       wo.set(2 * wi.dot(H) * (H.get('x') - wi.get('x')),
              2 * wi.dot(H) * (H.get('y') - wi.get('y')),
              2 * wi.dot(H) * (H.get('z') - wi.get('z')));
       
       return wo;
    }

}
