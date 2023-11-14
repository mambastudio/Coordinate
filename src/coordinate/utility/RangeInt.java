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
public class RangeInt {
    private final int low;
    private final int high;

    public RangeInt(int low, int high){
        this.low = low;
        this.high = high;
    }

    public boolean containsInclusive(int number){
        return (number >= low && number <= high);
    }
    
    public boolean containsExclusive(int number){
        return (number >= low && number < high);
    }
    
    public int low(){return low;}
    public int high(){return high;}
    
    public int size(){return high - low;}
}
