/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic.g2;

import coordinate.memory.type.LayoutGroup;
import coordinate.memory.type.LayoutMemory;
import coordinate.memory.type.LayoutMemory.PathElement;
import coordinate.memory.type.LayoutValue;
import coordinate.memory.type.MemoryRegion;
import coordinate.memory.type.StructBase;
import coordinate.memory.type.ValueState;

/**
 *
 * @author user
 * @param <S>
 */
public abstract class AbstractTriangleIndex<S extends AbstractCoordIntStruct<S>> 
        implements StructBase<AbstractTriangleIndex<S>> {
    
    public final S v_123;
    public final S u_123m;
    public final S n_123;
    
    private static final LayoutMemory sLayout = LayoutGroup.groupLayout(
        LayoutValue.JAVA_INT.withId("x"),
        LayoutValue.JAVA_INT.withId("y"),
        LayoutValue.JAVA_INT.withId("z"),
        LayoutValue.JAVA_INT.withId("w")); 
    
    private static final LayoutMemory layout = LayoutGroup.groupLayout(
        sLayout.withId("v_xyz"),
        sLayout.withId("u_xyzm"),
        sLayout.withId("n_xyz")
    );            
    
    
    private static final ValueState v_1State = layout.valueState(PathElement.groupElement("v_xyz"), PathElement.groupElement("x"));
    private static final ValueState v_2State = layout.valueState(PathElement.groupElement("v_xyz"), PathElement.groupElement("y"));
    private static final ValueState v_3State = layout.valueState(PathElement.groupElement("v_xyz"), PathElement.groupElement("z"));
     
    public static final ValueState u_1State = layout.valueState(PathElement.groupElement("u_xyzm"), PathElement.groupElement("x"));
    public static final ValueState u_2State = layout.valueState(PathElement.groupElement("u_xyzm"), PathElement.groupElement("y"));
    public static final ValueState u_3State = layout.valueState(PathElement.groupElement("u_xyzm"), PathElement.groupElement("z"));
    public static final ValueState u_mState = layout.valueState(PathElement.groupElement("u_xyzm"), PathElement.groupElement("w"));
     
    public static final ValueState n_1State = layout.valueState(PathElement.groupElement("n_xyz"), PathElement.groupElement("x"));
    public static final ValueState n_2State = layout.valueState(PathElement.groupElement("n_xyz"), PathElement.groupElement("y"));
    public static final ValueState n_3State = layout.valueState(PathElement.groupElement("n_xyz"), PathElement.groupElement("z"));
    
    
    public AbstractTriangleIndex(S v_xyz, S u_xyzm, S n_xyz)
    {
        this.v_123 = v_xyz;
        this.u_123m = u_xyzm;
        this.n_123 = n_xyz;
    }
        
    @Override
    public String toString()
    {
        return String.format("v: %7.0f, %7.0f, %7.0f, u: %7.0f, %7.0f, %7.0f, n: %7.0f, %7.0f, %7.0f, m: %7.0f", 
                    (float)v_123.get('x'),     (float)v_123.get('y'),     (float)v_123.get('z'),
                    (float)u_123m.get('x'),    (float)u_123m.get('y'),    (float)u_123m.get('z'),
                    (float)n_123.get('x'),     (float)n_123.get('y'),     (float)n_123.get('z'),
                    (float)(u_123m.get('w') & 0xFFFF));
    }
    
    @Override
    public void fieldToMemory(MemoryRegion memory) {
        v_1State.set(memory, v_123.get('x')); 
        v_2State.set(memory, v_123.get('y'));
        v_3State.set(memory, v_123.get('z'));
        
        u_1State.set(memory, u_123m.get('x')); 
        u_2State.set(memory, u_123m.get('y'));
        u_3State.set(memory, u_123m.get('z'));
        u_mState.set(memory, u_123m.get('w'));
        
        n_1State.set(memory, n_123.get('x')); 
        n_2State.set(memory, n_123.get('y'));
        n_3State.set(memory, n_123.get('z'));
    }

    @Override
    public void memoryToField(MemoryRegion memory) {
        v_123.set('x', (int) v_1State.get(memory));
        v_123.set('y',(int) v_2State.get(memory));
        v_123.set('z', (int) v_3State.get(memory));
        
        u_123m.set('x', (int) u_1State.get(memory));
        u_123m.set('y', (int) u_2State.get(memory));
        u_123m.set('z', (int) u_3State.get(memory));
        u_123m.set('w', (int) u_mState.get(memory));
        
        n_123.set('x', (int) n_1State.get(memory));
        n_123.set('y', (int) n_2State.get(memory));
        n_123.set('z', (int) n_3State.get(memory));
    }

    @Override
    public LayoutMemory getLayout()
    {
        return layout;
    }
}
