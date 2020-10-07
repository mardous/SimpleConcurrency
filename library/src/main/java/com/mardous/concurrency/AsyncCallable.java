package com.mardous.concurrency;

import com.mardous.concurrency.internal.UnacceptableResultException;
import com.mardous.concurrency.task.TaskConnection;

import java.util.concurrent.Callable;

/**
 * A {@link Callable} that extends the {@link TaskConnection} {@code class}.
 * <p>Generally, this is intended to operate along with {@link com.mardous.concurrency.task.ResultTask}.
 *
 * @author Christians Martínez Alvarado (mardous)
 */
public abstract class AsyncCallable<T> extends TaskConnection implements Callable<T> {

    /**
     * Called when the {@link #call()} method of this {@link Callable} returns a
     * result without throwing an exception.
     *
     * <p>If the task was cancelled previously, this method will never be called.
     *
     * @param result The result of {@link #call()}.
     */
    public void onSuccess(T result) {
    }

    /**
     * Called when the {@link #call()} method of this {@link Callable} return a
     * result that has not passed the filtering rules specified with the
     * {@link ResultFilter#add(com.mardous.concurrency.internal.Predicate)} method.
     *
     * <p>If the task was cancelled previously, this method will never be called.
     *
     * <p>The default implementation simply calls
     * {@link #onError(Exception)} with an {@link UnacceptableResultException}.
     *
     * @param result The result of {@link #call()}.
     */
    public void onBadResult(T result) {
        onError(new UnacceptableResultException(result));
    }
}
