/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 */
public interface SCoord<S extends SCoord, V extends VCoord> extends AbstractCoordinateFloat {
    
    public abstract S getSCoordInstance();  
    public abstract V getVCoordInstance();
    public abstract S copy();
    
    public default S newS(float x, float y, float z)
    {
        S dest = (S) copy();
        dest.set(x, y, z);
        return dest;
    }
    
    public default V add(S b)
    {
        V v = getVCoordInstance();        
        v.set('x', get('x') + b.get('x'));
        v.set('y', get('y') + b.get('y'));
        v.set('z', get('z') + b.get('z'));
        return v;
    }
    
    public default S add(V v) 
    {
        S s = getSCoordInstance();
        s.set('x', get('x') + v.get('x'));
        s.set('y', get('y') + v.get('y'));
        s.set('z', get('z') + v.get('z'));
        return s;
    }   
        
    public default S addS(S b)
    {
        S s = getSCoordInstance();        
        s.set('x', get('x') + b.get('x'));
        s.set('y', get('y') + b.get('y'));
        s.set('z', get('z') + b.get('z'));
        return s;
    }
            
    public default V sub(S b)
    {
        V v = getVCoordInstance();        
        v.set('x', get('x') - b.get('x'));
        v.set('y', get('y') - b.get('y'));
        v.set('z', get('z') - b.get('z'));
        return v;
    }
    
    public default S subS(S b)
    {
        S s = getSCoordInstance();        
        s.set('x', get('x') - b.get('x'));
        s.set('y', get('y') - b.get('y'));
        s.set('z', get('z') - b.get('z'));
        return s;
    }
    
    public default S sub(V b)
    {
        S s = copy();        
        s.set('x', get('x') - b.get('x'));
        s.set('y', get('y') - b.get('y'));
        s.set('z', get('z') - b.get('z'));
        return s;
    }
    
    

    
    
    public default float distanceTo(S p) {
        float dx = get('x') - p.get('x');
        float dy = get('y') - p.get('y');
        float dz = get('z') - p.get('z');
        return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public default float distanceTo(float px, float py, float pz) {
        float dx = get('x') - px;
        float dy = get('y') - py;
        float dz = get('z') - pz;
        return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public default float distanceToSquared(S p) {
        float dx = get('x') - p.get('x');
        float dy = get('y') - p.get('y');
        float dz = get('z') - p.get('z');
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public default float distanceToSquared(float px, float py, float pz) {
        float dx = get('x') - px;
        float dy = get('y') - py;
        float dz = get('z') - pz;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }
    
    public default S middle(S p)
    {
        S s = getSCoordInstance();
        s.set('x', (get('x') + p.get('x')) * 0.5f);
        s.set('y', (get('y') + p.get('y')) * 0.5f);
        s.set('z', (get('z') + p.get('z')) * 0.5f);
        return s;
    }
    
    public default S setValue(float... values) {
        if(values == null)
             return null;                
        this.set(values);       
        return (S) this;
    }
    
    
    public default S newCoordAll(float value)
    {
        S s = this.getSCoordInstance();
        float[] values = s.getArray();
        for(int i = 0; i<values.length; i++)
            values[i] = value;
        set(values);
        return s;
    }
    
    
    public default S setValue(S p) {
        this.set('x', p.get('x'));
        this.set('y', p.get('y'));
        this.set('z', p.get('z'));
        return (S) this;
    }
    
    public default S mul(float a) 
    {
        S s = getSCoordInstance();
        s.set('x', get('x') * a);
        s.set('y', get('y') * a);
        s.set('z', get('z') * a);
        return s;
    }  
    
    public default void mulAssign(float a) 
    {        
        set('x', get('x') * a);
        set('y', get('y') * a);
        set('z', get('z') * a);
    }
    
    public default S mul(S snew) 
    {
        S s = getSCoordInstance();
        s.set('x', get('x') * snew.get('x'));
        s.set('y', get('y') * snew.get('y'));
        s.set('z', get('z') * snew.get('z'));
        return s;
    }  
        
    public default S neg()        
    {
        S s = this.copy();
        s.set('x', s.get('x')* -1);
        s.set('y', s.get('y')* -1);
        s.set('z', s.get('z')* -1);
        return s;        
    }
    
    public default S sqrt()
    {
        S s = this.copy();
        float[] array = this.getArray();
        for(int i = 0; i<array.length; i++)
        {
            array[i] = (float) Math.sqrt(array[i]);
        }
        s.setValue(array);
        return s;
    }
    
    default S abs()
    {
        S s = copy();
        s.set('x', Math.abs(this.get('x')));
        s.set('y', Math.abs(this.get('y')));
        s.set('z', Math.abs(this.get('z')));
        return s;
    }
    
    public static SCoord min3(SCoord s0, SCoord s1)
    {
        SCoord v = s0.newS(0, 0, 0);
        v.set(min(s0.get(0), s0.get(0)), min(s0.get(1), s0.get(1)), min(s0.get(2), s0.get(2)));        
        return v;
    }
    
    public static SCoord min4(SCoord s0, SCoord s1)
    {
        SCoord v = s0.newS(0, 0, 0);
        v.set(min(s0.get(0), s0.get(0)), min(s0.get(1), s0.get(1)), min(s0.get(2), s0.get(2)), min(s0.get(3), s0.get(3)));        
        return v;
    }
    
    public static SCoord max3(SCoord s0, SCoord s1)
    {
        SCoord v = s0.newS(0, 0, 0);
        v.set(max(s0.get(0), s0.get(0)), max(s0.get(1), s0.get(1)), max(s0.get(2), s0.get(2)));        
        return v;
    }
    
    public static SCoord max4(SCoord s0, SCoord s1)
    {
        SCoord v = s0.newS(0, 0, 0);
        v.set(max(s0.get(0), s0.get(0)), max(s0.get(1), s0.get(1)), max(s0.get(2), s0.get(2)), max(s0.get(3), s0.get(3)));        
        return v;
    }
}
