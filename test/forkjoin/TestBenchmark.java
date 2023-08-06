/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forkjoin;

import coordinate.memory.NativeInteger;
import coordinate.memory.functions.ParallelNative;

/**
 *
 * @author user
 */
public class TestBenchmark {
    public static void main(String... args)
    {
        // Assume you have a NativeInteger with 5 elements
        long n = 10000000;
        NativeInteger n1 = new NativeInteger(n).fill(1);  
        NativeInteger n2 = new NativeInteger(n).fill(1); 
        
        long startTime, endTime;
        
        for(int x = 0; x<50; x++)
        {
            startTime = System.nanoTime();
            long sum = 0;
            for(long i = 0; i<n; i++)
            {
                sum += n1.get(i);
            }
            endTime = System.nanoTime();
            System.out.println("serial   " +getReadableTime(endTime - startTime));

            // Convert the NativeInteger into a Stream and perform reduction
            startTime = System.nanoTime();
            sum = ParallelNative.streamInteger(n2)
                    .reduce(0, (accumulator, element) -> accumulator + element);
            endTime = System.nanoTime();
            System.out.println("parallel " +getReadableTime(endTime - startTime));
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
