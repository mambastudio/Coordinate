/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.model;

import coordinate.transform.Transform;
import coordinate.generic.AbstractBound;
import coordinate.generic.AbstractRay;
import coordinate.generic.SCoord;
import coordinate.generic.VCoord;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 * @param <R>
 * @param <B>
 */
public class OrientationModel<S extends SCoord, V extends VCoord, R extends AbstractRay<S, V>, B extends AbstractBound<S, V, R, B>> 
{    
    Class<S> classS = null;
    Class<V> classV = null;
    
    private static float maxExt;
    
    public OrientationModel(Class<S> classS, Class<V> classV)
    {
        this.classS = classS;
        this.classV = classV;
    }
    
    public void repositionLocation(CameraModel<S, V, R> camera, B bound)
    {
        S center = bound.getCenter();
        float distance = bound.getMinimum().distanceTo(bound.getMaximum()); //maximum distance from one side of bound to the other
        V direction = (V) center.sub(camera.position()).normalize();        //direction
        camera.position = (S) center.add(direction.neg().mul(distance*1.5f));  //r - td
        camera.lookat = center;
        
    }
    
    public void reposition(CameraModel<S, V, R> camera, B bound)
    {
        S center = bound.getCenter();
        float distance = bound.getMinimum().distanceTo(bound.getMaximum());
        
        camera.position.set('x', center.get('x'));
        camera.position.set('y', center.get('y'));
        camera.position.set('z', center.get('z') - 1);
        
        camera.lookat.set('x', center.get('x'));
        camera.lookat.set('y', center.get('y'));
        camera.lookat.set('z', center.get('z'));
        
        camera.up.set('x', 0);
        camera.up.set('y', 1);
        camera.up.set('z', 0);
        
        distance(camera, distance);
    }
    
    public void rotateY(CameraModel<S, V, R> camera, float angle)
    {
        Transform toOrigin = Transform.translate(camera.lookat.neg());
        
        //Transform camera to origin for proper tranform (i.e. rotation, translation)       
        toOrigin.transformAssign(camera.up);
        toOrigin.transformAssign(camera.position);
        toOrigin.transformAssign(camera.lookat);
              
        V look = (V) camera.lookat.sub(camera.position);       
        V Du   = (V) look.cross(camera.up).normalize();   //get x-axis     
         
        //do proper transforms, and no point of transforming lookat since it's the origin in this context
        Transform transform = Transform.rotate(angle, Du);
        transform.transformAssign(camera.up);
        transform.transformAssign(camera.position);
                         
        //Untransform camera
        toOrigin.inverse().transformAssign(camera.lookat);
        toOrigin.inverse().transformAssign(camera.up);
        toOrigin.inverse().transformAssign(camera.position);
        
    }
    
    public void rotateX(CameraModel<S, V, R> camera, float angle)
    {
        Transform toOrigin = Transform.translate(camera.lookat.neg()); 
                
        //Transform camera to origin for proper tranform (i.e. rotation, translation)               
        toOrigin.transformAssign(camera.up);
        toOrigin.transformAssign(camera.position);
        toOrigin.transformAssign(camera.lookat);
        
       //do proper transforms, and no point of transforming lookat since it's the origin in this context
        Transform transform = Transform.rotate(angle, camera.up);  
        transform.transformAssign(camera.up);
        transform.transformAssign(camera.position);
        
        //Untransform camera        
        toOrigin.inverse().transformAssign(camera.lookat);
        toOrigin.inverse().transformAssign(camera.up);
        toOrigin.inverse().transformAssign(camera.position);
    }
    
    public  void distance(CameraModel<S, V, R> camera, float distance)
    {
        Transform toOrigin = Transform.translate(camera.lookat.neg());        
        
        //Transform camera to origin for proper tranform (i.e. rotation, translation)
        toOrigin.transformAssign(camera.up);
        toOrigin.transformAssign(camera.position);
        
        //TODO : transform camera
        V sphCoord = sphericalCoordinates(camera.position.get('x'), camera.position.get('y'), camera.position.get('z'));

        float p = sphCoord.get('x');
        float phi = sphCoord.get('y');
        float theta = sphCoord.get('z');

        float t = p + distance;
       
        if(t < 0.0f)
        {
            t = 0.01f;
        }
        p = t;      
        
        V cartCoord = cartesianCoordinates(getVInstance(p, phi, theta));

        camera.position.set('x', cartCoord.get('x'));
        camera.position.set('y', cartCoord.get('y'));
        camera.position.set('z', cartCoord.get('z'));
        
        //Untransform camera
        toOrigin.inverse().transformAssign(camera.up);
        toOrigin.inverse().transformAssign(camera.position);    
    }
    
    public void translateDistance(CameraModel<S, V, R> camera, float distance)
    {              
        distance(camera, distance);
    }
    
    public V sphericalCoordinates(float x, float y, float z)
    {
        V v = getVInstance(x, y, z);
        return sphericalCoordinates(v);
    }
    
    public V sphericalCoordinates(V coordinates)
    {
        float p, phi, theta;

        p = (float) Math.sqrt((coordinates.get('x') * coordinates.get('x')) + 
                              (coordinates.get('y') * coordinates.get('y')) + 
                              (coordinates.get('z') * coordinates.get('z')));
        
        V coord = getVInstance();
        coord.set(coordinates.get('x'), coordinates.get('y'), coordinates.get('z'));
        coord.normalizeAssign();
        
        phi = (float)acos(coord.dot(getVInstance(0, 1, 0)));
        theta = (float)atan2(coord.get('z'), coord.get('x'));

        return getVInstance(p, phi, theta);
    }
    
    public V cartesianCoordinates(V coordinates)
    {
        float x, y, z;
        float p = coordinates.get('x');
        float phi = coordinates.get('y');
        float theta = coordinates.get('z');

        x = (float) (p * sin(phi) * cos(theta));
        y = (float) (p * cos(phi));
        z = (float) (p * sin(phi) * sin(theta));

        V newCoordinates = getVInstance();
        newCoordinates.set(x, y, z);
        return newCoordinates;
    }
       
    private S getSInstance()
    {
        try {
            return classS.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(OrientationModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private V getVInstance(float x, float y, float z)
    {
        V v = getVInstance();
        v.set(x, y, z);
        return v;
    }
    
    private V getVInstance()
    {
        try {
            return classV.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(OrientationModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
