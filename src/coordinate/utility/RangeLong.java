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
public class RangeLong {
    private final long low;
    private final long high;

    public RangeLong(long low, long high){
        this.low = low;
        this.high = high;
    }

    public boolean containsInclusive(long number){
        return (number >= low && number <= high);
    }
    
    public boolean containsExclusive(long number){
        return (number >= low && number < high);
    }
    
    public long low(){return low;}
    public long high(){return high;}
    
    public long size(){return high - low;}
}
