/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct.newstruct.base;

import coordinate.memory.type.MemoryAllocator;
import coordinate.memory.type.MemoryRegion;
import coordinate.memory.type.ValueState;

/**
 *
 * @author user
 */
public class TestAny {
    public static void main(String... args)
    {
        MemoryRegion carrier = MemoryAllocator.allocateHeap(10, 4);
        ValueState x = ValueState.valueState(float.class, 0, 10, 4);
        x.set(carrier, 3, 13f);
        
        float value = (float) x.get(carrier, 3);
        System.out.println(value);
    }
    
    public static abstract class AbstractClass
    {
        public AbstractClass() {
            System.out.println("Initializing AbstractClass");
        }
        
        private AbstractClass(int i)
        {
            System.out.println("Parametrized Counter constructor");
        }
    }
    
    
    public static class ConcreteClassC extends AbstractClass {
        public ConcreteClassC(int i)
        {
            super(5);
        }
    }
}
