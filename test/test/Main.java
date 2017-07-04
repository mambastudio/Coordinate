/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import coordinate.Point3f;
import coordinate.Vector3f;
import coordinate.model.Camera;
import coordinate.model.Transform;

/**
 *
 * @author user
 */
public class Main {
    public static void main(String... args)
    {
        Transform<Point3f, Vector3f> transform = new Transform<>();
        transform.transformAssign(new Point3f());
        Camera<Point3f, Vector3f, Ray> camera = new Camera<>(new Point3f(0, 0, 4), new Point3f(), new Vector3f(0, 1, 0), 45);
        System.out.println(camera.getFastRay(50, 50, 100, 100, new Ray()));
    }
}
