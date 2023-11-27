/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct.newstruct.base;

import coordinate.memory.type.MemoryStruct;
import coordinate.memory.type.MemoryStructFactory.Int32;
import coordinate.utility.Timer;

/**
 *
 * @author user
 */
public class TestStruct3 {
    public static void main(String... args)
    {
        test1();
    }
    
    
    public static void test1()
    {
        long size = 50_000_000L;
        
        MemoryStruct<Int32> mem = new MemoryStruct(new Int32(), size);
        System.out.println(Timer.timeThis(()->{
            for(long i = 0; i < mem.size(); i++)
                mem.set(i, new Int32(1));
        }));        
    }
    
}
