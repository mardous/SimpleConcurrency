package com.mardous.concurrency;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import com.mardous.concurrency.internal.UnacceptableResultException;

import java.util.concurrent.Callable;

/**
 * This is the base {@link Callable} used to operate along with
 * {@link com.mardous.concurrency.task.ResultTask}.
 * <p>The main difference of this with the standard {@link Callable}
 * is that this offers possibility for execute actions before
 * and after the execution of the task as well.
 *
 * @author Chris Alvarado (mardous)
 */
public abstract class AsyncCallable<T> implements Callable<T> {
    /**
     * Called before the {@link #call()} method of this
     * {@link Callable}.
     */
    @UiThread
    @CallSuper
    public void onPreExecute() {
        // Implemented by sub-classes.
    }

    /**
     * Called when the {@link #call()} method of this
     * {@link Callable} return a result without throwing an
     * exception.
     *
     * @param result The result of {@link #call()}.
     */
    @UiThread
    @CallSuper
    public void onSuccess(T result) {
        // Implemented by sub-classes.
    }

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

    /**
     * Called when the {@link #call()} method of this
     * {@link Callable} throws an exception.
     *
     * @param error The exception thrown.
     */
    @UiThread
    @CallSuper
    public void onError(Exception error) {
        // Implemented by sub-classes.
    }
}
