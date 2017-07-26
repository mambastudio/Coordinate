/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.model;

import coordinate.generic.VCoord;
import static java.lang.Math.copySign;

/**
 *
 * @author user
 * @param <V>
 */
public class Frame<V extends VCoord> {
    public V mX, mY, mZ;
    
    public Frame(V z)
    {
        setFromZ(z);
    }
    
    public final void setFromZ(V z)
    {
        float sign = copySign(1.0f, z.get('z'));
        float a = -1f / (sign + z.get('z'));
        float b = z.get('x')*z.get('y')*a;
        
        if(mX == null) mX = (V) z.getCoordInstance();
        if(mY == null) mY = (V) z.getCoordInstance();
        
        mX.set(1.0f + sign * z.get('x') * z.get('x') * a,  sign * b, -sign * z.get('x'));
        mY.set(b, sign + z.get('y') * z.get('y') * a, -z.get('y'));
    }
    
    public V toWorld(V a)
    {                
        return (V) mX.mul(a.get('x')).add(mY.mul(a.get('y'))).add(mZ.mul(a.get('z')));
    }
    
    public V toLocal(V a)
    {
        V v = (V) a.getCoordInstance();
        v.set('x', a.dot(mX));
        v.set('y', a.dot(mY));
        v.set('z', a.dot(mZ));        
        return v;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("xdir ").append(mX).append("\n");
        builder.append("ydir ").append(mY).append("\n");
        builder.append("zdir ").append(mZ);
        return builder.toString();
    }
            
}
