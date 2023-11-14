/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.type;

/**
 *
 * @author jmburu
 */
public abstract class LayoutValue extends LayoutMemory{
    
    private final Class<?> carrier;
    
    public static final OfBoolean   JAVA_BOOLEAN    = new OfBoolean("JAVA_BOOLEAN");
    public static final OfByte      JAVA_BYTE       = new OfByte("JAVA_BYTE");
    public static final OfChar      JAVA_CHAR       = new OfChar("JAVA_CHAR");
    public static final OfShort     JAVA_SHORT      = new OfShort("JAVA_SHORT");
    public static final OfInteger   JAVA_INT        = new OfInteger("JAVA_INT");
    public static final OfFloat     JAVA_FLOAT      = new OfFloat("JAVA_FLOAT");
    public static final OfLong      JAVA_LONG       = new OfLong("JAVA_LONG");
    public static final OfDouble    JAVA_DOUBLE     = new OfDouble("JAVA_DOUBLE");
    
    private LayoutValue(Class<?> carrier)
    {
        if(!isValidCarrier(carrier))
            throw new UnsupportedOperationException("invalid primitive carrier");
        this.carrier = carrier;
    }
    
    public final Class<?> carrier() {
        return carrier;
    }
    
    public static boolean isValidCarrier(Class<?> carrier) {
        // void.class is not valid
        return carrier == boolean.class
                || carrier == byte.class
                || carrier == short.class
                || carrier == char.class
                || carrier == int.class
                || carrier == long.class
                || carrier == float.class
                || carrier == double.class;
    }
    
    public static final class OfBoolean extends LayoutValue
    {
        private final String name;
        
        private OfBoolean(String name)
        {
            super(byte.class);
            this.name = name;
        }
        
        @Override
        public OfBoolean withId(String name){
            OfBoolean ofBoolean = new OfBoolean(name);
            return ofBoolean;
        }
        
        @Override
        public String getId(){return name;}
        
        @Override
        public long byteSizeAggregate() { return 1; }  
        
    }
    
    public static final class OfByte extends LayoutValue
    {
        private final String name;
        
        private OfByte(String name)
        {
            super(byte.class);
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
        public long byteSizeAggregate() { return 1; }  
        
    }
    
    public static final class OfChar extends LayoutValue
    {
        private final String name;
        
        private OfChar(String name)
        {
            super(char.class);
            this.name = name;
        }
        
        @Override
        public OfChar withId(String name){
            OfChar ofChar = new OfChar(name);
            return ofChar;
        }
        
        @Override
        public String getId(){return name;}
        
        @Override
        public long byteSizeAggregate() { return 2; }  
        
    }
    
    public static final class OfShort extends LayoutValue
    {
        private final String name;
        
        private OfShort(String name)
        {
            super(short.class);
            this.name = name;
        }
        
        @Override
        public OfShort withId(String name){
            OfShort ofShort = new OfShort(name);
            return ofShort;
        }
        
        @Override
        public String getId(){return name;}
        
        @Override
        public long byteSizeAggregate() { return 2; }  
        
    }
    
    public static final class OfInteger extends LayoutValue
    {
        private final String name;
        
        private OfInteger(String name)
        {
            super(int.class);
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
        public long byteSizeAggregate() { return 4; }        
    }
    
    public static class OfFloat extends LayoutValue
    {
        private final String name;
        
        private OfFloat(String name)
        {
            super(float.class);
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
        public long byteSizeAggregate() { return 4; }        
    }
    
    public static class OfLong extends LayoutValue
    {
        private final String name;
        
        private OfLong(String name)
        {
            super(long.class);
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
        public long byteSizeAggregate() { return 8; }        
    }
    
    public static class OfDouble extends LayoutValue
    {
        private final String name;
        
        private OfDouble(String name)
        {
            super(double.class);
            this.name = name;
        }
        
        @Override
        public OfDouble withId(String name){
            OfDouble ofDouble = new OfDouble(name);
            return ofDouble;
        }
        
        @Override
        public String getId(){return name;}
        
        @Override
        public long byteSizeAggregate() { return 8; }        
    }
}
