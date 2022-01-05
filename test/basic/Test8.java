/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.sampling.Distribution1D;
import coordinate.sampling.Distribution2D;
import coordinate.sampling.sat.SATDistribution1D;
import coordinate.sampling.sat.SATDistribution2D;
import coordinate.sampling.sat.SAT;
import coordinate.utility.Value1Df;
import coordinate.utility.Value2Di;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Test8 {
    public static void main(String... args)
    {
        test6();
    }
    
    public static void test1()
    {
        SAT table = new SAT(4, 4);
        table.setArray(
                2, 1, 0, 0,
                0, 1, 2, 0,
                1, 2, 1, 0,
                1, 1, 0, 2
        );  
        
        
        Distribution2D dist = new Distribution2D(new float[]{
                                                    2, 1, 0, 0,
                                                    0, 1, 2, 0,
                                                    1, 2, 1, 0,
                                                    1, 1, 0, 2
                                        }, 4, 4);
        System.out.println(dist.pMarginalCdfString());
        System.out.println();
       
        System.out.println();
        
        Distribution1D dist1D = new Distribution1D(new float[]{
                                    2, 1, 0, 0
                                }, 0, 4);
    }
    
    public static void test2()
    {
        float[] arr = new float[]{4, 4, 1, 8, 2, 20};
        SATDistribution1D distSAT = new SATDistribution1D(arr);        
        System.out.println(distSAT.cdfString());
        
        Distribution1D dist = new Distribution1D(arr, 0, arr.length);
        System.out.println(dist.cdfString());
        
        float rand = (float) Math.random();
        int offsetSAT = distSAT.sampleDiscreteConditional(rand);
        int offset = dist.sampleDiscrete(rand, new Value1Df());        
        System.out.println(offsetSAT+ " " +offset);
                
        float continuousSAT = distSAT.sampleContinuousConditional(rand);
        float continous = dist.sampleContinuous(rand, new Value1Df());
        System.out.println(continuousSAT+ " " +continous);
    }
    
    public static void test3()
    {
        float[] array = new float[]{
                                2, 1, 0, 0, 3,
                                0, 1, 2, 0, 6,
                                1, 2, 1, 0, 5,
                                1, 1, 0, 2, 9,
        };
        SATDistribution2D distSAT = new SATDistribution2D(array, 5, 4);
        Distribution2D dist = new Distribution2D(array, 5, 4);
        
        float rand0 = (float) Math.random();
        float rand1 = (float) Math.random();
        
        float[] uv  = new float[2];
        float[] pdf = new float[1];
        
        float[] uvSAT  = new float[2];
        float[] pdfSAT = new float[1];
        
        dist.sampleContinuous(rand0, rand1, uv, pdf);
        distSAT.sampleContinuous(rand0, rand1, uvSAT, pdfSAT);
        
        System.out.println(Arrays.toString(uv));
        System.out.println(Arrays.toString(uvSAT));
        
        System.out.println(Arrays.toString(pdf));
        System.out.println(Arrays.toString(pdfSAT));
    }
    
    public static void test4()
    {
        float[] func = new float[]{
                2, 1, 3.6f, 4
        };
        
        SATDistribution1D satConditional = new SATDistribution1D(func);        
        Distribution1D distConditional = new Distribution1D(func, 0, func.length);
        
        int sIndex;
        float sPdf[] = new float[1];
        int dIndex;
        float dPdf[] = new float[1];
        
        float rand0 = (float) Math.random();
        
        sIndex = satConditional.sampleDiscrete(rand0, sPdf);
        dIndex = distConditional.sampleDiscrete(rand0, dPdf);
        
        System.out.println("index sat  " +sIndex);
        System.out.println("index spdf " +Arrays.toString(sPdf));
        System.out.println("index dist " +dIndex);
        System.out.println("index dpdf " +Arrays.toString(dPdf));
    }
    
    public static void test5()
    {
        float[] func = new float[]{
                2, 1, 0, 0,
                0, 1, 2, 0,
                1, 2, 1, 0,
                1, 1, 0, 2
        };
        
        SATDistribution2D satDiscrete = new SATDistribution2D(func, 4, 4);        
        Distribution2D distDiscrete = new Distribution2D(func, 4, 4);
        
        float rand0 = (float) Math.random();
        float rand1 = (float) Math.random();
        
        int[] sOffset = new int[2];
        float sPdf[] = new float[1];
        Value2Di dOffset = new Value2Di();
        Value1Df dPdf = new Value1Df();
        
        satDiscrete.sampleDiscrete(rand0, rand1, sOffset, sPdf);
        distDiscrete.sampleDiscrete(rand0, rand1, dOffset, dPdf);
        
        System.out.println("index sat  " +Arrays.toString(sOffset));
        System.out.println("index spdf " +Arrays.toString(sPdf));
        System.out.println("index dist " +dOffset);
        System.out.println("index dpdf " +dPdf);
    }
    
    public static void test6()
    {
        Distribution1D dist = new Distribution1D(0, 0, 0, 0, 1);
        
        float u0 = 1f;
        
        Value1Df pdf1 = new Value1Df();
        Value1Df pdf2 = new Value1Df();
        float sample1;
        int   sample2;
        
        sample1 = dist.sampleContinuous(u0, pdf1);
        sample2 = dist.sampleDiscrete(u0, pdf2);
        
        System.out.println("Continous:");
        System.out.println("sample " +sample1);
        System.out.println("pdf    " +pdf1);
        System.out.println("Discrete:");
        System.out.println("sample " +sample2);
        System.out.println("pdf    " +pdf2);
    }
}
