/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

/**
 *
 * @author jmburu
 */
public class Value2DBoolean {
    public boolean x, y;
    
    public Value2DBoolean()
    {
        x = y = false;
    }
    
    public Value2DBoolean(boolean x, boolean y)
    {
        this.x = x; 
        this.y = y;        
    }
    
    public boolean allTrue()
    {
        boolean array[] = new boolean[]{x, y};
        boolean result = true;
        int i;
        for(i = 0; i < array.length; ++i)
        {
            result &= array[i];
        }
        return result;
    }
}
