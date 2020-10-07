package com.mardous.concurrency.task;

import android.os.Handler;
import android.util.Log;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.Lifecycle;
import com.mardous.concurrency.Handlers;
import com.mardous.concurrency.Utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * A {@code task} is defined by a computation that runs on a background thread and
 * may leave a result, which is published on the UI thread.
 *
 * <p>Currently, we provide two types:
 * <ol>
 *     <li>{@link SimpleTask}</li>
 *     <li>{@link ResultTask}</li>
 * </ol>
 *
 * @author Christians Martínez Alvarado (mardous)
 */
public abstract class Task implements TaskLifecycleObserver {

    protected final Handler uiThreadHandler = Handlers.forMainThread();

    protected volatile Lifecycle lifecycle;
    protected volatile Executor executor;
    protected volatile TaskConnection taskConnection;

    private volatile State state = State.IDLE;

    Task(TaskBuilder taskBuilder, TaskConnection taskConnection) {
        this.lifecycle = taskBuilder.lifecycle;
        this.executor = taskBuilder.executor;
        this.taskConnection = taskConnection;

        if (lifecycle != null)
            lifecycle.addObserver(this);
        else Log.w("SimpleConcurrency", "Running a Task without an attached Lifecycle may produce memory leaks");
    }

    /**
     * Executes the task. The task returns itself (this) so that the caller
     * can keep a reference to it.
     *
     * @return this same task.
     */
    @MainThread
    public abstract Task execute();

    /**
     * Attempts to cancel execution of this task.  This attempt will
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
     *                              task should be interrupted; otherwise, in-progress tasks are allowed
     *                              to complete
     * @return {@code false} if the task could not be cancelled,
     * typically because it has already completed normally;
     * {@code true} otherwise
     */
    @MainThread
    public abstract boolean cancel(boolean mayInterruptIfRunning);

    /**
     * Returns true if this task was cancelled before it completed normally
     *
     * @return {@code true} if this task was cancelled before it completed
     */
    public synchronized final boolean isCancelled() {
        return state == State.CANCELLED;
    }

    /**
     * Returns true if this task completed normally
     *
     * @return {@code true} if this task completed
     */
    public synchronized final boolean isFinished() {
        return state == State.FINISHED;
    }

    /**
     * Gets the {@link State state} of this task.
     */
    public final State getState() {
        return state;
    }

    /**
     * Internally sets the state of this task.
     */
    protected final void setState(final State newState) {
        synchronized (Task.class) {
            Utils.assertNonNull(newState, "State");
            if (newState == State.RUNNING) {
                if (state == State.RUNNING) {
                    throw new IllegalStateException("Cannot execute task: the task is already running.");
                } else if (state == State.FINISHED) {
                    throw new IllegalArgumentException("Cannot execute task: the task was already executed.");
                } else if (state == State.CANCELLED) {
                    throw new IllegalArgumentException("Cannot execute task: the task was canceled previously.");
                }
            }

            switch (newState) {
                case RUNNING:
                    if (taskConnection == null) return;
                    taskConnection.mTask = this;
                    post(() -> {
                        if (taskConnection == null) return;
                        taskConnection.onPreExecute();
                    });
                    break;
                case CANCELLED:
                    post(() -> {
                        if (taskConnection == null) return;
                        taskConnection.onCancelled();
                        completeShutdown();
                    });
                    break;
                case FINISHED:
                    post(() -> {
                        if (taskConnection == null) return;
                        taskConnection.onFinished();
                        completeShutdown();
                    });
                    break;
            }
            this.state = newState;
        }
    }

    /**
     * Internally cancels this task.
     */
    protected final <T> boolean doCancellation(Future<T> future, boolean mayInterruptIfRunning) {
        if (isCancelled() || isFinished()) {
            return true;
        }
        setState(State.CANCELLED);
        return future != null && future.cancel(mayInterruptIfRunning);
    }

    /**
     * Posts an action on the main thread.
     */
    protected final void post(@NonNull Runnable action) {
        uiThreadHandler.post(action);
    }

    /**
     * Posts an error on the main thread. In other words,
     * calls the {@link TaskConnection#onError(Exception)} method.
     */
    @WorkerThread
    protected final void postError(Exception e) {
        if (!isCancelled()) {
            post(() -> taskConnection.onError(e));
        }
    }

    private void completeShutdown() {
        if (lifecycle != null)
            lifecycle.removeObserver(this);

        if (taskConnection != null) {
            // To reduce footprint
            taskConnection.mTask = null;
            taskConnection = null;
        }
    }

    @Override
    public void onLifecycleDestroyed() {
        cancel(true);
    }

    /**
     * Represents the current state of a task.
     */
    public enum State {
        /**
         * Means the task is created but has been not executed yet.
         */
        IDLE,
        /**
         * Means the task is currently running.
         */
        RUNNING,
        /**
         * Means the task has been previously cancelled by the user.
         */
        CANCELLED,
        /**
         * Means the task has finished its execution normally
         * (with no calls to {@link #cancel(boolean)}).
         */
        FINISHED;
    }
}
