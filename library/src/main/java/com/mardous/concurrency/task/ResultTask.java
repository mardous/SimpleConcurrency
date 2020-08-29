package com.mardous.concurrency.task;

import androidx.annotation.Nullable;
import com.mardous.concurrency.AsyncCallable;
import com.mardous.concurrency.Handlers;
import com.mardous.concurrency.ResultFilter;

import java.util.concurrent.Executor;

/**
 * A task that can execute an action and expect a
 * result from such action.
 *
 * @author Chris Alvarado (mardous)
 */
public class ResultTask<Result> extends Task<ResultTask<Result>> {

    private final AsyncCallable<Result> action;
    private final ResultFilter<Result> resultFilter;

    private Result result;

    ResultTask(Executor executor, AsyncCallable<Result> action, ResultFilter<Result> resultFilter) {
        super(executor);
        this.action = action;
        this.resultFilter = resultFilter;
    }

    @Nullable
    public Result getResult() {
        return result;
    }

    @Override
    public ResultTask<Result> execute() {
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
