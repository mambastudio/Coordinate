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
public interface VCoord<S extends SCoord, V extends VCoord> extends AbstractCoordinateFloat{
    public abstract V getCoordInstance();
    public abstract V copy();
        
    public default V newV(float x, float y, float z)
    {
        V dest = (V) copy();
        dest.set(x, y, z);
        return dest;
    }
    
    public default float dot(V b) 
    {
        return get('x') *b.get('x') + get('y')*b.get('y') + get('z')*b.get('z');
    }
    
    public default float absDot(V b)
    {
        return Math.abs(dot(b));
    }
    
    public default V cross(V t)
    {
        V dest = (V) t.copy();
        dest.set('x', get('y') * t.get('z') - get('z') * t.get('y'));
        dest.set('y', get('z') * t.get('x') - get('x') * t.get('z'));
        dest.set('z', get('x') * t.get('y') - get('y') * t.get('x'));
        return dest;
    }
    
    public default V addAssign(V v) {
        set('x', get('x')+v.get('x'));
        set('y', get('y')+v.get('y'));
        set('z', get('z')+v.get('z'));
        return (V) this;
    }
    
    public default V addAssign(float f) 
    {
        set('x', get('x')+f);
        set('y', get('y')+f);
        set('z', get('z')+f);
        return  (V) this;
    }
    
    public default V add(float a)
    {
        V v = getCoordInstance();
        v.set('x', get('x') + a);
        v.set('y', get('y') + a);
        v.set('z', get('z') + a);        
        return v;
    }
    
    public default V add(V a) 
    {
        V dest = copy();
        dest.set('x', get('x') + a.get('x'));
        dest.set('y', get('y') + a.get('y'));
        dest.set('z', get('z') + a.get('z'));
        return dest;
    }  
    
    public default V mul(V a) 
    {
        V dest = copy();
        dest.set('x', get('x') * a.get('x'));
        dest.set('y', get('y') * a.get('y'));
        dest.set('z', get('z') * a.get('z'));
        return dest;
    }
    public default V div(V a) 
    {
        V dest = copy();
        dest.set('x', get('x') / a.get('x'));
        dest.set('y', get('y') / a.get('y'));
        dest.set('z', get('z') / a.get('z'));
        return dest;
    }
    
    public default V mul(float a) 
    {
        V v = this.copy();
        v.set('x', v.get('x') * a);
        v.set('y', v.get('y') * a);
        v.set('z', v.get('z') * a);        
        return v;
    }
    
    public default void mulAssign(V a)
    {
        set('x', get('x') * a.get('x'));
        set('y', get('y') * a.get('y'));
        set('z', get('z') * a.get('z'));
    }
    
    public default void mulAssign(float a) 
    {        
        set('x', get('x') * a);
        set('y', get('y') * a);
        set('z', get('z') * a);     
    }
    
    public default V div(float a) 
    {
        V v = this.copy();
        v.set('x', v.get('x') / a);
        v.set('y', v.get('y') / a);
        v.set('z', v.get('z') / a);
        return v;
    }   
    
    public default V divAssign(V v) {
        set('x', get('x')/v.get('x'));
        set('y', get('y')/v.get('y'));
        set('z', get('z')/v.get('z'));
        return (V) this;
    }
    
    public default V neg()        
    {
        V v = this.copy();
        v.set('x', v.get('x')* -1);
        v.set('y', v.get('y')* -1);
        v.set('z', v.get('z')* -1);
        return v;        
    }
    
    public default V normalize()
    {
        
        V dest = copy();
        float lenSqr = dest.dot(dest);
        float len    = (float) java.lang.Math.sqrt(lenSqr);
        
        dest.set('x', get('x')/len);
        dest.set('y', get('y')/len);
        dest.set('z', get('z')/len);
        return dest;   
    }
    
    public default void normalizeAssign()
    {        
        float lenSqr = this.dot((V) this);
        float len    = (float) java.lang.Math.sqrt(lenSqr);
        
        set('x', get('x')/len);
        set('y', get('y')/len);
        set('z', get('z')/len);
    }
    
    
    public default float lenSqr() 
    { 
        return this.dot((V) this);   
    }
    
    public default float length() 
    { 
        return (float) java.lang.Math.sqrt(lenSqr());
    }
    
    public default void setValue(V v)
    {
        set('x', v.get('x')); 
        set('y', v.get('y')); 
        set('z', v.get('z'));
    }
    
    default boolean isZero()
    {
        return get('x') == 0 && get('y') == 0 && get('z') == 0;
    }
    
    default boolean hasUndefinedValues()
    {
        return (Float.isNaN(this.get('x'))) || (Float.isNaN(this.get('y'))) || (Float.isNaN(this.get('z'))) ||
               (Float.isInfinite(this.get('x'))) || (Float.isInfinite(this.get('y'))) || (Float.isInfinite(this.get('z')));
    }
    
    default S asScalar()
    {
        throw new UnsupportedOperationException("not yet supported");
    }
    
    default V abs()
    {
        V v = copy();
        v.set('x', Math.abs(this.get('x')));
        v.set('y', Math.abs(this.get('y')));
        v.set('z', Math.abs(this.get('z')));
        return v;
    }
    
    default V sign()
    {
        V v = copy();
        v.set('x', Math.signum(this.get('x')));
        v.set('y', Math.signum(this.get('y')));
        v.set('z', Math.signum(this.get('z')));
        return v;
    }
    
    
    public static VCoord getFromCoordinate(VCoord v, float x, float y, float z)
    {
        v.set(x, y, z);
        return v;
    }
}
