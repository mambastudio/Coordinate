/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 */
public abstract class SCoord<S extends SCoord, V extends VCoord> extends AbstractCoordinate {
    
    public abstract S getSCoordInstance();  
    public abstract V getVCoordInstance();
    public abstract S copy();
            
    public V sub(S b)
    {
        V v = getVCoordInstance();        
        v.set('x', get('x') - b.get('x'));
        v.set('y', get('y') - b.get('y'));
        v.set('z', get('z') - b.get('z'));
        return v;
    }
    
    public S add(V v) 
    {
        S s = getSCoordInstance();
        s.set('x', get('x') + v.get('x'));
        s.set('y', get('y') + v.get('y'));
        s.set('z', get('z') + v.get('z'));
        return s;
    }       
    
    public final float distanceTo(S p) {
        float dx = get('x') - p.get('x');
        float dy = get('y') - p.get('y');
        float dz = get('z') - p.get('z');
        return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public final float distanceTo(float px, float py, float pz) {
        float dx = get('x') - px;
        float dy = get('y') - py;
        float dz = get('z') - pz;
        return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public final float distanceToSquared(S p) {
        float dx = get('x') - p.get('x');
        float dy = get('y') - p.get('y');
        float dz = get('z') - p.get('z');
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public final float distanceToSquared(float px, float py, float pz) {
        float dx = get('x') - px;
        float dy = get('y') - py;
        float dz = get('z') - pz;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }
    
    public final S middle(S p)
    {
        S s = getSCoordInstance();
        s.set('x', (get('x') + p.get('x')) * 0.5f);
        s.set('y', (get('y') + p.get('y')) * 0.5f);
        s.set('z', (get('z') + p.get('z')) * 0.5f);
        return s;
    }
    
    public final S set(float x, float y, float z) {
        this.set('x', x);
        this.set('y', y);
        this.set('z', z);
        return (S) this;
    }

    public final S set(S p) {
        this.set('x', p.get('x'));
        this.set('y', p.get('y'));
        this.set('z', p.get('z'));
        return (S) this;
    }
    
    public S mul(float a) 
    {
        S s = getSCoordInstance();
        s.set('x', get('x') * a);
        s.set('y', get('y') * a);
        s.set('z', get('z') * a);
        return s;
    }  
        
    public S neg()        
    {
        S s = this.copy();
        s.set('x', s.get('x')* -1);
        s.set('y', s.get('y')* -1);
        s.set('z', s.get('z')* -1);
        return s;        
    }
}
