package cn.utokato.other;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author
 * @date 2019/2/15
 */
public class ForkJoinTest extends RecursiveTask<Integer> {

    private static final int THREAD_HOLD = 2;

    private int start;
    private int end;

    public ForkJoinTest(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;

        boolean canCompute = (end - start) <= THREAD_HOLD;

        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            int middle = (start + end) / 2;
            ForkJoinTest left = new ForkJoinTest(start, middle);
            ForkJoinTest right = new ForkJoinTest(middle + 1, end);

            left.fork();
            right.fork();

            Integer lResult = left.join();
            Integer rResult = right.join();

            sum = lResult + rResult;
        }

        return sum;
    }

    private void test(int s, int e) {
        int sum = 0;
        for (int i = s; i <= e; i++) {
            sum += i;
        }
        System.out.println("test result: " + sum);
    }

    public static void main(String[] args) {
        int start = 1;
        int end = 5;

        ForkJoinPool pool = new ForkJoinPool();

        ForkJoinTest task = new ForkJoinTest(start, end);

        Future<Integer> result = pool.submit(task);

        try {
            System.out.println(result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        task.test(start, end);
    }
}
