/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct.newstruct.base;

import coordinate.struct.StructAbstractCache;
import coordinate.struct.StructUtils;
import coordinate.struct.cache.StructArrayCache;
import coordinate.struct.cache.StructBufferCache;
import coordinate.struct.cache.StructDirectCache;
import coordinate.struct.cache.StructUnsafeCache;
import coordinate.struct.structbyte.StructArrayMemory;
import coordinate.struct.structbyte.StructBufferMemory;
import coordinate.struct.structbyte.StructUnsafeMemory;
import coordinate.unsafe.UnsafeByteBuffer;

/**
 *
 * @author user
 */
public class TestStruct2 {
    public static void main(String... args)
    {
        StructAbstractCache<Profile, UnsafeByteBuffer> cache = StructUtils.createStructCache(Profile.class, StructUnsafeCache.class,  2);
        Profile profile = new Profile();       
        profile.setV(3);
        profile.setF(34.343f);
        profile.setS((short)4);
        profile.setL(Long.MAX_VALUE);
        cache.setStruct(profile, 1);
        
        Profile p = cache.get(1);
        System.out.println(p.s);
        System.out.println(p.f);
        System.out.println(p.l);
        
    }
    
    public static class Profile extends StructUnsafeMemory
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
        
        public void setV(int v)
        {
            this.v = v;
            this.refreshGlobalBuffer();
        }
        
        public void setF(float f)
        {
            this.f = f;
            this.refreshGlobalBuffer();
        }
        
        public void setL(long l)
        {
            this.l = l;
            this.refreshGlobalBuffer();
        }
        
        public void setS(short s)
        {
            this.s = s;
            this.refreshGlobalBuffer();
        }
    }
    
    
}
