package com.mardous.concurrency.task;

import androidx.annotation.NonNull;
import com.mardous.concurrency.AsyncRunnable;

import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * A task that simply executes an action without
 * leaving a result.
 *
 * @author Christians Marínez Alvarado (mardous)
 */
public class SimpleTask extends Task {

    private final AsyncRunnable runnable;
    private RunnableFuture<?> future;

    SimpleTask(TaskBuilder taskBuilder, AsyncRunnable runnable) {
        super(taskBuilder, runnable);
        this.runnable = runnable;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return doCancellation(future, mayInterruptIfRunning);
    }

    @NonNull
    @Override
    public SimpleTask execute() {
        setState(State.RUNNING);

        future = new FutureTask<>(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                postError(e);
            } finally {
                setState(State.FINISHED);
            }
        }, null);

        executor.execute(future);

        return this;
    }
}
