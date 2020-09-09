package com.mardous.concurrency.task;

import androidx.annotation.NonNull;
import com.mardous.concurrency.AsyncRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * A task that simply executes an action without
 * leaving a result.
 *
 * @author Chris Alvarado (mardous)
 */
public class SimpleTask extends Task<SimpleTask> {

    private final Runnable runnable;
    private Future<?> future;

    SimpleTask(ExecutorService executor, AsyncRunnable runnable) {
        super(executor, runnable);
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
        future = executor.submit(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                uiThreadHandler.post(() -> taskListener.onError(e));
            }
            setState(State.FINISHED);
        });
        return this;
    }
}
