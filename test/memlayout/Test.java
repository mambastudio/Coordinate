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
import coordinate.memory.layout.struct.StructBase;
import coordinate.memory.layout.struct.ValueState;
import java.nio.ByteBuffer;

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
        ByteBuffer buffer = ByteBuffer.allocate(sequence.byteSizeAggregate());
        
        ValueState i2 = sequence.valueState(buffer, 
                    PathElement.sequenceElement(),
                    PathElement.groupElement("i"),
                    PathElement.sequenceElement(0)
                );
        for(int i = 0; i<i2.length(); i++)
            i2.set(i, 5);
        
        for(int i = 0; i<i2.length(); i++)
            System.out.println(i2.get(i));
        
        LayoutMemory pointLayout = LayoutGroup.groupLayout(
                LayoutValue.JAVA_INT.withId("x"),
                LayoutValue.JAVA_INT.withId("y"),
                LayoutValue.JAVA_INT.withId("z")
        );
        
        LayoutMemory cellLayout = LayoutGroup.groupLayout(
                pointLayout.withId("min"),
                LayoutValue.JAVA_INT.withId("begin"),
                pointLayout.withId("max"),
                LayoutValue.JAVA_INT.withId("end")
        );
        
        //trying to prove that point(x, y, z) of int values added int values in cellLayout is similar to point(x, y, z, w) series layout
        System.out.println(cellLayout);
        
        /*
        LayoutMemory sequence = LayoutArray.arrayLayout(5, group);
        
        LayoutMemory mem = sequence.select(
                PathElement.sequenceElement(4),
                PathElement.groupElement("v"));
        System.out.println(mem.offset());
        
        Josto josto = new Josto();
        josto.i = 3000;
        josto.v = 400L;
        josto.toBuffer();
        System.out.println(josto.iState.get());
        System.out.println(josto.vState.get());
        */
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
        public void toBuffer() {
            cState.set(c);
            iState.set(i);
            vState.set(v);
        }

        @Override
        public void fromBuffer() {
            c = (byte) cState.get();
            i = (int) iState.get();
            v = (long) vState.get();
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
        
    }
}
