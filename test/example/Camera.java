/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import coordinate.model.CameraModel;

/**
 *
 * @author user
 */
public class Camera extends CameraModel <Point3f, Vector3f, Ray> {

    public Camera(Point3f position, Point3f lookat, Vector3f up, float horizontalFOV) {
        super(position, lookat, up, horizontalFOV);
    }

    @Override
    public Camera copy() {
        return new Camera(position.copy(), lookat.copy(), up.copy(), fov);
    }
    
}
