/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import coordinate.memory.NativeInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author user
 */
public class TestReduce {
    public static void main(String... args)
    {
        // Assume you have a NativeInteger with 5 elements
        NativeInteger nativeInteger = new NativeInteger(124922).fill(21);
        

        // Convert the NativeInteger into a Stream and perform reduction
        int sum = convertToStream(nativeInteger)
                .reduce(0, (accumulator, element) -> accumulator + element);

        System.out.println("Sum of elements: " + sum);
    }
    
    // Convert the NativeInteger to a Stream using a custom Spliterator
    private static Stream<Integer> convertToStream(NativeInteger nativeInteger) {
        return StreamSupport.stream(new NativeIntegerSpliterator(nativeInteger), true);
    }
}
