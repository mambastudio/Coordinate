/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.type;

import coordinate.memory.type.LayoutMemory.PathElement.PathType;
import static coordinate.memory.type.LayoutMemory.PathElement.PathType.GROUP_ELEMENT;
import static coordinate.memory.type.LayoutMemory.PathElement.PathType.SEQUENCE_ELEMENT;
import static coordinate.memory.type.LayoutMemory.PathElement.PathType.SEQUENCE_ELEMENT_RANGE;
import java.util.UUID;

/**
 *
 * @author jmburu
 * 
 * https://courses.cs.washington.edu/courses/cse351/17sp/lectures/CSE351-L14-structs_17sp-ink-day2.pdf
 * https://github.com/openjdk/panama-foreign/blob/foreign-memaccess%2Babi/doc/panama_memaccess.md
 */
public abstract class LayoutMemory{
    protected long offset = 0; //byteoffset
    
    public abstract long byteSizeAggregate();
    
    public long byteSizeElement()
    {
        return byteSizeAggregate();
    }
    
    public boolean isArray()
    {
        return elementCount()>1;
    }
    
    public final long elementCount()
    {
        return byteSizeAggregate()/byteSizeElement();
    }
    
    public long offset()
    {
        return offset;
    }
    
    public abstract LayoutMemory withId(String name);
    public abstract String getId();
    
    public LayoutMemory select(PathElement... elements)
    {
        //elements[0] will check if 'this' path element is either a struct or array in switch statement below
        LayoutPath path = new LayoutPath(this);
        int offsetUltimate = 0; 
        //array loops for value state
        long arrayLength        = 1;
        long arrayElementSize   = 0;
        
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
                case SEQUENCE_ELEMENT_RANGE:   //this is like SEQUENCE_ELEMENT with a range, but adds more information for traversing range which starts at 0 index relative
                    arrayLength = ((LayoutArray)path.layout).elementCount();       //length of array 
                    path = path.arrayElement(0);
                    offsetUltimate += path.layout.offset; 
                    arrayElementSize = path.layout.byteSizeElement();
                    break;
                default:
                    throw new UnsupportedOperationException("not supported");
            }            
        }        
        if(path.layout instanceof LayoutValue)    
        {
            LayoutValue layoutValue = (LayoutValue)path.layout;
            return new AnonymousLayout(layoutValue.carrier(), offsetUltimate, path.layout.byteSizeElement(), arrayElementSize, arrayLength);
        }
        else
            return new AnonymousLayout(null, offsetUltimate, path.layout.byteSizeElement(), arrayElementSize, arrayLength);
    }
    
    public long offset(PathElement... elements)
    {
        LayoutMemory layout = select(elements);
        return layout.offset();
    }
    
    public ValueState valueState(PathElement... elements)
    {
        AnonymousLayout memory = (AnonymousLayout) select(elements);
        if(!(memory.hasCarrier()))
            throw new UnsupportedOperationException("path is not a value");
        return ValueState.valueState(memory.getCarrier(), memory.offset(), memory.arrayLength(), memory.arrayElementSize());
    }
      
    /**
     * Align is the size of data (e.g. 4-byte data -> int, float), and offset is starting point 
        or reference starting point.

        The returned value is the new starting point or offset of the align data.

        https://en.wikipedia.org/wiki/Data_structure_alignment

        padding not included here
     * 
     * @param offset
     * @param align
     * @return 
     */
    public final long computeAlignmentOffset(long offset, long align)
    {
        return (offset + align - 1) & -align;
    }  
    
    public static class PathElement
    {
        public enum PathType {
            GROUP_ELEMENT,
            SEQUENCE_ELEMENT,
            SEQUENCE_ELEMENT_RANGE
        }
        
        final PathType pathType;
        final String id;
        long index = -1; //to be used in the sequence element section of traversing the memory layout. Check select and valueState methods
        
        
        private PathElement(PathType type, String id)
        {
            this.pathType = type;
            this.id = id;
        }
        
        private PathElement(PathType type, long index)
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
        
        public long index()
        {
            return index;
        }
        
        public static PathElement groupElement(String name)
        {
            return new PathElement(GROUP_ELEMENT, name);
        }
        
        public static PathElement sequenceElement(long index)
        {
            return new PathElement(SEQUENCE_ELEMENT, index);
        }
        
        public static PathElement sequenceElement()
        {
            return new PathElement(SEQUENCE_ELEMENT_RANGE, 0);
        }
    }
    
    private static class AnonymousLayout extends LayoutMemory
    {
        private final String name;
        private final long byteSize;
        private final Class<?> carrier;
        
        //for array sequence traversal
        private final long  arrayElementSize;
        private final long  arrayLength;
                        
        public AnonymousLayout(Class<?> carrier, long offset, long byteSize, long arrayElementSize, long arrayLength)
        {
            this.offset = offset;
            this.byteSize = byteSize;
            this.name = UUID.randomUUID().toString();
            this.carrier = carrier;
            if(arrayLength < 1)
                throw new UnsupportedOperationException("array length is less than one: " +arrayLength);
            
            this.arrayElementSize = arrayElementSize;
            this.arrayLength = arrayLength;            
        }
        
        @Override
        public long byteSizeElement()
        {
            return byteSize;
        }
        
        @Override
        public long byteSizeAggregate() {
            if(arrayLength == 0)
                return byteSize;
            else
                return (int) (byteSizeElement() * arrayLength);
        }

        @Override
        public LayoutMemory withId(String name) {
            return new AnonymousLayout(carrier, offset, byteSize, arrayElementSize, arrayLength);
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
        
        public long arrayLength()
        {
            return arrayLength;
        }
        
        public long arrayElementSize()
        {
            return arrayElementSize;
        }   
    }
}
