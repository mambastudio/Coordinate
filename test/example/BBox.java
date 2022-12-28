/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import coordinate.generic.AbstractBound;

/**
 *
 * @author user
 */
public class BBox implements AbstractBound<Point3f, Vector3f, Ray, BBox>
{

    @Override
    public void include(Point3f s) {
        
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
    public void include(BBox b) {
        AbstractBound.super.include(b); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int maximumExtentAxis() {
        return AbstractBound.super.maximumExtentAxis(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BBox getInstance() {
        return new BBox();
    }
    
}
