/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sat;

import coordinate.sampling.Distribution1D;
import coordinate.sampling.Distribution2D;
import coordinate.sampling.sat.SAT;

/**
 *
 * @author jmburu
 */
public class Test4 {
    public static void main(String... args)
    {
        float[] func1 = new float[]{ 1, 1, 1, 4
        };        
        Distribution1D dist2D = new Distribution1D(func1, 0, 4);
        
        //System.out.println(sat.sampleContinuousMarginal(3f, null, null));
        System.out.println(dist2D.sampleContinuous(1f, null, null));
    }
}
