/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.layout;

import java.util.UUID;

/**
 *
 * @author jmburu
 */
public abstract class LayoutValue extends LayoutMemory{
    
    public static final OfByte      JAVA_BYTE   = new OfByte("JAVA_BYTE");
    public static final OfInteger   JAVA_INT    = new OfInteger("JAVA_INT");
    public static final OfFloat     JAVA_FLOAT  = new OfFloat("JAVA_FLOAT");
    public static final OfLong      JAVA_LONG   = new OfLong("JAVA_LONG");
    
    public static LayoutValue createValue(int byteSize)
    {
        return new DefinedLayoutValue(byteSize);
    }
    
    private static final class DefinedLayoutValue extends LayoutValue
    {
        private final String name;
        private final int byteSize;
        
        private DefinedLayoutValue(int byteSize)
        {
            this.name = UUID.randomUUID().toString();
            this.byteSize = byteSize;
        }
        
        private DefinedLayoutValue(String name, int byteSize)
        {
            this.name = name;
            this.byteSize = byteSize;
        }
        
        @Override
        public int byteSize() {
            return byteSize;
        }

        @Override
        public LayoutMemory withId(String name) {
            return new DefinedLayoutValue(name, byteSize);
        }

        @Override
        public String getId() {
            return name;
        }
        
    }
    
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
            OfFloat ofFloat = new OfFloat(name);
            return ofFloat;
        }
        
        @Override
        public String getId(){return name;}
        
        @Override
        public int byteSize() { return 4; }        
    }
    
    public static class OfLong extends LayoutValue
    {
        private final String name;
        
        private OfLong(String name)
        {
            this.name = name;
        }
        
        @Override
        public OfLong withId(String name){
            OfLong ofLong = new OfLong(name);
            return ofLong;
        }
        
        @Override
        public String getId(){return name;}
        
        @Override
        public int byteSize() { return 8; }        
    }
}
