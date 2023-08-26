/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package struct;

/**
 *
 * @author jmburu
 */
public abstract class LayoutValue implements LayoutMemory{
    
    public static OfByte JAVA_BYTE;
    public static OfInteger JAVA_INT;
    public static OfFloat JAVA_FLOAT;
    
    public static final class OfByte extends LayoutValue
    {
        @Override
        public int byteSize() { return 1; }        
    }
    
    public static final class OfInteger extends LayoutValue
    {
        @Override
        public int byteSize() { return 4; }        
    }
    
    public static class OfFloat extends LayoutValue
    {
        @Override
        public int byteSize() { return 4; }        
    }
}
