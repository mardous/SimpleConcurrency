package com.mardous.concurrency.internal;

import com.mardous.concurrency.task.Task;

/**
 * An {@code interface} used to listen to events occurred during the
 * execution of a {@link Task}.
 * <p>Such events are:
 * <ol>
 *     <li>{@link #onPreExecute()}</lo>
 *     <li>{@link #onCanceled()}</lo>
 *     <li>{@link #onError(Exception)}</lo>
 * </ol>
 *
 * @author Chris Alvarado (mardous)
 */
public interface TaskListener {
    /**
     * Called when the associated {@link Task} of this
     * {@link TaskListener} is ready to execute. This method will always
     * be called from the <b>main</b> {@link Thread thread}.
     */
    void onPreExecute();

    /**
     * Called when the associated {@link Task} of this
     * {@link TaskListener} has been canceled by the user. This method will always
     * be called from the <b>main</b> {@link Thread thread}.
     */
    void onCanceled();

    /**
     * Called when the associated {@link Task} of this
     * {@link TaskListener} has finished in an unexpected way due to an error
     * during its execution. This method will always be called from the
     * <b>main</b> {@link Thread thread}.
     *
     * @param e An {@link Exception} describing the error.
     */
    void onError(Exception e);
}
