/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memlayout;

import coordinate.memory.layout.LayoutValue;
import coordinate.memory.layout.LayoutArray;
import coordinate.memory.layout.LayoutGroup;
import coordinate.memory.layout.LayoutMemory;
import coordinate.memory.layout.LayoutMemory.PathElement;
import coordinate.memory.layout.struct.ValueState;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        LayoutMemory group = LayoutGroup.groupLayout(
                LayoutValue.JAVA_BYTE.withId("c"),
                LayoutArray.arrayLayout(2, LayoutValue.JAVA_INT).withId("i"),
                LayoutValue.JAVA_LONG.withId("v")
        );
        
        System.out.println(group);
        
        LayoutMemory sequence = LayoutArray.arrayLayout(5, group);
        
        LayoutMemory mem = sequence.select(
                PathElement.sequenceElement(4),
                PathElement.groupElement("v"));
        System.out.println(mem.offset());
        
        
    }
}
