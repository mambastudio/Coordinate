/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memlayout;

import coordinate.memory.layout.LayoutValue;
import coordinate.memory.layout.LayoutArray;
import coordinate.memory.layout.LayoutGroup;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
     
        LayoutGroup group = LayoutGroup.createGroup(
            LayoutValue.JAVA_LONG.withId("c"),
            LayoutArray.createArray(2, LayoutValue.JAVA_INT).withId("i"),
            LayoutValue.JAVA_BYTE.withId("v")
        );
        
        System.out.println(group);
        
        LayoutGroup group2 = LayoutGroup.createGroup(            
            LayoutValue.JAVA_INT.withId("i"),
            LayoutValue.JAVA_BYTE.withId("c"),
            LayoutValue.JAVA_BYTE.withId("d")
        );
        
        System.out.println(group2);
    }
}
