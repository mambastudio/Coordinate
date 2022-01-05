/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sat;

import coordinate.sampling.sat.SATSubgrid;
import coordinate.sampling.Distribution2D;
import coordinate.sampling.sat.SAT;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Test2 {
    public static void main(String... args)
    {
        SAT sat = new SAT(4, 4);
        sat.setArray(
                1, 1, 1, 1,
                1, 7, 8, 1,
                1, 1, 1, 1,
                1, 1, 1, 1
        );  
        
        Distribution2D dist2D = new Distribution2D(new float[]{
                                                    1, 1, 1, 1,
                                                    1, 7, 8, 1,
                                                    1, 1, 1, 1,
                                                    1, 1, 1, 1
                                        }, 4, 4);
        
        SATSubgrid subgrid = new SATSubgrid(4, 4, 8, 8);
        subgrid.setArray(
                1, 1, 1, 1, 1, 1, 1, 1, 
                1, 7, 8, 1, 1, 7, 8, 1, 
                1, 1, 1, 1, 1, 1, 1, 1, 
                1, 1, 1, 1, 1, 1, 1, 1,  
                1, 1, 1, 1, 1, 1, 1, 1, 
                1, 1, 1, 1, 1, 1, 1, 1, 
                1, 1, 1, 1, 1, 1, 1, 1, 
                1, 1, 1, 1, 1, 1, 1, 1
        );
               
        float rand0 = (float) Math.random();
        float rand1 = (float) Math.random();        
                
        float[] uv  = new float[2];
        float[] pdf = new float[1];
        
        float[] uvSAT  = new float[2];
        float[] pdfSAT = new float[1];
        
        float[] uvSATSub  = new float[2];
        float[] pdfSATSub = new float[1];
                
       
        dist2D.sampleContinuous(rand0, rand1, uv, pdf);
        sat.sampleContinuous(rand0, rand1, uvSAT, null, pdfSAT);
        subgrid.sampleContinuous(1, rand0, rand1, uvSATSub, pdfSATSub);
        
        System.out.println(Arrays.toString(uv));
        System.out.println(Arrays.toString(uvSAT));
        System.out.println(Arrays.toString(uvSATSub));
        
        System.out.println(Arrays.toString(pdf));
        System.out.println(Arrays.toString(pdfSAT));       
        System.out.println(Arrays.toString(pdfSATSub));

    }
}
