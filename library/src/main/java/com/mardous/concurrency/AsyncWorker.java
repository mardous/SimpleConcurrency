package com.mardous.concurrency;

import com.mardous.concurrency.task.FileResultTaskBuilder;
import com.mardous.concurrency.task.IntegerResultTaskBuilder;
import com.mardous.concurrency.task.LongResultTaskBuilder;
import com.mardous.concurrency.task.ResultTaskBuilder;
import com.mardous.concurrency.task.SimpleTaskBuilder;
import com.mardous.concurrency.task.StringResultTaskBuilder;
import com.mardous.concurrency.task.TaskConnection;

import java.io.File;

/**
 * @author Chris Alvarado (mardous)
 */
public final class AsyncWorker {

    private AsyncWorker() {
    }

    /**
     * Executes a simple task represented by the given {@link Runnable}.
     * <p>If you want to listen to events occurred during the execution
     * of the task, you must use {@link #simpleTask(AsyncRunnable)}
     * instead, which gives you access to the {@link TaskConnection#onPreExecute()},
     * {@link TaskConnection#onCancelled()} and {@link TaskConnection#onError(Exception)}
     * callbacks.
     *
     * @param runnable The {@link Runnable} to execute.
     * @return A new instance of {@link SimpleTaskBuilder} which you
     * can use later to build your task.
     */
    public static SimpleTaskBuilder simpleTask(Runnable runnable) {
        return new SimpleTaskBuilder(runnable);
    }

    /**
     * Executes a simple task represented by the given {@link AsyncRunnable}.
     * Unlike the {@link #simpleTask(Runnable)} method, which uses a simple
     * {@link Runnable}, this uses a {@link AsyncRunnable}, giving you some
     * extra callbacks to listen to events occurred during the execution of
     * the task.
     *
     * @param runnable The {@link AsyncRunnable} to execute.
     * @return A new instance of {@link SimpleTaskBuilder} which you
     * can use later to build your task.
     */
    public static SimpleTaskBuilder simpleTask(AsyncRunnable runnable) {
        return new SimpleTaskBuilder(runnable);
    }

    public static <T> ResultTaskBuilder<T> forResult(AsyncCallable<T> callable) {
        return new ResultTaskBuilder<>(callable);
    }

    public static FileResultTaskBuilder forFile(AsyncCallable<File> callable) {
        return new FileResultTaskBuilder(callable);
    }

    public static StringResultTaskBuilder forString(AsyncCallable<String> callable) {
        return new StringResultTaskBuilder(callable);
    }

    public static IntegerResultTaskBuilder forInteger(AsyncCallable<Integer> callable) {
        return new IntegerResultTaskBuilder(callable);
    }

    public static LongResultTaskBuilder forLong(AsyncCallable<Long> callable) {
        return new LongResultTaskBuilder(callable);
    }
}
