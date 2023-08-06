/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import coordinate.memory.NativeInteger;
import java.util.concurrent.RecursiveAction;
import static stream.BlellochScanReduceTask.SEQUENTIAL_THRESHOLD;

/**
 *
 * @author jmburu
 */
public class BlellochScanDownSweepTask extends RecursiveAction {
    private final NativeInteger input;
    private final long start;
    private final long end;
    private int carry;

    public BlellochScanDownSweepTask(NativeInteger input, long start, long end, int carry) {
        this.input = input;
        this.start = start;
        this.end = end;
        this.carry = carry;
    }

    @Override
    protected void compute() {
        if (end - start <= SEQUENTIAL_THRESHOLD) {
            // If array size is below or equal to threshold, perform sequential downsweep
            sequentialDownSweep(input, start, end, carry);
        } else {
            if (end - start == SEQUENTIAL_THRESHOLD) {
                // Base case for downsweep
                long temp = input.get(start);
                input.set(start, carry);
                carry += temp;
                return;
            }

            long mid = (start + end) / 2;

            BlellochScanDownSweepTask leftTask = new BlellochScanDownSweepTask(input, start, mid, carry);
            BlellochScanDownSweepTask rightTask = new BlellochScanDownSweepTask(input, mid, end, carry + input.get(mid - 1));

            leftTask.fork();
            rightTask.compute();
            leftTask.join();
        }
    }

    public static void sequentialDownSweep(NativeInteger input, long start, long end, int carry) {
        for (long i = start; i < end; i++) {
            long temp = input.get(i);
            input.set(i, carry);
            carry += temp;
        }
    }
}

