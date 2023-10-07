/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.type;

import coordinate.utility.RangeCheckArray;

/**
 *
 * @author user
 */
public class LayoutPath {
    LayoutMemory layout;
    
    protected LayoutPath(LayoutMemory layout)
    {
        this.layout = layout;
    }
    
    public LayoutPath groupElement(String name)
    {
        check(LayoutGroup.class, "bad sequence");
        LayoutGroup g = (LayoutGroup)layout;
        LayoutMemory m;
        if(!g.containsField(name))
            throw badLayoutPath("cannot find path for name: " +name);
        m = g.getField(name);
        return new LayoutPath(m);
    }
    
    public LayoutPath arrayElement(long index)
    {
        check(LayoutArray.class, "bad sequence");
        LayoutArray arr = (LayoutArray)layout;      
        RangeCheckArray.validateIndexSize(index, arr.elementCount());
        
        LayoutMemory m = arr.getLayoutMemory();        
        return new LayoutPath(m);
    }
        
    private void check(Class<?> layoutClass, String msg) {
        if (!layoutClass.isAssignableFrom(layout.getClass())) {
            throw new UnsupportedOperationException(msg);
        }
    }
    
    private static IllegalArgumentException badLayoutPath(String cause) {
        return new IllegalArgumentException("Bad layout path: " + cause);
    }
}
