/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.layout;

import static coordinate.memory.layout.LayoutMemory.PathElement.PathType.GROUP_ELEMENT;
import static coordinate.memory.layout.LayoutMemory.PathElement.PathType.SEQUENCE_ELEMENT;
import coordinate.memory.layout.struct.ValueState;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 *
 * @author jmburu
 * 
 * https://courses.cs.washington.edu/courses/cse351/17sp/lectures/CSE351-L14-structs_17sp-ink-day2.pdf
 * https://github.com/openjdk/panama-foreign/blob/foreign-memaccess%2Babi/doc/panama_memaccess.md
 */
public abstract class LayoutMemory{
    protected int offset = 0;
    
    public abstract int byteSizeAggregate();
    
    protected int byteSizeElement()
    {
        return byteSizeAggregate();
    }
    
    public int offset()
    {
        return offset;
    }
    
    public abstract LayoutMemory withId(String name);
    public abstract String getId();
    
    public LayoutMemory select(PathElement... elements)
    {
        LayoutPath path = new LayoutPath(this);
        int offsetUltimate = 0;
        for(PathElement element: elements)
        {            
            switch (element.pathType) {
                case GROUP_ELEMENT:
                    path = path.groupElement(element.id());
                    offsetUltimate += path.layout.offset;
                    break;            
                case SEQUENCE_ELEMENT:
                    path = path.arrayElement(element.index());
                    offsetUltimate += path.layout.offset + element.index() * path.layout.byteSizeElement();
                    break;
                default:
                    throw new UnsupportedOperationException("not supported");
            }            
        }
        if(path.layout instanceof LayoutValue)
        {
            LayoutValue layoutValue = (LayoutValue)path.layout;
            return new AnonymousLayout(layoutValue.carrier(), offsetUltimate, path.layout.byteSizeElement());
        }
        return new AnonymousLayout(offsetUltimate, path.layout.byteSizeElement());
    }
    
    
    public ValueState valueState(ByteBuffer buffer, PathElement... elements)
    {
        AnonymousLayout memory = (AnonymousLayout) select(elements);
        if(!(memory.hasCarrier()))
            throw new UnsupportedOperationException("path is not a value");
        return ValueState.valueState(memory.getCarrier(), memory.offset, buffer);
    }
    
    
    /**
     * Align is the size of data (e.g. 4-byte data -> int, float), and offset is starting point 
     * or reference starting point.
     * 
     * The returned value is the new starting point or offset of the align data.
     * 
     * https://en.wikipedia.org/wiki/Data_structure_alignment
     * 
     * @param offset
     * @param align
     * @return 
     */
    public final int computeAlignmentOffset(int offset, int align)
    {
        return (offset + align - 1) & -align;
    }  
    
    public static class PathElement
    {
        public enum PathType {
            GROUP_ELEMENT,
            SEQUENCE_ELEMENT
        }
        
        final PathType pathType;
        final String id;
        int index = 0;
        
        
        private PathElement(PathType type, String id)
        {
            this.pathType = type;
            this.id = id;
        }
        
        private PathElement(PathType type, int index)
        {
            this.pathType = type;
            this.id = UUID.randomUUID().toString();
            this.index = index;
        }
        
        public PathType type()
        {
            return pathType;
        }
        
        public String id()
        {
            return id;
        }
        
        public int index()
        {
            return index;
        }
        
        public static PathElement groupElement(String name)
        {
            return new PathElement(GROUP_ELEMENT, name);
        }
        
        public static PathElement sequenceElement(int index)
        {
            return new PathElement(SEQUENCE_ELEMENT, index);
        }
        
        public static PathElement sequenceElement()
        {
            return sequenceElement(0);
        }
    }
    
    private static class AnonymousLayout extends LayoutMemory
    {
        private final String name;
        private final int byteSize;
        private final Class<?> carrier;
        
        public AnonymousLayout(int offset, int byteSize)
        {
            this.offset = offset;
            this.byteSize = byteSize;
            this.name = UUID.randomUUID().toString();
            this.carrier = null;
        }
        
        public AnonymousLayout(Class<?> carrier, int offset, int byteSize)
        {
            this.offset = offset;
            this.byteSize = byteSize;
            this.name = UUID.randomUUID().toString();
            this.carrier = carrier;
        }
        
        @Override
        public int byteSizeAggregate() {
            return byteSize;
        }

        @Override
        public LayoutMemory withId(String name) {
            return new AnonymousLayout(offset, byteSize);
        }

        @Override
        public String getId() {
            return name;
        }
        
        public Class<?> getCarrier()
        {
            return carrier;
        }
        
        public boolean hasCarrier()
        {
            return carrier != null;
        }
    }
}
