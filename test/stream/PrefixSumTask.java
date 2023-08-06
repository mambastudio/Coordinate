/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 *
 * @author jmburu
 */
public class PrefixSumTask extends RecursiveAction {
    private static final int THRESHOLD = 4; // Threshold for sequential processing
    private int[] array;
    private int[] prefixSum;
    private int low;
    private int high;

    public PrefixSumTask(int[] array, int[] prefixSum, int low, int high) {
        this.array = array;
        this.prefixSum = prefixSum;
        this.low = low;
        this.high = high;
    }

    @Override
    protected void compute() {
        if (high - low <= THRESHOLD) {
            for (int i = low + 1; i < high; i++) {
                prefixSum[i] = array[i - 1] + ((i == low + 1) ? 0 : prefixSum[i - 1]);
            }
        } else {
            int mid = (low + high) / 2;
            PrefixSumTask leftTask = new PrefixSumTask(array, prefixSum, low, mid);
            PrefixSumTask rightTask = new PrefixSumTask(array, prefixSum, mid, high);

            invokeAll(leftTask, rightTask);

            if (mid > 0) {
                prefixSum[mid] += prefixSum[mid - 1];
            }
        }
    }
}
