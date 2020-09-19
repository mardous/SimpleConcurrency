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
 * may leave a result, which is published on the UI thread. An asynchronous task is
 * defined by one generic type, called {@code Type}, which represents the type of
 * the task:
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
    protected TaskConnection taskConnection;

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
     * task should be interrupted; otherwise, in-progress tasks are allowed
     * to complete
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
    public final boolean isCancelled() {
        synchronized (Task.class) {
            return state == State.CANCELLED;
        }
    }

    /**
     * Gets the {@link State state} of this task.
     */
    public final State getState() {
        synchronized (Task.class) {
            return state;
        }
    }

    /**
     * Internally sets the state of this task.
     */
    protected final void setState(final State state) {
        setStateInternal(state);
        synchronized (Task.class) {
            uiThreadHandler.post(() -> {
                switch (state) {
                    case RUNNING:
                        taskConnection.mTask = this;
                        taskConnection.onPreExecute();
                        break;
                    case CANCELLED:
                        taskConnection.onCancelled();
                        completeShutdown();
                        break;
                    case FINISHED:
                        taskConnection.onFinished();
                        completeShutdown();
                        break;
                }
            });
        }
    }

    private void setStateInternal(State newState) {
        synchronized (Task.class) {
            Utils.assertNonNull(newState, "State");
            if (newState == State.RUNNING && state == State.RUNNING) {
                throw new IllegalStateException("Cannot execute task: the task is already running.");
            }
            if (!state.isBellow(newState)) {
                throw new IllegalArgumentException("Downgrade task state is not permitted. You have tried to downgrade from " +
                        state.nameWithLevel() + " to " + newState.nameWithLevel());
            }
            this.state = newState;
        }
    }

    /**
     * Internally cancels this task.
     */
    protected final <T> boolean doCancellation(Future<T> future, boolean mayInterruptIfRunning) {
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
        post(() -> taskConnection.onError(e));
    }

    private void completeShutdown() {
        if (lifecycle != null)
            lifecycle.removeObserver(this);

        // To reduce footprint
        taskConnection.mTask = null;
        taskConnection = null;
        executor = null;
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
        IDLE(0),
        /**
         * Means the task is currently running.
         */
        RUNNING(25),
        /**
         * Means the task has been previously cancelled by the user.
         */
        CANCELLED(50),
        /**
         * Means the task has finished its execution normally
         * (with no calls to {@link #cancel(boolean)}).
         */
        FINISHED(75);

        int level;

        State(int level) {
            this.level = level;
        }

        boolean isAtLeast(State state) {
            return level >= state.level;
        }

        boolean isBellow(State state) {
            return level < state.level;
        }

        String nameWithLevel() {
            return name() + " (Level: " + level + ")";
        }
    }
}
