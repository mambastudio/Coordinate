package coordinate.memory.type;

import coordinate.memory.type.LayoutMemory.PathElement;

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
    
    public static class Int32 implements StructBase<Int32>
    {
        private static final LayoutMemory layout = LayoutGroup.groupLayout(LayoutValue.JAVA_INT.withId("value"));
        private static final ValueState valueState = layout.valueState(PathElement.groupElement("value"));
        
        private int value; 
                
        public Int32(int value) {this.value = value;}   
        public Int32(long value) {this.value = Math.toIntExact(value);}
        public Int32(){}
        
        public int value(){return value;}        
        
        @Override
        public Int32 newStruct() {return new Int32();}
        @Override
        public Int32 copyStruct() {return new Int32(value);}        
        @Override
        public String toString(){return ""+value;}
        @Override
        public void fieldToMemory(MemoryRegion memory) {valueState.set(memory, value);}
        @Override
        public void memoryToField(MemoryRegion memory) {value = (int) valueState.get(memory);}        
        @Override
        public LayoutMemory getLayout() {return layout;}
    }    
    
    public final static class Float32 implements StructBase<Float32>
    {
        private static final LayoutMemory layout = LayoutGroup.groupLayout(LayoutValue.JAVA_FLOAT.withId("value"));
        private static final ValueState valueState = layout.valueState(PathElement.groupElement("value"));
        
        private float value;   
                
        public Float32(float value) {this.value = value;}        
        public Float32(){}
        
        public float value(){return value;}        
        
        @Override
        public Float32 newStruct() {return new Float32();}
        @Override
        public Float32 copyStruct() {return new Float32(value);}        
        @Override
        public String toString(){return ""+value;}
        @Override
        public void fieldToMemory(MemoryRegion memory) {valueState.set(memory, value);}
        @Override
        public void memoryToField(MemoryRegion memory) {value = (float) valueState.get(memory);}        
        @Override
        public LayoutMemory getLayout() {return layout;}
    }   
    
    public final static class Long64 implements StructBase<Long64>
    {
        private static final LayoutMemory layout = LayoutGroup.groupLayout(LayoutValue.JAVA_LONG.withId("value"));
        private static final ValueState valueState = layout.valueState(PathElement.groupElement("value"));
        
        private long value;      
                
        public Long64(long value) {this.value = value;}        
        public Long64(){}
        
        public long value(){return value;}        
        
        @Override
        public Long64 newStruct() {return new Long64();}
        @Override
        public Long64 copyStruct() {return new Long64(value);}        
        @Override
        public String toString(){return ""+value;}
        @Override
        public void fieldToMemory(MemoryRegion memory) {valueState.set(memory, value);}
        @Override
        public void memoryToField(MemoryRegion memory) {value = (long)valueState.get(memory);}        
        @Override
        public LayoutMemory getLayout() {return layout;}
    }   
    
    public final static class Double64 implements StructBase<Double64>
    {
        private static final LayoutMemory layout = LayoutGroup.groupLayout(LayoutValue.JAVA_DOUBLE.withId("value"));
        private static final ValueState valueState = layout.valueState(PathElement.groupElement("value"));
        
        private double value;    
        
        public Double64(double value) {this.value = value;}        
        public Double64(){}
        
        public double value(){return value;}        
        
        @Override
        public Double64 newStruct() {return new Double64();}
        @Override
        public Double64 copyStruct() {return new Double64(value);}        
        @Override
        public String toString(){return ""+value;}
        @Override
        public void fieldToMemory(MemoryRegion memory) {valueState.set(memory, value);}
        @Override
        public void memoryToField(MemoryRegion memory) {value = (double) valueState.get(memory);}        
        @Override
        public LayoutMemory getLayout() {return layout;}
    }   
}
