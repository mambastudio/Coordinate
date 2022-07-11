/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct.newstruct.base;

import coordinate.struct.structbyte.StructArrayMemory;

/**
 *
 * @author user
 */
public class TestStruct1 {
    public static void main(String... args)
    {
        Profile profile = new Profile();
        
        System.out.println(profile.getLayout());
    }
    
    public static class Profile extends StructArrayMemory
    {       
        public float    f;
        public short    s;
        public int      v;  
        public long     l;        
        
        public Profile()
        {
            f = 0;
            v = 0;
            s = 0;
            l = 0;
        }
    }
    
}
