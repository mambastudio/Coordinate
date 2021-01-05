/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.model;

import coordinate.generic.SCoord;
import coordinate.generic.VCoord;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import coordinate.generic.AbstractCoordinateFloat;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 */
public class Transform <S extends SCoord, V extends VCoord>{
    public Matrix m;
    public Matrix mInv;
    
     public Transform()
    {
        m = new Matrix();
        mInv = new Matrix();
    }
            
    public Transform(Matrix mat)
    {
        m = mat.clone();
        mInv = m.inverse();
    }
    
    public Transform(Matrix mat, Matrix matInv) 
    {
        m = mat.clone();
        mInv = matInv.clone();
    }
    
    
    public static Transform translate(AbstractCoordinateFloat v)
    {
        return translate(v.get('x'), v.get('y'), v.get('z'));
    }
    
    public static Transform translate(float x, float y, float z) 
    {
        Matrix sM = new Matrix();
        sM.setRow(0, 1, 0, 0, x);
        sM.setRow(1, 0, 1, 0, y);
        sM.setRow(2, 0, 0, 1, z);
        sM.setRow(3, 0, 0, 0, 1);
        
        Matrix sInvM = new Matrix();
        sInvM.setRow(0, 1, 0, 0, -x);
        sInvM.setRow(1, 0, 1, 0, -y);
        sInvM.setRow(2, 0, 0, 1, -z);
        sInvM.setRow(3, 0, 0, 0,  1);
        
        return new Transform(sM, sInvM);
    }
    
    public static Transform rotate(float angle, VCoord axis)
    {
        VCoord a = axis.normalize();
        float s = (float) sin(toRadians(angle));
        float c = (float) cos(toRadians(angle));
        
        Matrix rM = new Matrix();
        
        rM.set(0, 0, a.get('x') * a.get('x') + (1.f - a.get('x') * a.get('x')) * c);
        rM.set(0, 1, a.get('x') * a.get('y') * (1.f - c) - a.get('z') * s);
        rM.set(0, 2, a.get('x') * a.get('z') * (1.f - c) + a.get('y') * s);
        rM.set(0, 3, 0);
        
        rM.set(1, 0, a.get('x') * a.get('y') * (1.f - c) + a.get('z') * s);
        rM.set(1, 1, a.get('y') * a.get('y') + (1.f - a.get('y') * a.get('y')) * c);
        rM.set(1, 2, a.get('y') * a.get('z') * (1.f - c) - a.get('x') * s);
        rM.set(1, 3, 0);
        
        rM.set(2, 0, a.get('x') * a.get('z') * (1.f - c) - a.get('y') * s);
        rM.set(2, 1, a.get('y') * a.get('z') * (1.f - c) + a.get('x') * s);
        rM.set(2, 2, a.get('z') * a.get('z') + (1.f - a.get('z') * a.get('z')) * c);
        rM.set(2, 3, 0);
       
        rM.set(3, 0, 0);
        rM.set(3, 1, 0);
        rM.set(3, 2, 0);
        rM.set(3, 3, 1);
        
        return new Transform(rM, rM.transpose());
    }
    
    public S transformScalar(S sCoord)
    {
        float x = sCoord.get('x'), y = sCoord.get('y'), z = sCoord.get('z');
        float xp = m.get(0,0) * x + m.get(0,1) * y + m.get(0,2) * z + m.get(0,3);
        float yp = m.get(1,0) * x + m.get(1,1) * y + m.get(1,2) * z + m.get(1,3);
        float zp = m.get(2,0) * x + m.get(2,1) * y + m.get(2,2) * z + m.get(2,3);
        float wp = m.get(3,0) * x + m.get(3,1) * y + m.get(3,2) * z + m.get(3,3);
        assert (wp != 0);
        if (wp == 1.)
        {            
            S s = (S) sCoord.getSCoordInstance();            
            s.setValue(xp, yp, zp);
            return s;
        } 
        else 
        {         
            S s = (S) sCoord.getSCoordInstance();  
            s.setValue(xp * wp, yp * wp, zp * wp);
            return s;
        }
    }
      
    public V transformVector(V aVec)
    {         
        V v = (V) aVec.getCoordInstance();
        float x = aVec.get('x'), y = aVec.get('y'), z = aVec.get('z');
        v.set('x', m.get(0,0) * x + m.get(0,1) * y + m.get(0,2) * z);
        v.set('y', m.get(1,0) * x + m.get(1,1) * y + m.get(1,2) * z);
        v.set('z', m.get(2,0) * x + m.get(2,1) * y + m.get(2,2) * z);   
        return v;
    }  
    
    public V transformNormal(V aVec)
    {
        float x = aVec.get('x'), y = aVec.get('y'), z = aVec.get('z');
        V vn = (V) aVec.getCoordInstance();
        vn.set(mInv.get(0,0) * x + mInv.get(1,0) * y + mInv.get(2,0) * z,
               mInv.get(0,1) * x + mInv.get(1,1) * y + mInv.get(2,1) * z,
               mInv.get(0,2) * x + mInv.get(1,2) * y + mInv.get(2,2) * z);
        return vn;
    }
       
    public void transformAssign(V aVec)
    {          
        float x = aVec.get('x'), y = aVec.get('y'), z = aVec.get('z');        
        aVec.set('x', m.get(0,0) * x + m.get(0,1) * y + m.get(0,2) * z);
        aVec.set('y', m.get(1,0) * x + m.get(1,1) * y + m.get(1,2) * z);
        aVec.set('z', m.get(2,0) * x + m.get(2,1) * y + m.get(2,2) * z);       
    }    
    
    public void transformAssign(S aVec)
    {
        float x = aVec.get('x'), y = aVec.get('y'), z = aVec.get('z');
        float xp = m.get(0,0) * x + m.get(0,1) * y + m.get(0,2) * z + m.get(0,3);
        float yp = m.get(1,0) * x + m.get(1,1) * y + m.get(1,2) * z + m.get(1,3);
        float zp = m.get(2,0) * x + m.get(2,1) * y + m.get(2,2) * z + m.get(2,3);
        float wp = m.get(3,0) * x + m.get(3,1) * y + m.get(3,2) * z + m.get(3,3);
        assert (wp != 0);
        if (wp == 1.)
        {
            aVec.set('x', xp);
            aVec.set('y', yp);
            aVec.set('z', zp);
        } 
        else 
        {
            wp = 1 / wp;
            aVec.set('x', xp * wp);
            aVec.set('y', yp * wp);
            aVec.set('z', zp * wp);
        }
    }
    
    public Transform inverse()
    {
        return new Transform(mInv, m);
    }
    
}
