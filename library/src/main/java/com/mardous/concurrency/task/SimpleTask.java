package com.mardous.concurrency.task;

import com.mardous.concurrency.Handlers;

import java.util.concurrent.Executor;

/**
 * @author Chris Alvarado (mardous)
 */
public class SimpleTask extends Task {
    private final Runnable runnable;

    public SimpleTask(Executor executor, Runnable runnable) {
        super(executor);
        this.runnable = runnable;
    }

    @Override
    public Task execute() {
        setState(State.RUNNING);
        executor.execute(() -> {
            runnable.run();
            Handlers.postHere(() -> setState(State.FINISHED));
        });
        return this;
    }
}
