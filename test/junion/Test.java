/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package junion;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import theleo.jstruct.Mem;
import theleo.jstruct.Struct;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
       Test test = new Test();
       test.test();
    }
    
    @Struct
    public class Vec3 {
        public float x,y,z;
    }
    
    public void test()
    {
        
        
        ByteBuffer a = ByteBuffer.allocateDirect(5*Mem.sizeOf(Vec3.class))
            .order(ByteOrder.nativeOrder());
        //Modify Direct Native Bytebuffer as it were a struct
        Vec3[] arr = Mem.wrap(a, Mem.sizeOf(Vec3.class));
        arr[5].x = 10;
    }
}
