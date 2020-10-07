package com.mardous.concurrency.task;

import android.os.Binder;
import android.os.Process;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import com.mardous.concurrency.AsyncCallable;
import com.mardous.concurrency.ResultFilter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A task that can execute an action and expect a
 * result from such action.
 *
 * @author Christians Martínez Alvarado (mardous)
 */
public class ResultTask<Result> extends Task {

    private final AsyncCallable<Result> action;
    private final ResultFilter<Result> resultFilter;

    private FutureTask<Result> future;

    ResultTask(TaskBuilder taskBuilder, AsyncCallable<Result> action, ResultFilter<Result> resultFilter) {
        super(taskBuilder, action);
        this.action = action;
        this.resultFilter = resultFilter;
    }

    /**
     * Waits if necessary for the task to complete, and then retrieves its result.
     *
     * @return the computed result
     * @throws ExecutionException   if the computation threw an
     *                              exception
     * @throws InterruptedException if the current thread was interrupted
     *                              while waiting
     */
    @Nullable
    @MainThread
    public Result getResult() throws ExecutionException, InterruptedException {
        return future.get();
    }

    /**
     * Waits if necessary for at most the given time for the task to complete,
     * and then retrieves its result, if available.
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @return the computed result
     * @throws ExecutionException   if the computation threw an
     * @throws InterruptedException if the current thread was interrupted
     *                              while waiting
     * @throws TimeoutException     if the wait timed out
     */
    @Nullable
    @MainThread
    public Result getResult(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return doCancellation(future, mayInterruptIfRunning);
    }

    @NonNull
    @Override
    public ResultTask<Result> execute() {
        setState(State.RUNNING);

        future = new FutureTask<>(() -> {
            Result result;
            try {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                result = action.call();
                Binder.flushPendingCommands();
            } catch (Exception e) {
                postError(e);
                return null;
            } finally {
                setState(State.FINISHED);
            }
            return postResult(result);
        });

        executor.execute(future);
        return this;
    }

    @WorkerThread
    private Result postResult(Result result) {
        if (!isCancelled()) {
            if (resultFilter.acceptable(result)) {
                post(() -> action.onSuccess(result));
            } else {
                post(() -> action.onBadResult(result));
            }
        }
        return result;
    }
}
