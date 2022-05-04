/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.parser.attribute;

import coordinate.generic.AbstractCoordinateFloat;
import javafx.scene.paint.Color;

/**
 *
 * @author user
 */
public class Color4T implements AbstractCoordinateFloat 
{
    public float r, g, b, w;
    
    public Color4T()
    {
        r = g = b = 0;
        w = 1;
    }
    
    public Color4T(float r, float g, float b)
    {
        this.r = r; this.g = g; this.b = b;
        this.w = 1;
    }
    
    public Color4T(float r, float g, float b, float w)
    {
        this(r, g, b);
        this.w = w;
    }
    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public float[] getArray() {
        return new float[]{r, g, b, w};
    }

    @Override
    public float get(char axis) {
        switch (axis) {
            case 'r':
                return r;
            case 'g':
                return g;
            case 'b':
                return b;
            case 'w':
                return w;
            default:
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.  
        }
    }

    @Override
    public void set(char axis, float value) {
        switch (axis) {
            case 'r':
                r = value;
                break;
            case 'g':
                g = value;
                break;
            case 'b':
                b = value;
                break;
            case 'w':
                w = value;
                break;
            default:
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public void set(float... values) {
        r = values[0];
        g = values[1];
        b = values[2];
        w = values[3];
    }

    @Override
    public void setIndex(int index, float value) {
        switch (index)
        {
            case 0:
                r = value;
                break;
            case 1:
                g = value;
                break;    
            case 2:
                b = value;
                break;
            case 3:
                w = value;
                break;    
        }
    }
    
    public Color4T copy()
    {
        return new Color4T(r, g, b, w);
    }

    @Override
    public int getByteSize() {
        return 4;
    }
    
    @Override
    public String toString()
    {
        float[] array = getArray();
        return String.format("(%3.2f, %3.2f, %3.2f)", array[0], array[1], array[2]);
    }
    
    public Color getColorFX()
    {
        return new Color(r, g, b, w);
    }
}
