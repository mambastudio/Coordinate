/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sat;

import coordinate.sampling.sat.SAT;
import coordinate.sampling.sat.SATDistribution1D;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Test3 {
    public static void main(String... args)
    {
        test2();
    }
    
    public static void test1()
    {
        float[] func1 = new float[]{
                1, 1, 1, 
                1, 9, 1, 
                1, 1, 1             
        };
        
        float[] func2 = new float[]{
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1,
                1, 1, 9, 1, 1,
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        };
        
        SAT sat1 = new SAT(3, 3);
        sat1.setArray(func1);
        
        SAT sat2 = new SAT(5, 5);
        sat2.setArray(func2);
        sat2.setRegion(1, 1, 3, 3);
        
        float rand0 = (float) Math.random();
        float rand1 = (float) Math.random();        
                
        int[] offsetSAT1  = new int[2];
        float[] pdfSAT1 = new float[1];
        
        int[] offsetSAT2  = new int[2];
        float[] pdfSAT2 = new float[1];
        
        float[] uvSAT22 = new float[2];
        int[] offsetSAT22  = new int[2];
        float[] pdfSAT22 = new float[1];
        
        
        
        sat1.sampleDiscrete(rand0, rand1, offsetSAT1, pdfSAT1);
        sat2.sampleDiscrete(rand0, rand1, offsetSAT2, pdfSAT2);        
        sat2.sampleContinuous(rand0, rand1, uvSAT22, offsetSAT22, pdfSAT22);
        
        int ff[] = new int[]{(int)(uvSAT22[0]*5), (int)(uvSAT22[1]*5)};
        
        
        System.out.println(Arrays.toString(offsetSAT1));
        System.out.println(Arrays.toString(offsetSAT2));
        System.out.println(Arrays.toString(offsetSAT22));
        System.out.println(Arrays.toString(ff));
                
        System.out.println(Arrays.toString(pdfSAT1));
        System.out.println(Arrays.toString(pdfSAT2));   
        System.out.println(Arrays.toString(pdfSAT22));  
        
    }
    
    public static void test2()
    {
        SATDistribution1D sat1 = new SATDistribution1D(1, 2, 1);
        SATDistribution1D sat2 = new SATDistribution1D(1, 1, 2, 1, 1);
        sat2.setRegionX(1, 3);
        
        float rand0 = (float) Math.random();
        
        int uvSAT1;
        float[] pdfSAT1 = new float[1];
        
        int uvSAT2;
        float[] pdfSAT2 = new float[1];
        
        uvSAT1 = sat1.sampleDiscrete(rand0, pdfSAT1);
        uvSAT2 = sat2.sampleDiscrete(rand0, pdfSAT2);
        
        System.out.println(uvSAT1);
        System.out.println(uvSAT2);
                
        System.out.println(Arrays.toString(pdfSAT1));
        System.out.println(Arrays.toString(pdfSAT2)); 
        
      
    }
}
