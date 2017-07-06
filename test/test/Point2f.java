package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import coordinate.generic.AbstractCoordinate;

/**
 *
 * @author user
 */
public class Point2f extends AbstractCoordinate {
    public float x, y;
    
    public Point2f(){}
    public Point2f(float x, float y){this.x = x; this.y = y;}
        
    public Point2f add(Point2f a) {return new Point2f(x + a.x, y + a.y);}
    
    @Override
    public final String toString() 
    {
        
        return String.format("(%.2f, %.2f)", x, y);
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public float[] getArray() {
        return new float[]{x, y};
    }

    @Override
    public void set(float... values) {
        x = values[0];
        y = values[1];
    }

    @Override
    public float get(char axis) {
        switch (axis) {
            case 'x':
                return x;
            case 'y':
                return y;
            default:
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.  
        }
    }

    @Override
    public void set(char axis, float value) {
        switch (axis) {
            case 'x':
                x = value;
                break;
            case 'y':
                y = value;
                break;
            default:
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
