/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import coordinate.memory.NativeInteger;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author user
 */
public class TestScan1 {
    public static void main(String... args)
    {
        NativeInteger input1  = new NativeInteger(10000000).fill(1);        
        NativeInteger input2  = new NativeInteger(10000000).fill(1);
        int[] arr =                       new int[10000000];
        
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        
        for(int i = 0; i<1; i++)
        {
            long startTime, endTime; 

            startTime = System.nanoTime();
            sequentialScan(input1);
            endTime = System.nanoTime();
            System.out.println(getReadableTime(endTime - startTime)); 

            startTime = System.nanoTime();
            // Perform the upsweep phase using ForkJoinPool
            BlellochScanReduceTask upsweepTask = new BlellochScanReduceTask(input2, 0, input2.capacity());
            forkJoinPool.invoke(upsweepTask);       

            // Perform the downsweep phase using ForkJoinPool
            BlellochScanDownSweepTask downsweepTask = new BlellochScanDownSweepTask(input2, 0, input2.capacity(), 0);
            forkJoinPool.invoke(downsweepTask);
            endTime = System.nanoTime();

            System.out.println(getReadableTime(endTime - startTime)); 
            System.out.println(input2.getLast());

            startTime = System.nanoTime();
            Arrays.parallelPrefix(arr, 0, arr.length, (a, b)->a + b);
            endTime = System.nanoTime();
            System.out.println(getReadableTime(endTime - startTime)); 
        }
    }
    
    public static void sequentialScan(NativeInteger input)
    {
        // Perform the sequential scan (prefix sum)
        int prefixSum = 0;
        input.set(0, 0);
        for (int i = 1; i < input.capacity(); i++) {
            prefixSum += input.get(i);
            input.set(i, prefixSum);
        }
    }
    
    private static String getReadableTime(long nanos){
        
        long tempMillis = nanos / (1000 * 1000);
        long millis = tempMillis % 1000;
        long tempSec = tempMillis / 1000;
        long sec = tempSec % 60;
        long min = (tempSec / 60) % 60;
        long hour = (tempSec / (60 * 60)) % 24;
        long day = (tempSec / (24 * 60 * 60)) % 24;

        return String.format("%dd %dh %dm %ds %dms", day, hour, min, sec, millis);

    }
    
}
