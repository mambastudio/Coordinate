/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import coordinate.generic.AbstractBound;

/**
 *
 * @author user
 */
public class BoundingBox implements AbstractBound<Point3f, Vector3f, Ray, BoundingBox>
{

    @Override
    public void include(Point3f s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point3f getCenter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getCenter(int dim) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point3f getMinimum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point3f getMaximum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void include(BoundingBox b) {
        AbstractBound.super.include(b); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int maximumExtent() {
        return AbstractBound.super.maximumExtent(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BoundingBox getInstance() {
        return new BoundingBox();
    }
    
}
