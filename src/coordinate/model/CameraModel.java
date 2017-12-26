/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.model;

import static java.lang.Math.abs;
import coordinate.generic.SCoord;
import coordinate.generic.VCoord;
import coordinate.generic.AbstractRay;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 * @param <R>
 */
public abstract class CameraModel <S extends SCoord, V extends VCoord, R extends AbstractRay<S, V>>
{
    public S position;
    public S lookat;
    public V up;    
    public float fov;    
    public Transform<S, V> cameraTransform;
    public float mImagePlaneDist;
    
    private V Du;
    private V Dv;
    private V vp;
    
    public CameraModel(S position, S lookat, V up, float horizontalFOV)
    {
        this.position = position;
        this.lookat = lookat;
        this.up = up;
        this.fov = horizontalFOV;
        this.cameraTransform = new Transform<>();
    }  
       
    public void setUp()
    {
        V d = (V) lookat.sub(position).normalize();
        V r = (V) d.cross(up).normalize();
        V u = (V) r.cross(d);

        Matrix e = Matrix.identity();
        e.set(0, 3, -position.get('x'));
        e.set(1, 3, -position.get('y'));
        e.set(2, 3, -position.get('z'));
                
        Matrix eInv = Matrix.identity();
        eInv.set(0, 3, position.get('x'));
        eInv.set(1, 3, position.get('y'));
        eInv.set(2, 3, position.get('z'));
        
        Matrix viewToWorld = Matrix.identity();
        viewToWorld.setRow(0, r.get('x'), u.get('x'), -d.get('x'), 0);
        viewToWorld.setRow(1, r.get('y'), u.get('y'), -d.get('y'), 0);
        viewToWorld.setRow(2, r.get('z'), u.get('z'), -d.get('z'), 0);
        
        Matrix worldToView = viewToWorld.transpose();     
        Matrix mV = worldToView.mul(e);        
        Matrix mV_Inv = eInv.mul(viewToWorld);
                                        
        cameraTransform.m = mV;
        cameraTransform.mInv = mV_Inv;                    
    }
    
    public R getFastRay(float x, float y, float xRes, float yRes, R ray)
    {     
        float fv = (float)Math.toRadians(this.fov);
        
        V look  = (V) lookat.sub(position);        
        Du    = (V) look.cross(up).normalize();
        Dv    = (V) look.cross(Du).normalize();
        
        float fl = xRes / (2.0F * (float)Math.tan(0.5F * fv));
        
        vp = (V) look.normalize();
        vp.set('x', (vp.get('x') * fl - 0.5F * (xRes * Du.get('x') + yRes * Dv.get('x'))));
        vp.set('y', (vp.get('y') * fl - 0.5F * (xRes * Du.get('y') + yRes * Dv.get('y'))));
        vp.set('z', (vp.get('z') * fl - 0.5F * (xRes * Du.get('z') + yRes * Dv.get('z'))));
        
        V dir = (V) vp.getCoordInstance();
        dir.set(x * Du.get('x') + y * Dv.get('x') + vp.get('x'), x * Du.get('y') + y * Dv.get('y') + vp.get('y'), x * Du.get('z') + y * Dv.get('z') + vp.get('z'));
        dir.normalizeAssign();                
        ray.set(position, dir);
        
        return ray;        
    }
    
    public V forward()
    {
        return (V) lookat.sub(position).normalize();
    }
    
    public S position()
    {
        return position;
    }
    
    public R generateRay(float x, float y, float xResolution, float yResolution, R ray)
    {
        float d = (float) (1./Math.tan(Math.toRadians(fov)/2));
        
        float a = xResolution/yResolution;
        float px = a * (2 * x/xResolution - 1);
        float py = -2 * y/yResolution + 1;
        float pz = -d;
        
        V rd = (V) up.getCoordInstance();
        rd.set(px, py, pz);
        rd.normalizeAssign();
        S ro = (S) lookat.getSCoordInstance();
     
        ray.set(ro, rd);
        
        float focalDistance = focalDistance();
        float ft = focalDistance/abs(ray.getDirection().get('z'));        
        S pFocus = ray.getPoint(ft);
        
        //SamplingDisk disk = new SamplingDisk(0.5f);
        //Point2f lensUV = disk.sampleDisk(Rng.getFloat(), Rng.getFloat());
        //Point2f lensUV = new Point2f();        
        //Point3f oo = new Point3f(lensUV.x, lensUV.y, 0);
        
        S oo = (S) lookat.getSCoordInstance();
        V dd = (V) pFocus.sub(oo);
        
        cameraTransform.inverse().transformAssign(oo);
        cameraTransform.inverse().transformAssign(dd);
        
        ray.set(oo, dd);
        return ray;
    }
    
    private float focalDistance()
    {
        return lookat.sub(position).length();
    }
    
     @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        
        builder.append("Camera: ").append("\n");
        builder.append("         eye    ").append(String.format("(%.5f, %.5f, %.5f)", position.get('x'), position.get('y'), position.get('z'))).append("\n");
        builder.append("         lookat ").append(String.format("(%.5f, %.5f, %.5f)", lookat.get('x'), lookat.get('y'), lookat.get('z'))).append("\n");
        
        return builder.toString(); 
    }

    public boolean checkRaster(float x, float y, float xResolution, float yResolution) 
    {
        return x >= 0 && y >= 0 &&
            x < xResolution && y < yResolution;
    }
        
    public abstract CameraModel<S, V, R> copy();
    
    public float[] worldToRaster(S aHitpoint, float xResolution, float yResolution)            
    {
        S cHitpoint = cameraTransform.transformScalar(aHitpoint);
        
        float d = (float) (1./Math.tan(Math.toRadians(fov)/2));
        float a = xResolution/yResolution;
        
        float xndc = d*cHitpoint.get('x')/(-a*cHitpoint.get('z'));
        float yndc = d*cHitpoint.get('y')/(-cHitpoint.get('z'));
        
        float xs = xResolution/2*xndc + xResolution/2;
        float ys = -yResolution/2*yndc + yResolution/2;
        
        return new float[]{xs, ys};        
    }
    
}
