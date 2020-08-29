package com.mardous.concurrency.task;

import com.mardous.concurrency.AsyncCallable;
import com.mardous.concurrency.Handlers;
import com.mardous.concurrency.ResultFilter;

import java.util.concurrent.Executor;

/**
 * @author Chris Alvarado (mardous)
 */
public class ResultTask<T> extends Task {

    private final AsyncCallable<T> action;
    private final ResultFilter<T> resultFilter;

    private T result;

    public ResultTask(Executor executor, AsyncCallable<T> action, ResultFilter<T> resultFilter) {
        super(executor);
        this.action = action;
        this.resultFilter = resultFilter;
    }

    public T getResult() {
        return result;
    }

    @Override
    public Task execute() {
        setState(State.RUNNING);
        uiThreadHandler.post(action::onPreExecute);
        executor.execute(() -> {
            try {
                this.result = action.call();
                if (!resultFilter.acceptable(result)) {
                    uiThreadHandler.post(() -> action.onBadResult(result));
                    return;
                }
                uiThreadHandler.post(() -> action.onSuccess(result));
            } catch (Exception e) {
                uiThreadHandler.post(() -> action.onError(e));
            }
            Handlers.postHere(() -> setState(State.FINISHED));
        });
        return this;
    }
}
