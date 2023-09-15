/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct.newstruct.base;

import coordinate.memory.layout.struct.ValueState;
import java.nio.ByteBuffer;

/**
 *
 * @author user
 */
public class TestAny {
    public static void main(String... args)
    {
        //new ConcreteClassA();
       // new ConcreteClassB();
        ByteBuffer buffer = ByteBuffer.allocate(10);
        ValueState x = ValueState.valueState(float.class, 0, 0,0, buffer);
        x.set(3f);
        
        float value = (float) x.get();
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
