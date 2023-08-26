/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memlayout;

/**
 *
 * @author jmburu
 */
public abstract class LayoutValue implements LayoutMemory{
    
    public static final OfByte JAVA_BYTE = new OfByte("JAVA_BYTE");
    public static final OfInteger JAVA_INT = new OfInteger("JAVA_INT");
    public static final OfFloat JAVA_FLOAT = new OfFloat("JAVA_FLOAT");
    
    public static final class OfByte extends LayoutValue
    {
        private final String name;
        
        private OfByte(String name)
        {
            this.name = name;
        }
        
        @Override
        public OfByte withId(String name){
            OfByte ofByte = new OfByte(name);
            return ofByte;
        }
        
        @Override
        public String getId(){return name;}
        
        @Override
        public int byteSize() { return 1; }  
        
    }
    
    public static final class OfInteger extends LayoutValue
    {
        private final String name;
        
        private OfInteger(String name)
        {
            this.name = name;
        }
        
        @Override
        public OfInteger withId(String name){
            OfInteger ofInteger = new OfInteger(name);
            return ofInteger;
        }
        
        @Override
        public String getId(){return name;}
        
        @Override
        public int byteSize() { return 4; }        
    }
    
    public static class OfFloat extends LayoutValue
    {
        private final String name;
        
        private OfFloat(String name)
        {
            this.name = name;
        }
        
        @Override
        public OfFloat withId(String name){
            OfFloat ofByte = new OfFloat(name);
            return ofByte;
        }
        
        @Override
        public String getId(){return name;}
        
        @Override
        public int byteSize() { return 4; }        
    }
}
