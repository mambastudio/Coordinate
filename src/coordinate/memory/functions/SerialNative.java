/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.functions;

import coordinate.memory.NativeInteger;

/**
 *
 * @author user
 */
public class SerialNative {
    public static int exclusiveScan(NativeInteger input, NativeInteger output)
    {
        if(!input.equalSize(output))
            throw new UnsupportedOperationException("not equal size");
        output.set(0, 0);
        for(long i = 1; i<input.capacity(); i++)
            output.set(i, input.get(i-1) + output.get(i-1));
        return output.getLast();
    }
    
    public static int reduce(NativeInteger input)
    {
        int sum = 0;
        for(long i = 0; i<input.capacity(); i++)
            sum += input.get(i);
        return sum;
    }
}
