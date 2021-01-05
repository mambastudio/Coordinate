/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct;

import coordinate.generic.AbstractCoordinateFloat;
import coordinate.struct.ByteStruct;
import coordinate.struct.StructByteArray;

/**
 *
 * @author user
 */
public class ByteStructTest {
    public static void main(String... args)
    {
        StructByteArray<Intersection> arrayBox = new StructByteArray<>(Intersection.class);
        arrayBox.add(new Intersection());
        Intersection isect = arrayBox.get(0);
        
        
        isect.setP(2, 14, 70);
        isect.setUV(3, 9);
        
       
        for(Intersection intersection : arrayBox)
            System.out.println(intersection);
         
        isect.setUV(3, 1344);

        for(Intersection intersection : arrayBox)
            System.out.println(intersection);
    }
    
    public static class Intersection extends ByteStruct
    {   
        public Float4 p;       //16 bytes
        public final Float2 uv;      //8 bytes
      
        public Intersection()
        {
            p   = new Float4();
            uv  = new Float2();
        }
        
        public void setP(float x, float y, float z)
        {
            p.x = x; p.y = y; p.z = z;
            this.refreshGlobalArray();
        }
        
        public void setUV(float x, float y)
        {
            uv.x = x; uv.y = y;
            this.refreshGlobalArray();
        }
        
        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("p    ").append(p).append("\n");
            builder.append("uv   ").append(uv).append("\n");
            return builder.toString();
        }

        
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
