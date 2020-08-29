package com.mardous.concurrency;

import com.mardous.concurrency.task.FileResultTaskBuilder;
import com.mardous.concurrency.task.IntegerResultTaskBuilder;
import com.mardous.concurrency.task.LongResultTaskBuilder;
import com.mardous.concurrency.task.ResultTaskBuilder;
import com.mardous.concurrency.task.SimpleTaskBuilder;
import com.mardous.concurrency.task.StringResultTaskBuilder;

import java.io.File;

/**
 * @author Chris Alvarado (mardous)
 */
public final class AsyncWorker {

    private AsyncWorker() {}

    public static SimpleTaskBuilder simpleTask(Runnable runnable) {
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
