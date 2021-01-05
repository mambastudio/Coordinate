/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.newstruct;

import coordinate.generic.AbstractCoordinateFloat;
import coordinate.struct.ByteStruct;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        Alignment align = StructUtility.getAlignment(Intersection.class);
        System.out.println(Arrays.toString(align.getOffsets()) + " size " +align.getSize());
        
        Integer[] intArr= {1,2,6,2,234,3,54,6,4564,456};

        IntStream.range(0, intArr.length-1).parallel().
                        reduce((a,b)->intArr[a]<intArr[b]? b: a).
                        ifPresent(ix -> System.out.println("Index: " + ix + ", value: " + intArr[ix]));
    }
    
    public static class Intersection extends ByteStruct
    {   
        public int i;
        public int j;
        public Float4 p;       //16 bytes
        public InnerStruct inner1;
        public Float2 uv;      //8 bytes      
        public Float4 p1;       //16 bytes
        public InnerStruct inner2;
        
        
        
    }
    
    public static class InnerStruct extends ByteStruct
    {
        public Float4 p1;       //16 bytes
        public int i;
    }
    
    public static class Float4 implements AbstractCoordinateFloat
    {
        public float x, y, z, w;
        
        @Override
        public String toString()
        {
            return "x " +x+ " y " +y+ " z " +z;
        }

        @Override
        public int getSize() {
            return 4;
        }

        @Override
        public int getByteSize() {
            return 4;
        }

        @Override
        public float[] getArray() {
            return new float[]{x, y, z, 0};
        }

        @Override
        public float get(char axis) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public void set(char axis, float value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void set(float... values) {
            x = values[0];
            y = values[1];
            z = values[2];
            w = values[3];
        }

        @Override
        public void setIndex(int index, float value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    public static class Float2 implements AbstractCoordinateFloat
    {
        public float x, y;
        
        
        @Override
        public String toString()
        {
            return "x " +x+ " y " +y;
        }

        @Override
        public int getSize() {
            return 2;
        }

        @Override
        public int getByteSize() {
            return 4;
        }

        @Override
        public float get(char axis) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public void set(char axis, float value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void set(float... values) {
            x = values[0];
            y = values[1];
        }

        @Override
        public void setIndex(int index, float value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float[] getArray() {
            return new float[]{x, y};
        }
    }
}
