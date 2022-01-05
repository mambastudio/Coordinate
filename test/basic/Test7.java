/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 *
 * @author user
 */
public class Test7 {
    static int arr[][] = {
            {2, 1, 0, 0},
            {0, 1, 2, 0},
            {1, 2, 1, 0},
            {1, 1, 0, 2}
        };
    public static void main(String... args)
    {
        //int arr[][] = new int[4][4]; //row, col   
        println();
        System.out.println();
        inclusiveScanRow();
        println();
        System.out.println();
        inclusiveScanColumn(false);
        println();
        System.out.println();
        System.out.println(getSumRange(0, 2, 3, 2));
    }
    
    public static int getSumRange(int minX, int minY, int maxX, int maxY)
    {
        int A = get(maxX, maxY);
        int B = get(minX-1, maxY);
        int C = get(maxX, minY-1);
        int D = get(minX-1, minY-1);
        return A+D-B-C;
    }
    
    public static int get(int i, int j)
    {
        if(i < 0)
            return 0;
        if(j < 0)
            return 0;
        if(i > arr[0].length - 1)
            return 0;
        if(j > arr[0].length - 1)
            return 0;        
        else
            return arr[j][i];
    }
    
    public static void inclusiveScanRow()
    {
        for(int i = 0; i<arr[0].length; i++)
            Arrays.parallelPrefix(arr[i], (x, y) -> x + y);
    }
    
    public static void inclusiveScanColumn(boolean reverse)
    {
        for(int i = 0; i<arr[0].length; i++) //column
        {
            int arrayCol[] = getColumn(i);
            if(reverse)
                reverse(arrayCol);
            Arrays.parallelPrefix(arrayCol, (x, y) -> x + y);
            if(reverse)
                reverse(arrayCol);
            for(int j = 0; j<arr[0].length; j++) //row
            {
                setValue(j, i, arrayCol[j]);
            }
        }
    }
    
    public static void setValue(int row, int column, int value)
    {
        arr[row][ column] = value;
    }
    
    public static int[] getColumn(int column)
    {
        return IntStream.range(0, arr.length)
            .map(i -> arr[i][column]).toArray();
    }
    
    public static void reverse(int[] array)
    {
        int i, t; 
        int n = array.length;
        for (i = 0; i < n / 2; i++) { 
            t = array[i]; 
            array[i] = array[n - i - 1]; 
            array[n - i - 1] = t; 
        } 
    }
    
    public static void println()
    {
        for(int j = 0; j<arr[0].length; j++)
        {
            for(int i = 0; i<arr[0].length; i++)
                System.out.printf("%-3d", arr[j][i]);
            System.out.println();
        }
    }
}
