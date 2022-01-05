/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sat;

import coordinate.sampling.sat.SATSubgrid;
import coordinate.println.PrintFloat;
import coordinate.sampling.sat.SAT;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Test1 {
    public static void main(String... args)
    {
        SAT table = new SAT(4, 4);
        table.setArray(
                0, 0,    0,   0,
                0, 0.9f, 0,   0,
                0, 0,    0,   0,
                0, 0,    0.1f, 0
        );        
             
        SATSubgrid subgrid = new SATSubgrid(4, 4, 4, 4);
        subgrid.setArray(
                1, 1, 1, 1, 
                1, 1, 1, 1,
                1, 1, 1, 1, 
                1, 1, 1, 1 
        );
        PrintFloat printFloat = new PrintFloat(subgrid.getSATArray());
        printFloat.setDimension(4, 4);
        printFloat.printArray();
        
        System.out.println(subgrid.subgridIndexFromGlobalIndex(0));
    }
    
 
}
