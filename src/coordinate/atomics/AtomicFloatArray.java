/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.atomics;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 *
 * @author user
 */
public final class AtomicFloatArray {
    private final AtomicIntegerArray array;
    
    public AtomicFloatArray(int length)
    {
        array = new AtomicIntegerArray(length);
    }
    
    public float incrementAndGet(int index)
    {
        return addAndGet(index, 1);
    }
    
    public float decrementAndGet(int index)
    {
        return addAndGet(index, -1);
    }
    
    public float get(int index)
    {
        int icurrent = array.get(index);
        float current = f(icurrent);
        return current;
    }
    
    public float getAndAccumulate(int index, float value)
    {
        int icurrent = array.get(index);
        float current = f(icurrent);
        float next = current + value;
        int inext = i(next);
        array.set(index, inext);
        return current;
    }
    
    public float addAndGet(int index, float delta)
    {
        int icurrent = array.get(index);
        float current = f(icurrent);
        float next = current + delta;
        int inext = i(next);
        array.set(index, inext);
        return next;
    }
    
    public void set(int index, float value)
    {
        int inext = i(value);
        array.set(index, inext);
    }
    
    public void setAll(float value)
    {
        for(int i = 0; i<length(); i++)
            set(i, value);
    }
    
    public int length()
    {
        return array.length();
    }
    
    private static int i(final float f) {
        return Float.floatToIntBits(f);
    }

    private static float f(final int i) {
        return Float.intBitsToFloat(i);
    }
    
    
}
