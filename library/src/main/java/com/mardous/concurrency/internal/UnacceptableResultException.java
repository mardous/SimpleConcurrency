package com.mardous.concurrency.internal;

import com.mardous.concurrency.ResultFilter;
import com.mardous.concurrency.task.ResultTask;
import com.mardous.concurrency.task.ResultTaskBuilder;

/**
 * Thrown when a {@link ResultTask} leaves an unacceptable result.
 * <i>Unacceptable</i> means such result has not passed
 * the rules added to a {@link ResultFilter filter}
 * through the {@link ResultTaskBuilder#addFilter(Predicate)}
 * method of a {@link ResultTaskBuilder}.
 *
 * @author Chris Alvarado (mardous)
 */
public class UnacceptableResultException extends Exception {

    /**
     * Constructs a {@code UnacceptableResultException}.
     *
     * @param result The result marked as <i>unacceptable</i>.
     */
    public UnacceptableResultException(Object result) {
        super("The result of the executed task was not approved. Result: " + result);
    }
}
