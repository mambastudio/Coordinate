/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct;

import coordinate.struct.structbyte.StructBufferMemory;


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
     
    public static class Struct1 extends StructBufferMemory
    {        
        
        public float f;
        public char  c;
        
        //@arraysize(2)
        //public float[] array2; 
        
        public Struct2 struct;
    }
    
    public static class Struct2 extends StructBufferMemory
    {        
        public long d;
        public float f;        
        public char  c;
    }
}
