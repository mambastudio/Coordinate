/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct;

import coordinate.generic.AbstractCoordinateFloat;
import coordinate.struct.cache.StructBufferCache;
import coordinate.struct.structbyte.StructBufferMemory;
/**
 *
 * @author user
 */
public class StructureArrayTest {
    public static void main(String... args)
    {
        StructBufferCache<Intersection> arrayBox = new StructBufferCache<>(Intersection.class, 4);
        Intersection isect = new Intersection();     
        isect.value = 8;
        isect.atom.charge = 3;
        isect.atom2.charge = 67;
        
        System.out.println(isect.getLayout());
               
        arrayBox.setStruct(isect, 1);
        
        System.out.println(arrayBox.get(1).atom.charge);
        System.out.println(arrayBox.get(1).atom2.charge);
        
        
        
        /*
        arrayBox.add(new Intersection());
        Intersection isect = arrayBox.get(0);
        
        
        isect.setP(2, 14, 70);
        isect.setUV(3, 9);
        
       
        for(Intersection intersection : arrayBox)
            System.out.println(intersection);
         
        isect.setUV(3, 1344);
        isect.setP(3, 0, 0);
        isect.setData(2, 34);

        for(Intersection intersection : arrayBox)
            System.out.println(intersection);
*/
    }
    
    public static class Intersection extends StructBufferMemory
    {   
       
        public int value;
        public long value2;
        public Atom atom;
        public Atom atom2;
        
        public void setCharge(int charge)
        {
            this.atom.charge = charge;
            this.refreshGlobalBuffer();
        }
        
        
        
        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            return builder.toString();
        }

        
    }
    
    public static class Atom extends StructBufferMemory
    {
        public int charge;
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

        @Override
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

        @Override
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
