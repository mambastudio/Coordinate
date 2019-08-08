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
public class Value2Di {
    public int x, y;
    
    public Value2Di()
    {
        x = y = 0;
    }
    
    public Value2Di(int x, int y)
    {
        this.x = x; this.y = y;
    }
    
    public void set(int x, int y)
    {
        this.x = x; this.y = y;
    }
}
