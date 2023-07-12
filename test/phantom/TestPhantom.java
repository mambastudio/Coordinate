/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phantom;

import coordinate.memory.NativeInteger;
import coordinate.utility.Sweeper;

/**
 *
 * @author user
 * 
 * https://codegym.cc/groups/posts/213-features-of-phantomreference
 * 
 */
public class TestPhantom {
    public static void main(String... args) throws InterruptedException
    {
        kubafu();
        Thread.sleep(10000);
        //Reference ref = new LargePhantomReference<>(new LargeObject(), new ReferenceQueue<>());
        
        Sweeper.getSweeper().register(new LargeObject(), ()->System.out.println("kubafu"));
        

        //System.out.println("ref = " + ref);

        Thread.sleep(5000);

        //System.out.println("Collecting garbage!");

        System.gc();
        Thread.sleep(300);

        //System.out.println("ref = " + ref);

        Thread.sleep(5000);

        //System.out.println("Collecting garbage!");

        System.gc();
       
    }
    
    public static void kubafu()
    {
        new NativeInteger(1);
    }
}
