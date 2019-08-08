/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;



/**
 *
 * @author user
 */
public class SimpleUIntRadixSort {
    public static void main(String... args)
    {
        int[] array = new Random().ints(10, 0, 10).toArray();
        int[] values0 = new int[array.length];
        int[] values1 = new int[array.length];
        
        sort(array, values0, values1);
              
        System.out.println(Arrays.toString(array));
    }
    
    
    public static void sort(int[] values, int[] values0, int[] values1)
    {
        for (int bit = 0; bit < 31; ++bit)
        {
            AtomicInteger insertIndex0 = new AtomicInteger(), insertIndex1 = new AtomicInteger();
            int mask = 0x00000001 << bit;
             
            //parallel sort
            for (int i = 0; i < values.length; ++i)
            {
                if ((values[i] & mask) == 0) 
                    values0[insertIndex0.getAndIncrement()] = values[i];
                else
                    values1[insertIndex1.getAndIncrement()] = values[i];
            }

            //parallel insert
            for(int i = 0; i< values.length; i++)
            {
                if(i < insertIndex0.get()) 
                    values[i] = values0[i];
                else
                    values[i] = values1[i - insertIndex0.get()];
            }
        }
    }
}
