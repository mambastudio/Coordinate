/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.newstruct;

import coordinate.struct.ByteStruct;
import coordinate.struct.annotation.ByteStructAnnotation;
import coordinate.struct.refl.ByteStructInfo;
import example.Point3f;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Test2 {
    public static void main(String... args)
    {
        int offsets[] = ByteStructInfo.offsets(Struct.class).getArrayFieldOffsets();
        System.out.println(Arrays.toString(offsets));
    }
    
    public static class Struct extends ByteStruct
    {   
        
        public int k;
        @ByteStructAnnotation(arraysize = 1)
        public int[] array;
        //public Point3f point;
        //public Struct2 struct;
                
        public Struct()
        {           
            k = 0;
            //point = new Point3f();
            //struct = new Struct2();
            array = new int[1];
        }        
    }
    
    public static class Struct2 extends ByteStruct
    {
        public int f;
        public int k;
        
        public Struct2()
        {
            f = 0;
        }
    }
}
