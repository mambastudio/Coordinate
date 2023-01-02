/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.shapes;

import coordinate.generic.AbstractRay;
import coordinate.generic.SCoord;
import coordinate.generic.VCoord;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 * @param <R>
 */
public abstract class TriangleShape<S extends SCoord, V extends VCoord, R extends AbstractRay<S, V>> 
{
    private final S p1;
    private final S p2;
    private final S p3;
    private final V n;
    
    protected TriangleShape(S p1, S p2, S p3)
    {
        this.p1 = p1; this.p2 = p2; this.p3 = p3;
        this.n = null;
    }
    
    public boolean intersect(R r)
    {
        return this.intersect(r, null);
    }
    
    public boolean intersect(R r, float[] tuv)
    {
        return this.mollerIntersection(r, tuv, p1, p2, p3);
    }
    
    private boolean mollerIntersection(R r, float[] tuv, S p1, S p2, S p3)
    {
        V e1, e2, h, s, q;
        double a, f, b1, b2;

        e1  = (V) p2.sub(p1);
        e2  = (V) p3.sub(p1);
        h   = (V) r.getDirection().cross(e2);
        a   = e1.dot(h);

        if (a > -0.0000001 && a < 0.0000001)
            return false;

        f = 1/a;
        
        s = (V) r.getOrigin().sub(p1);
	b1 = f * s.dot(h);

        if (b1 < 0.0 || b1 > 1.0)
            return false;

        q =  (V) s.cross(e1);
	b2 = f * r.getDirection().dot(q);

	if (b2 < 0.0 || b1 + b2 > 1.0)
            return false;

	float t = (float) (f * e2.dot(q));
        
        if(r.isInside(t)) 
        {
            if(tuv != null)
            {
                tuv[0] = t;
                tuv[1] = (float) b1;
                tuv[2] = (float) b2;
            }
            return true;
        }
        else
            return false;
    }
}
