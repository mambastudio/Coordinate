package coordinate.memory.type;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class MemoryStructFactory {
    
    public final static class Int32 extends StructBase<Int32>
    {
        private int value;        
        private ValueState valueState;
                
        public Int32(int value) {this.value = value;}        
        public Int32(){}
        
        public int value(){return value;}
        @Override
        public void fieldToMemory() {valueState.set(memory(), value);}
        @Override
        public void memoryToField() {value = (int)  valueState.get(memory());}
        @Override
        public Int32 newInstance() {return new Int32();}
        @Override
        public Int32 copy() {return new Int32(value);}
        @Override
        protected LayoutGroup initLayout() {return LayoutGroup.groupLayout(LayoutValue.JAVA_INT.withId("value"));}
        @Override
        protected void initValueStates() {valueState = valueState(LayoutMemory.PathElement.groupElement("value"));}
        @Override
        public String toString(){return ""+value;}
    }    
    
    public final static class Float32 extends StructBase<Float32>
    {
        private float value;        
        private ValueState valueState;
                
        public Float32(float value) {this.value = value;}        
        public Float32(){}
        
        public float value(){return value;}
        @Override
        public void fieldToMemory() {valueState.set(memory(), value);}
        @Override
        public void memoryToField() {value = (float)  valueState.get(memory());}
        @Override
        public Float32 newInstance() {return new Float32();}
        @Override
        public Float32 copy() {return new Float32(value);}
        @Override
        protected LayoutGroup initLayout() {return LayoutGroup.groupLayout(LayoutValue.JAVA_FLOAT.withId("value"));}
        @Override
        protected void initValueStates() {valueState = valueState(LayoutMemory.PathElement.groupElement("value"));}          
        @Override
        public String toString(){return ""+value;}
    }   
    
    public final static class Long64 extends StructBase<Long64>
    {
        private long value;        
        private ValueState valueState;
                
        public Long64(long value) {this.value = value;}        
        public Long64(){}
        
        public long value(){return value;}
        @Override
        public void fieldToMemory() {valueState.set(memory(), value);}
        @Override
        public void memoryToField() {value = (int)  valueState.get(memory());}
        @Override
        public Long64 newInstance() {return new Long64();}
        @Override
        public Long64 copy() {return new Long64(value);}
        @Override
        protected LayoutGroup initLayout() {return LayoutGroup.groupLayout(LayoutValue.JAVA_LONG.withId("value"));}
        @Override
        protected void initValueStates() {valueState = valueState(LayoutMemory.PathElement.groupElement("value"));}          
        @Override
        public String toString(){return ""+value;}
    }   
    
    public final static class Double64 extends StructBase<Double64>
    {
        private double value;        
        private ValueState valueState;
                
        public Double64(double value) {this.value = value;}        
        public Double64(){}
        
        public double value(){return value;}
        @Override
        public void fieldToMemory() {valueState.set(memory(), value);}
        @Override
        public void memoryToField() {value = (double)  valueState.get(memory());}
        @Override
        public Double64 newInstance() {return new Double64();}
        @Override
        public Double64 copy() {return new Double64(value);}
        @Override
        protected LayoutGroup initLayout() {return LayoutGroup.groupLayout(LayoutValue.JAVA_DOUBLE.withId("value"));}
        @Override
        protected void initValueStates() {valueState = valueState(LayoutMemory.PathElement.groupElement("value"));}          
        @Override
        public String toString(){return ""+value;}
    }   
}
