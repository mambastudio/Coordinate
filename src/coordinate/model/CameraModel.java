/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.model;

import coordinate.transform.Matrix;
import coordinate.transform.Transform;
import static java.lang.Math.abs;
import coordinate.generic.SCoord;
import coordinate.generic.VCoord;
import coordinate.generic.AbstractRay;
import coordinate.utility.Value2Df;

/**
 *
 * Essential mathematics for games and interactive applications by James M. Van Verth & Lars M. Bishop
 * 
 * @author user
 * @param <S>
 * @param <V>
 * @param <R>
 */
public abstract class CameraModel <S extends SCoord, V extends VCoord, R extends AbstractRay<S, V, R>>
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

        //translate eye position from world to view
        Matrix e = Matrix.identity();
        e.set(0, 3, -position.get('x'));
        e.set(1, 3, -position.get('y'));
        e.set(2, 3, -position.get('z'));
                
        //translate eye position from view to world
        Matrix eInv = Matrix.identity();
        eInv.set(0, 3, position.get('x'));
        eInv.set(1, 3, position.get('y'));
        eInv.set(2, 3, position.get('z'));
        
        //computing view orientation, in this view to world
        Matrix viewToWorldOrientation = Matrix.identity();
        viewToWorldOrientation.setRow(0, r.get('x'), u.get('x'), -d.get('x'), 0);
        viewToWorldOrientation.setRow(1, r.get('y'), u.get('y'), -d.get('y'), 0);
        viewToWorldOrientation.setRow(2, r.get('z'), u.get('z'), -d.get('z'), 0);
        
        //inverse of view orientation, world to view
        Matrix worldToViewOrientation = viewToWorldOrientation.transpose();   
        
        //full transform from world to view is (W_to_V * E)
        Matrix mV = worldToViewOrientation.mul(e);  
        //full transform from view to world is (E_inv * V_to_W) 
        Matrix mV_Inv = eInv.mul(viewToWorldOrientation);
                     
        //camera transform
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
    
    /**
     * @param x     
     * @param y     
     * @param xResolution     
     * @param yResolution     
     * @param ray     
     * @return      
     * 
     * Normalised Device Coordinates (NDC) - unit square (-1,-1) <-> (1, 1)
     * NDC is similar to projection plane. How far (d) is it from point/origin
     * to NDC?
     *            1/d = tan(theta_fov/2)  
     *              d = cot(theta_fov/2)
     * 
     * Calculate aspect ratio of screen since screen width is not always equal
     * to height
     *              a = w_s/h_s
     * 
     * Calculate screen space to NDC but height goes down for screen since origin
     * is upper left corner as usual. i.e. 
     * (0, 0) <-> (w_s, h_s) to (-1,-1) <-> (1, 1)
     * 
     *          x_ndc = 2*x_s/w_s - 1
     *          y_ndc = -2*y_s/h_s + 1
     * 
     * NDC to view space is almost similar but need to factor in the aspect ratio
     *            p_x = a * x_ndc
     *            p_y = y_ndc
     *            p_z = -d
     * Therefore,
     *              p = (p_x, p_y, p_z)
     *  
     * Direction of ray is p - o but o is (0, 0, 0), therefore
     *              d = p.normalize()
     *              o = (0, 0, 0)
     * 
     *            ray = (o, d)
     * 
     * Transform ray from view to world
     *     ray_world  = cameraTransform.inverse().transform(ray) 
    */
    public R generateRay(float x, float y, float xResolution, float yResolution, R ray)
    {
        
        float d_ndc = (float) (1./Math.tan(Math.toRadians(fov)/2));
        
        //aspect ratio of screen is a * w_s/h_s
        float a = xResolution/yResolution;
        
        
        float px = a * (2 * x/xResolution - 1);
        float py = -2 * y/yResolution + 1;
        float pz = -d_ndc;
        
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
    
    public R generateRayJitter(float x, float y, float xResolution, float yResolution, R ray)
    {
        float d = (float) (1./Math.tan(Math.toRadians(fov)/2));
        
        //generate random number (0 to 1)
        Value2Df sample = Value2Df.getRandom();
        float jitter1   = 1.f/xResolution * (2 * sample.x - 1.f);
        float jitter2   = 1.f/yResolution * (2 * sample.y - 1.f);
        
        float a = xResolution/yResolution;
        float px = a * (2 * x/xResolution - 1 + jitter1);
        float py = -2 * y/yResolution + 1 + jitter2;
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
                
        //view to world
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
        builder.append("         up     ").append(String.format("(%.5f, %.5f, %.5f)", up.get('x'), up.get('y'), up.get('z'))).append("\n");
        builder.append("         fov    ").append(String.format("(%.5f)", fov));
        
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
    
    protected Value2Df screenSpaceToNDC(Value2Df screenCoord, Value2Df screenResolution)
    {
        float x_ndc = (2 * screenCoord.x/screenResolution.x - 1);
        float y_ndc = -2 * screenCoord.y/screenResolution.y + 1;
        return new Value2Df(x_ndc, y_ndc);
    }
    
    protected Value2Df ndcToScreenSpace(Value2Df ndcCoord, Value2Df screenResolution)
    {
        float xs = screenResolution.x/2*ndcCoord.x + screenResolution.x/2;
        float ys = -screenResolution.y/2*ndcCoord.y + screenResolution.y/2;
        return new Value2Df(xs, ys);
    }
    
    protected Value2Df ndcToViewSpace(Value2Df ndcCoord, Value2Df screenResolution)
    {
        return new Value2Df(
                ndcCoord.x * getAspectRatio(screenResolution),
                ndcCoord.y
        );        
    }
    
    protected Value2Df viewSpaceToNDC(S position, Value2Df screenResolution)
    {
        float a = getAspectRatio(screenResolution);
        float d = distanceToProjectionPlane();
        
        float xndc = d*position.get('x')/(-a*position.get('z'));
        float yndc = d*position.get('y')/(-position.get('z'));
        
        return new Value2Df(xndc, yndc);
    }
    
    //aspect ratio of screen is a * w_s/h_s
    protected float getAspectRatio(Value2Df screenResolution)
    {        
        return screenResolution.x/screenResolution.y;
    }
    
    //view position to projection plane or NDC plane
    protected float distanceToProjectionPlane()
    {
        return (float) (1./Math.tan(Math.toRadians(fov)/2));
    }
    
    protected boolean isNDCValid(Value2Df ndcCoord)
    {
        return ndcCoord.x >=-1 && ndcCoord.x <=1 && ndcCoord.y >=-1 && ndcCoord.y <=1;
    }
}
