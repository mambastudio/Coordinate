/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import static basic.Test.Josto.JOE;
import static basic.Test.Josto.MWANGI;
import coordinate.unsafe.UnsafeUtils;
import coordinate.utility.Value2Di;
import java.nio.ByteBuffer;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author user
 */
public class Test {
    enum Josto{JOE, MWANGI}
    public static void main(String... args)
    {
        ByteBuffer buf = UnsafeUtils.allocateLightDirectBuffer(10000000);
        buf = UnsafeUtils.allocateLightDirectBuffer(34);
    }
    
    
}
