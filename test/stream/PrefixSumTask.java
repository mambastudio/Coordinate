/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import coordinate.memory.NativeInteger;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author user
 */
class PrefixSumTask extends RecursiveAction {
    private static final int SEQUENTIAL_THRESHOLD = 10000; // Adjust the threshold for parallelism

    private final NativeInteger nativeInteger;
    private final long start;
    private final long end;

    public PrefixSumTask(NativeInteger nativeInteger, long start, long end) {
        this.nativeInteger = nativeInteger;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end - start <= SEQUENTIAL_THRESHOLD) {
            for (long i = start + 1; i <= end; i++) {
                int prevValue = nativeInteger.get(i - 1);
                nativeInteger.set(i, prevValue + nativeInteger.get(i));
            }
        } else {
            long middle = (start + end) / 2;

            PrefixSumTask leftTask = new PrefixSumTask(nativeInteger, start, middle);
            PrefixSumTask rightTask = new PrefixSumTask(nativeInteger, middle + 1, end);

            invokeAll(leftTask, rightTask);

            nativeInteger.set(end, nativeInteger.get(start + middle) + nativeInteger.get(end));
        }
    }
}