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
public class Value3DBoolean {
    public boolean x, y, z;
    
    public Value3DBoolean()
    {
        x = y = z = false;
    }
    
    public Value3DBoolean(boolean x, boolean y, boolean z)
    {
        this.x = x; 
        this.y = y;
        this.z = z;
    }
    
    public boolean allTrue()
    {
        boolean array[] = new boolean[]{x, y, z};
        boolean result = true;
        int i;
        for(i = 0; i < array.length; ++i)
        {
            result &= array[i];
        }
        return result;
    }
}
