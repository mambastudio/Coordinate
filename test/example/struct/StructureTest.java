/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct;

import coordinate.struct.structbyte.Structure;
import coordinate.struct.annotation.arraysize;
import coordinate.utility.Value1Di;

/**
 *
 * @author user
 */
public class StructureTest {
    public static void main(String... args)
    {
        Struct1  struct = new Struct1();
        
        System.out.println(struct.getLayout());       
    }
     
    public static class Struct1 extends Structure
    {        
        
        public float f;
        public char  c;
        
        //@arraysize(2)
        //public float[] array2; 
        
        public Struct2 struct;
    }
    
    public static class Struct2 extends Structure
    {        
        public long d;
        public float f;        
        public char  c;
    }
}
