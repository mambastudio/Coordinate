/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sat;

import coordinate.sampling.Distribution1D;
import coordinate.sampling.sat.SATDistribution1D;
import coordinate.utility.Value1Df;
import java.util.Arrays;

/**
 *
 * @author jmburu
 */
public class Test4 {
    public static void main(String... args)
    {
        float[] func = new float[]{ 1, 1, 1, 2};        
        Distribution1D dist1D = new Distribution1D(func, 0, 4);
        SATDistribution1D sat1D = new SATDistribution1D(func);
        
        
        //System.out.println(sat.sampleContinuousMarginal(3f, null, null));
        Value1Df pdf = new Value1Df();
        int[] offset = new int[1];
        
        float[] pdfS = new float[1];
        int[] offsetS = new int[1];
        
        float u = 9f;
        
        System.out.println(dist1D.sampleContinuous(u, pdf, offset));
        System.out.println("pdf " +pdf);
        System.out.println("offset " +Arrays.toString(offset));
        System.out.println();
        System.out.println(sat1D.sampleContinuous(u, pdfS, offsetS));
        System.out.println("spdf " +Arrays.toString(pdfS));
        System.out.println("soffset " +Arrays.toString(offsetS));
    }
}
