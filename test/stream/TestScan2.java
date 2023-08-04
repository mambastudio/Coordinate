/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import coordinate.memory.NativeInteger;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import static stream.BlellochScanReduceTask.SEQUENTIAL_THRESHOLD;

/**
 *
 * @author user
 */
public class TestScan2 {
    public static void main(String... args)
    {     
        NativeInteger input1  = new NativeInteger(8);
        input1.copyFromArr(new int[]{1, 2, 3 ,4 ,5, 6, 7, 8}, 0, 8);
        
        ForkJoinPool forkJoinPool = new ForkJoinPool();
                        
        // Perform the upsweep phase using ForkJoinPool
        BlellochScanReduceTask upsweepTask = new BlellochScanReduceTask(input1, 0, input1.capacity(), 0);
        forkJoinPool.invoke(upsweepTask);   
                
        // Perform the downsweep phase using ForkJoinPool
        BlellochScanDownSweepTask downsweepTask = new BlellochScanDownSweepTask(input1, 0, input1.capacity(), 0);
        forkJoinPool.invoke(downsweepTask);
            
        System.out.println(input1);
    }
    
    public static void sequentialScan(NativeInteger input)
    {
        // Perform the sequential scan (prefix sum)
        int prefixSum = 0;
        for (int i = 0; i < input.capacity(); i++) {
            prefixSum += input.get(i);
            input.set(i, prefixSum);
        }
    }
    
    private static String getReadableTime(long nanos){
        
        long tempSec    = nanos/(1000*1000*1000);
        long sec        = tempSec % 60;
        long min        = (tempSec /60) % 60;
        long hour       = (tempSec /(60*60)) % 24;
        long day        = (tempSec / (24*60*60)) % 24;

        return String.format("%dd %dh %dm %ds", day,hour,min,sec);

    }
    
}
