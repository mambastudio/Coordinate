/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memlayout;

import coordinate.memory.type.LayoutValue;
import coordinate.memory.type.LayoutGroup;
import coordinate.memory.type.LayoutMemory.PathElement;
import coordinate.memory.type.MemoryStruct;
import coordinate.memory.type.MemoryStructFactory.Int32;
import coordinate.memory.type.StructBase;
import coordinate.memory.type.ValueState;
import coordinate.utility.RangeLong;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        test2();
    }
        
    public static void test1()
    {        
        MemoryStruct<Int32> intArray = new MemoryStruct(new Int32(), 100);        
        
        ValueState state = intArray.getState(
                PathElement.sequenceElement(), 
                PathElement.groupElement("value"));
        
        state.forEachSet(intArray.getMemory(), i -> (int)i);            
        state.set(intArray.getMemory(), 5, 1000000);
        
        System.out.println(intArray.getString(new RangeLong(0, 50)));
        
        MemoryStruct<Int32> subArray = intArray.offsetIndex(50);        
        subArray.set(5, new Int32(5));
        System.out.println(subArray);
        System.out.println(intArray);
    }
    
    public static void test2()
    {
        MemoryStruct<Josto> array = new MemoryStruct(new Josto(), 10);   
        ValueState state = array.getState(
                PathElement.sequenceElement(), 
                PathElement.groupElement("i"));
        state.forEachSet(array.getMemory(), i -> 22343); 
                
        System.out.println(array.get(5));        
        System.out.println(array.toString());
    }
    
    public static class Josto extends StructBase<Josto>
    {
        public byte c;
        public int i;
        public long v;
        
        public ValueState cState;
        public ValueState iState;
        public ValueState vState;
        
        public Josto(byte c, int i, long v) {this.c = c; this.i = i; this.v = v;}        
        public Josto(){}

        @Override
        public void fieldToMemory() {
            cState.set(memory(), c); 
            iState.set(memory(), i);
            vState.set(memory(), v);
        }

        @Override
        public void memoryToField() {
            c = (byte)  cState.get(memory());
            i = (int)   iState.get(memory());
            v = (long)  vState.get(memory());
        }

        @Override
        public Josto newInstance() {
            return new Josto();
        }

        @Override
        public Josto copy() {
            return new Josto(c, i, v);
        }

        @Override
        protected LayoutGroup initLayout() {
            LayoutGroup group = LayoutGroup.groupLayout(
                    LayoutValue.JAVA_BYTE.withId("c"),
                    LayoutValue.JAVA_INT.withId("i"),
                    LayoutValue.JAVA_LONG.withId("v")
            );            
            return group;
        }

        @Override
        protected void initValueStates() {
            cState = valueState(PathElement.groupElement("c"));
            iState = valueState(PathElement.groupElement("i"));
            vState = valueState(PathElement.groupElement("v"));
        }  
        
        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("(c = ").append(c);
            builder.append(" i = ").append(i);
            builder.append(" v = ").append(v).append(")");
            return builder.toString();
        }
    }    
}
