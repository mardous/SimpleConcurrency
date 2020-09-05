package com.mardous.concurrency;

import com.mardous.concurrency.internal.TaskListener;
import com.mardous.concurrency.internal.UnacceptableResultException;

import java.util.concurrent.Callable;

/**
 * A {@link Callable} that implements the {@link TaskListener} {@code interface}.
 * <p>Generally, this is intended to operate along with {@link com.mardous.concurrency.task.ResultTask}.
 *
 * @author Chris Alvarado (mardous)
 */
public abstract class AsyncCallable<T> implements Callable<T>, TaskListener {

    @Override
    public void onPreExecute() {}

    /**
     * Called when the {@link #call()} method of this
     * {@link Callable} return a result without throwing an
     * exception.
     *
     * @param result The result of {@link #call()}.
     */
    public void onSuccess(T result) {}

    /**
     * Called when the {@link #call()} method of this
     * {@link Callable} return a result that has not
     * passed the filtering rules specified with the
     * {@link ResultFilter#add(com.mardous.concurrency.internal.Predicate)} method.
     *
     * <p>The default implementation simply calls
     * {@link #onError(Exception)} with an {@link UnacceptableResultException}.
     *
     * @param result The result of {@link #call()}.
     */
    public void onBadResult(T result) {
        // Default implementation.
        onError(new UnacceptableResultException(result));
    }

    @Override
    public void onCanceled() {}

    @Override
    public void onError(Exception error) {}
}
