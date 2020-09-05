package com.mardous.concurrency.task;

import androidx.annotation.Nullable;
import com.mardous.concurrency.AsyncCallable;
import com.mardous.concurrency.ResultFilter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * A task that can execute an action and expect a
 * result from such action.
 *
 * @author Chris Alvarado (mardous)
 */
public class ResultTask<Result> extends Task<ResultTask<Result>> {

    private final AsyncCallable<Result> action;
    private final ResultFilter<Result> resultFilter;

    private Future<Result> future;

    ResultTask(ExecutorService executor, AsyncCallable<Result> action, ResultFilter<Result> resultFilter) {
        super(executor, action);
        this.action = action;
        this.resultFilter = resultFilter;
    }

    @Nullable
    public Result getResult() {
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void cancel(boolean mayInterruptIfRunning) {
        if (future != null) {
            future.cancel(mayInterruptIfRunning);
        }
        setState(State.CANCELED);
    }

    @Override
    public ResultTask<Result> execute() {
        setState(State.RUNNING);
        RunnableFuture<Result> futureTask = new FutureTask<>(() -> {
            try {
                Result result = action.call();
                if (resultFilter.acceptable(result))
                    uiThreadHandler.post(() -> action.onSuccess(result));
                else uiThreadHandler.post(() -> action.onBadResult(result));
                return result;
            } catch (Exception e) {
                uiThreadHandler.post(() -> action.onError(e));
            }
            setState(State.FINISHED);
            return null;
        });
        executor.submit(futureTask);
        future = futureTask;
        return this;
    }
}
