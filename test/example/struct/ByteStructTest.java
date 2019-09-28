/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct;

import coordinate.generic.AbstractCoordinate;
import coordinate.struct.ByteStruct;
import coordinate.struct.StructByteArray;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author user
 */
public class ByteStructTest {
    public static void main(String... args)
    {
        StructByteArray<Intersection> arrayBox = new StructByteArray<>(Intersection.class, 3);
        
        for(Intersection isect : arrayBox)
        {
            isect.setP(3, 14, 77);
            isect.setUV(1, 9);
        }
        
        for(Intersection isect : arrayBox)
            System.out.println(isect);

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
        public void initFromGlobalArray() {            
            ByteBuffer buffer = this.getLocalByteBuffer(ByteOrder.nativeOrder());
            
            int[] offsets = this.getOffsets();
            
            buffer.position(offsets[0]);            
            p.x     = buffer.getFloat();
            p.y     = buffer.getFloat();
            p.z     = buffer.getFloat();
            
            buffer.position(offsets[1]);    
            uv.x    = buffer.getFloat();
            uv.y    = buffer.getFloat();           
        }

        @Override
        public byte[] getArray() {
            
            ByteBuffer buffer = this.getEmptyLocalByteBuffer(ByteOrder.nativeOrder());
           
            int[] offsets = this.getOffsets();
            
            buffer.position(offsets[0]);            
            buffer.putFloat(p.x);
            buffer.putFloat(p.y);
            buffer.putFloat(p.z);
            
            buffer.position(offsets[1]);            
            buffer.putFloat(uv.x);
            buffer.putFloat(uv.y);
            
            return buffer.array();
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
    
    public static class Float4 implements AbstractCoordinate
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
    }
    
    public static class Float2 implements AbstractCoordinate
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
    }
}
