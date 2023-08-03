/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 *
 * @author user
 */
class PrefixSumSpliterator implements Spliterator<Integer> {
    private final Spliterator<Integer> spliterator;

    public PrefixSumSpliterator(Spliterator<Integer> spliterator) {
        
        this.spliterator = spliterator;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Integer> action) {
        return spliterator.tryAdvance(action);
    }

    @Override
    public Spliterator<Integer> trySplit() {
        Spliterator<Integer> split = spliterator.trySplit();
        return split == null ? null : new PrefixSumSpliterator(split);
    }

    @Override
    public long estimateSize() {
        return spliterator.estimateSize();
    }

    @Override
    public int characteristics() {
        return spliterator.characteristics();
    }

}
