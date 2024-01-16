/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

import java.math.BigInteger;

/**
 *
 * @author user
 * 
 * all checks are always above zero
 * 
 */
public class RangeCheckArray {
    
    public static final void validateIndexRange(long index, long fromIndex, long toIndex) 
    {
        if (index < 0 || index < fromIndex || index >= toIndex) 
            throw new IndexOutOfBoundsException("index " + index + " is out of bounds; expected range [" + fromIndex + ", " + toIndex + "] and index should be greater than or equal to zero.");
    }
    
    public static final void validateRangeSize(long fromIndex, long toIndex, long size) {        
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("Invalid range: [" + fromIndex + ", " + toIndex + "); expected range [0, " + size + ")");
        }
    }
    
    public static final void validateIndexSize(long index, long size) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index " + index + " is out of bounds; expected range [0, " + size + "]");
    }
    
    public static final void validateIndexAboveZero(long index) {
        validateIndexRange(index, 1, Long.MAX_VALUE);
    }
        
    public static final void validateLongToIntBound(long index)
    {
        if (index < 0)
            throw new IndexOutOfBoundsException("index " + index + " is less than zero.");
    
        BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
        BigInteger value = BigInteger.valueOf(index);

        if (value.compareTo(maxInt) > 0)
            throw new IndexOutOfBoundsException("long value is out of Integer bounds [0, " + Integer.MAX_VALUE + "]: " + index);
    }
    
    public static final boolean isIndexInRange(long index, long fromIndex, long toIndex) {
        if(index < fromIndex)
            return false;
        else return index < toIndex;
    }
}
