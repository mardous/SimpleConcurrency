package com.mardous.concurrency.task;

/**
 * An {@code interface} used to listen to events occurred during the
 * execution of a {@link Task}.
 * <p>Such events are:
 * <ol>
 *     <li>{@link #onPreExecute()}</lo>
 *     <li>{@link #onCancelled()}</lo>
 *     <li>{@link #onError(Exception)}</lo>
 * </ol>
 *
 * @author Christians Martínez Alvarado (mardous)
 */
public abstract class TaskConnection {

    /**
     * Keeps a reference to the associated task.
     * Once the task is finished, we set its value to null
     * in order to avoid memory leaks.
     */
    Task mTask;

    /**
     * Attempts to cancel execution of the associated task.  This attempt will
     * fail if the task has already completed, has already been cancelled,
     * or could not be cancelled for some other reason. If successful,
     * and this task has not started when {@code cancel} is called,
     * this task should never run.  If the task has already started,
     * then the {@code mayInterruptIfRunning} parameter determines
     * whether the thread executing this task should be interrupted in
     * an attempt to stop the task.
     *
     * <p>After this method returns, subsequent calls to {@link #isCancelled}
     * will always return {@code true}.
     *
     * @param mayInterruptIfRunning {@code true} if the thread executing this
     * task should be interrupted; otherwise, in-progress tasks are allowed
     * to complete
     * @return {@code false} if the task could not be cancelled,
     * typically because it has already completed normally;
     * {@code true} otherwise
     */
    protected boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (this) {
            return mTask != null && mTask.cancel(mayInterruptIfRunning);
        }
    }

    /**
     * Returns true if the associated task was cancelled before it completed normally.
     * Is highly recommended to check this value periodically on the execution of you
     * task in order to stop it when necessary.
     *
     * <p>In example: if we are running a loop;
     * <pre><code>
     *     Person oldest = null;
     *     for (Person person : persons) {
     *         if (!isCancelled()) {  // First, check if we can continue.
     *            if (person.age >= 60) {
     *                oldest = person;
     *                break;
     *            }
     *         } else {
     *             // The task has been cancelled; stop immediately.
     *             // Don't care, the exception will be handled
     *             // by the TaskConnection.onError(Exception) method.
     *             throw new Exception("My task was cancelled before posting its result");
     *         }
     *     }
     *     return oldest;
     * </code></pre>
     *
     * @return {@code true} if the task was cancelled, {@code false} otherwise.
     */
    protected boolean isCancelled() {
        synchronized (this) {
            return mTask != null && mTask.isCancelled();
        }
    }

    /**
     * Called when the associated {@link Task} of this
     * {@link TaskConnection} is ready to execute. This method will always
     * be called from the <b>main</b> {@link Thread thread}.
     *
     * <p>Default implementation does nothing.
     */
    protected void onPreExecute() {
    }

    /**
     * Called when the associated {@link Task} of this {@link TaskConnection} has been
     * cancelled by the user. This method will always be called from the
     * <b>main</b> {@link Thread thread}.
     *
     * <p>Default implementation does nothing.
     */
    protected void onCancelled() {
    }

    /**
     * Called when the associated {@link Task} of this {@link TaskConnection} has finished
     * in an unexpected way due to an error during its execution. This method will always
     * be called from the <b>main</b> {@link Thread thread}.
     *
     * <p>Default implementation does nothing.
     *
     * @param e An {@link Exception} describing the error.
     */
    protected void onError(Exception e) {
    }

    /**
     * Called when the associated {@link Task} of this {@link TaskConnection} has finished.
     * This is a generic callback and will be called regardless of if the task finished
     * successfully or not.
     *
     * <p>If your task is holding some resources this may be a good point
     * to release then.
     *
     * <p>Default implementation does nothing.
     */
    protected void onFinished() {
    }
}
