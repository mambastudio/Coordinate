/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import coordinate.memory.NativeInteger;
import java.util.Spliterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author user
 */
public class TestScan {
    public static void main(String... args)
    {
        NativeInteger nativeInteger = new NativeInteger(100000).fill(1);
        
        // Perform parallel prefix sum
        parallelPrefixSum(nativeInteger);
        
        System.out.println(nativeInteger);
    }
    
    private static void parallelPrefixSum(NativeInteger nativeInteger) {
        int size = nativeInteger.capacity();
        if (size <= 0) return;

        NativeInteger prefixSumArray = new NativeInteger(size);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        PrefixSumTask task = new PrefixSumTask(nativeInteger, prefixSumArray, 0, size - 1);
        forkJoinPool.invoke(task);
        forkJoinPool.shutdown();

        // Copy the prefix sum values back to the original NativeInteger
        for (int i = 0; i < size; i++) {
            nativeInteger.set(i, prefixSumArray.get(i));
        }
    }
}
