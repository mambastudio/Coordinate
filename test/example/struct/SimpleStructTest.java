/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct;

/**
 *
 * @author user
 */
public class SimpleStructTest {
    public static void main(String... args)
    {
        
        /*
        StructFloatArray<Box> arrayBox = new StructFloatArray<>(Box.class, 6);
        
        for(Box box: arrayBox)
        {
            box.setMin(2, 4, 4);
            box.setMax(100, 0.7f, (int) (1 * Math.random() * 10));
        }
        
        for(Box box: arrayBox)
            System.out.println(box);        
    }
    
    public static class Box extends FloatStruct
    {
        public float xmin, ymin, zmin;
        public float xmax, ymax, zmax;
        
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
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("min ").append(xmin).append(" ").append(ymin).append(" ").append(zmin).append("\n");
            builder.append("max ").append(xmax).append(" ").append(ymax).append(" ").append(zmax).append("\n");
            return builder.toString();
        }
*/
        System.out.println(Float.floatToIntBits(0.002f));
    }
            
}
