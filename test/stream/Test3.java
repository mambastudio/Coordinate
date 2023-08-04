/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author jmburu
 */
public class Test3 {
    public static void main(String... args)
    {
        int[] inputArray = {1, 2, 3, 4, 5, 6, 7, 8};

        System.out.println("Input Array:");
        System.out.println(Arrays.toString(inputArray));

        parallelPrefixSum(inputArray);

        
    }
    
    public static void parallelPrefixSum(int[] array) {
        int[] prefixSum = new int[array.length];
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        PrefixSumTask task = new PrefixSumTask(array, prefixSum, 0, array.length);
        forkJoinPool.invoke(task);
        forkJoinPool.shutdown();

        // Copy the prefix sum values to the original input array
        System.arraycopy(prefixSum, 0, array, 0, prefixSum.length);
        
        System.out.println("\nPrefix Sum:");
        System.out.println(Arrays.toString(prefixSum));
    }
}
