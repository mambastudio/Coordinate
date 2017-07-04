/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;


import coordinate.generic.VCoord;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 * @param <B>
 */
public interface AbstractBound<S extends SCoord, V extends VCoord, B extends AbstractBound> {
    public void include(S s);
    public S getCenter(); 
    public float getCenter(int dim);
    public S getMinimum();
    public S getMaximum();
    public B getInstance();
    
    default void include(B b)
    {
        include((S) b.getMaximum());
        include((S) b.getMinimum());
    }
    
    default int maximumExtent() {
        V diag = (V) getMaximum().sub(getMinimum());
        
        if (diag.get('x') > diag.get('y') && diag.get('x') > diag.get('z')) {
            return 0;
        } else if (diag.get('y') > diag.get('z')) {
            return 1;
        } else {
            return 2;
        }
    }
    
}
