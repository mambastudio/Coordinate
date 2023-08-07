/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.memory.algorithms;

import coordinate.memory.NativeInteger;
import java.util.Spliterator;
import java.util.function.IntConsumer;

/**
 *
 * @author user
 */
public class NativeIntegerSpliterator implements Spliterator.OfInt {
    private NativeInteger array;
    private long index;
    private final long size;

    public NativeIntegerSpliterator(NativeInteger array) {
        this(array, 0, array.capacity());
    }

    private NativeIntegerSpliterator(NativeInteger array, long index, long size) {
        this.array = array;
        this.index = index;
        this.size = size;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (index < size) {
            action.accept(array.get(index++));
            return true;
        }
        return false;
    }

    @Override
    public Spliterator.OfInt trySplit() {
        long remaining = size - index;
        if (remaining <= 1) {
            return null;
        }
        long half = remaining / 2;
        NativeIntegerSpliterator newSpliterator = new NativeIntegerSpliterator(array, index, index + half);
        index += half;
        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        return size - index;
    }

    @Override
    public int characteristics() {
        return SIZED;
    }
}
