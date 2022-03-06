/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

/**
 *
 * @author user
 */
public class Value4Di {
    public int x, y, z, w;
    
    public Value4Di(){}
    public Value4Di(int x, int y, int z, int w){this.x = x; this.y = y; this.z = z; this.w = w;}
    
    public Value4Df getScale(Value4Di v)
    {
        return new Value4Df(v.x/(float)x, v.y/(float)y, v.z/(float)z, v.w/(float)w);
    }
    
    public Value4Di scale(Value4Df s)
    {
        return new Value4Di((int)(s.x*x), (int)(s.y*y), (int)(s.z*z), (int)(s.w*w));
    }
    
    @Override
    public String toString()
    {
        return String.format("(%3d, %3d, %3d, %3d)", x, y, z, w);
    }
}
