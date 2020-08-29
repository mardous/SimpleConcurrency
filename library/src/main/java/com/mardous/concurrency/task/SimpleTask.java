package com.mardous.concurrency.task;

import com.mardous.concurrency.Handlers;

import java.util.concurrent.Executor;

/**
 * A task that simply executes an action without
 * leaving a result.
 *
 * @author Chris Alvarado (mardous)
 */
public class SimpleTask extends Task<SimpleTask> {
    private final Runnable runnable;

    SimpleTask(Executor executor, Runnable runnable) {
        super(executor);
        this.runnable = runnable;
    }

    @Override
    public SimpleTask execute() {
        setState(State.RUNNING);
        executor.execute(() -> {
            runnable.run();
            Handlers.postHere(() -> setState(State.FINISHED));
        });
        return this;
    }
}
