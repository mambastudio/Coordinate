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
 */
public class RangeCheck {
    public static final void rangeAboveZero(long index)
    {
        if (index < 1)
            throw new IndexOutOfBoundsException("index out of bound " +index);
    }
    
    public static final void rangeIntegerBound(long index)
    {
        BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
        BigInteger value = BigInteger.valueOf(index);
        if(value.compareTo(maxInt) > 0)
            throw new IndexOutOfBoundsException("long value is greater than Integer.MAX_VALUE " +index);
    }
    
    public static final void rangeNotNegative(long index)
    {
        if (index < 0)
            throw new IndexOutOfBoundsException("index out of bound " +index);
    }
    
    public static final void checkEquality(long size1, long size2)
    {
        if (size1 != size2)
            throw new IndexOutOfBoundsException("sizes not the same " +size1+ " " +size2);
    }
    
    //Size should encompass the fromIndex and toIndex 
    public static final void rangeCheck(long offset, long size) {
        if (offset < 0)
            throw new IndexOutOfBoundsException("offset is less than zero " + offset);
        if (offset >= size)
            throw new IndexOutOfBoundsException("offset is beyond range, offset = " + offset + " size = " +size);        
    }    
    
    //Size should encompass the fromIndex and toIndex 
    public static final void rangeCheckBound(long fromIndex, long toIndex, long size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex is less than zero " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex is greater than size, toIndex = " + toIndex + " size = " +size);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }
    
    public static final boolean isRangeInBound(long index, long fromIndex, long toIndex) {
        if(index < fromIndex)
            return false;
        else return index < toIndex;
    }
}
