/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.struct.newstruct.base;

import coordinate.memory.layout.LayoutArray;
import coordinate.memory.layout.LayoutGroup;
import coordinate.memory.layout.LayoutValue;
import coordinate.memory.layout.struct.StructBase;

/**
 *
 * @author user
 */
public class TestStruct3 {
    public static void main(String... args)
    {
        
    }
    
    public static class S1 extends StructBase<S1>
    {
        public char c;
        public int[] i = new int[2];
        public long v;
        
        
        @Override
        public S1 newInstance() {
            return new S1();
        }

        @Override
        public S1 copy() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected LayoutGroup initLayout() {
            return LayoutGroup.groupLayout(
                    LayoutValue.JAVA_BYTE.withId("c"),
                    LayoutArray.arrayLayout(2, LayoutValue.JAVA_INT).withId("i"),
                    LayoutValue.JAVA_LONG.withId("v")
            );
        }

        @Override
        public void toBuffer() {
            
        }

        @Override
        public void fromBuffer() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
}
