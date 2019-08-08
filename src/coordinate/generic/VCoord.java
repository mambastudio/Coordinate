/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;

/**
 *
 * @author user
 * @param <V>
 */
public abstract class VCoord<V extends VCoord> extends AbstractCoordinate{
    public abstract V getCoordInstance();
    public abstract V copy();
    
    public float dot(V b) 
    {
        return get('x') *b.get('x') + get('y')*b.get('y') + get('z')*b.get('z');
    }
    
    public V cross(V t)
    {
        V dest = (V) t.copy();
        dest.set('x', get('y') * t.get('z') - get('z') * t.get('y'));
        dest.set('y', get('z') * t.get('x') - get('x') * t.get('z'));
        dest.set('z', get('x') * t.get('y') - get('y') * t.get('x'));
        return dest;
    }
    
    public final V addAssign(V v) {
        set('x', get('x')+v.get('x'));
        set('y', get('y')+v.get('y'));
        set('z', get('z')+v.get('z'));
        return (V) this;
    }
    
    public final V addAssign(float f) 
    {
        set('x', get('x')+f);
        set('y', get('y')+f);
        set('z', get('z')+f);
        return  (V) this;
    }
    
    public V add(float a)
    {
        V v = getCoordInstance();
        v.set('x', get('x') + a);
        v.set('y', get('y') + a);
        v.set('z', get('z') + a);        
        return v;
    }
    
    public V add(V a) 
    {
        V dest = copy();
        dest.set('x', get('x') + a.get('x'));
        dest.set('y', get('y') + a.get('y'));
        dest.set('z', get('z') + a.get('z'));
        return dest;
    }  
    
    public V mul(V a) 
    {
        V dest = copy();
        dest.set('x', get('x') * a.get('x'));
        dest.set('y', get('y') * a.get('y'));
        dest.set('z', get('z') * a.get('z'));
        return dest;
    }
    public V div(V a) 
    {
        V dest = copy();
        dest.set('x', get('x') / a.get('x'));
        dest.set('y', get('y') / a.get('y'));
        dest.set('z', get('z') / a.get('z'));
        return dest;
    }
    
    public V mul(float a) 
    {
        V v = this.copy();
        v.set('x', v.get('x') * a);
        v.set('y', v.get('y') * a);
        v.set('z', v.get('z') * a);        
        return v;
    }
    
    public void mulAssign(V a)
    {
        set('x', get('x') * a.get('x'));
        set('y', get('y') * a.get('y'));
        set('z', get('z') * a.get('z'));
    }
    
    public void mulAssign(float a) 
    {        
        set('x', get('x') * a);
        set('y', get('y') * a);
        set('z', get('z') * a);     
    }
    
    public V div(float a) 
    {
        V v = this.copy();
        v.set('x', v.get('x') / a);
        v.set('y', v.get('y') / a);
        v.set('z', v.get('z') / a);
        return v;
    }    
    public V neg()        
    {
        V v = this.copy();
        v.set('x', v.get('x')* -1);
        v.set('y', v.get('y')* -1);
        v.set('z', v.get('z')* -1);
        return v;        
    }
    
    public V normalize()
    {
        
        V dest = copy();
        float lenSqr = dest.dot(dest);
        float len    = (float) java.lang.Math.sqrt(lenSqr);
        
        dest.set('x', get('x')/len);
        dest.set('y', get('y')/len);
        dest.set('z', get('z')/len);
        return dest;   
    }
    
    public void normalizeAssign()
    {        
        float lenSqr = this.dot((V) this);
        float len    = (float) java.lang.Math.sqrt(lenSqr);
        
        set('x', get('x')/len);
        set('y', get('y')/len);
        set('z', get('z')/len);
    }
    
    
    public float lenSqr() 
    { 
        return this.dot((V) this);   
    }
    
    public float length() 
    { 
        return (float) java.lang.Math.sqrt(lenSqr());
    }
    
    public void set(V v)
    {
        set('x', v.get('x')); 
        set('y', v.get('y')); 
        set('z', v.get('z'));
    }
    
    
    public static VCoord getFromCoordinate(VCoord v, float x, float y, float z)
    {
        v.set(x, y, z);
        return v;
    }
}
