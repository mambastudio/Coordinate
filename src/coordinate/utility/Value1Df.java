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
public class Value1Df {
    public float x;
    
    @Override
    public String toString()
    {
        return String.format("%3.9f", x);
    }
}
