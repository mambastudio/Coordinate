/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.obj;

import coordinate.generic.AbstractMesh;
import coordinate.list.CoordinateFloatList;
import coordinate.list.IntList;
import example.BoundingBox;
import example.Point2f;
import example.Point3f;
import example.Vector3f;

/**
 *
 * @author user
 */
public class SimpleMesh extends AbstractMesh<Point3f, Vector3f, Point2f>{
    private final BoundingBox bounds;
    public SimpleMesh()
    {
        points = new CoordinateFloatList(Point3f.class);
        normals = new CoordinateFloatList(Vector3f.class);
        texcoords = new CoordinateFloatList(Point2f.class);
        triangleFaces = new IntList();
        bounds = new BoundingBox();
    }

    @Override
    public void addPoint(Point3f p) {
        points.add(p);
        bounds.include(p);
    }

    @Override
    public void addPoint(float... values) {
        Point3f p = new Point3f(values[0], values[1], values[2]);
        addPoint(p);
        bounds.include(p);
    }

    @Override
    public void addNormal(Vector3f n) {
        normals.add(n);
    }

    @Override
    public void addNormal(float... values) {
        Vector3f n = new Vector3f(values[0], values[1], values[2]);
        normals.add(n);
    }

    @Override
    public void addTexCoord(Point2f t) {
        texcoords.add(t);
    }

    @Override
    public void addTexCoord(float... values) {
        Point2f t = new Point2f(values[0], values[1]);
        texcoords.add(t);   
    }
    
}
