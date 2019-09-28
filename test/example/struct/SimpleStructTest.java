/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct;

import coordinate.struct.FloatStruct;
import coordinate.struct.StructFloatArray;

/**
 *
 * @author user
 */
public class SimpleStructTest {
    public static void main(String... args)
    {
        StructFloatArray<Box> arrayBox = new StructFloatArray<>(Box.class, 6);
        
        for(Box box: arrayBox)
        {
            box.setMin(2, 4, 4);
            box.setMax(100, 0.7f, 1);
        }
        
        for(Box box: arrayBox)
            System.out.println(box);        
    }
    
    public static class Box extends FloatStruct
    {
        private float xmin, ymin, zmin;
        private float xmax, ymax, zmax;
        
        public void setMin(float x, float y, float z)
        {
            this.xmin = x; this.ymin = y; this.zmin = z;
            this.refreshGlobalArray();
        }
        
        public void setMax(float x, float y, float z)
        {
            this.xmax = x; this.ymax = y; this.zmax = z;
            this.refreshGlobalArray();
        }
        
        @Override
        public float[] getArray() {
            return new float[]{xmin, ymin, zmin, xmax, ymax, zmax};
        }

        @Override
        public int getSize() {
            return 6;
        }

        @Override
        public void initFromGlobalArray() {
            float[] globalArray = getGlobalArray();
            if(globalArray == null)
                return;
            int globalArrayIndex = getGlobalArrayIndex();
            
            xmin = globalArray[globalArrayIndex + 0];
            ymin = globalArray[globalArrayIndex + 1];
            zmin = globalArray[globalArrayIndex + 2];
            xmax = globalArray[globalArrayIndex + 3];
            ymax = globalArray[globalArrayIndex + 4];
            zmax = globalArray[globalArrayIndex + 5];
            
        }
        
        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("min ").append(xmin).append(" ").append(ymin).append(" ").append(zmin).append("\n");
            builder.append("max ").append(xmax).append(" ").append(ymax).append(" ").append(zmax).append("\n");
            return builder.toString();
        }
        
    }
            
}
