package com.mardous.concurrency.task;

import android.os.Handler;
import android.os.Looper;
import com.mardous.concurrency.Handlers;
import com.mardous.concurrency.internal.TaskListener;

import java.util.concurrent.ExecutorService;

/**
 * @param <Type> The type of this task.
 * @author Chris Alvarado (mardous)
 */
public abstract class Task<Type extends Task> {

    protected final Handler uiThreadHandler = Handlers.forMainThread();
    protected ExecutorService executor;
    protected TaskListener taskListener;

    private State state = State.IDLE;

    Task(ExecutorService executor, TaskListener taskListener) {
        this.executor = executor;
        this.taskListener = taskListener;
    }

    /**
     * Executes this task.
     *
     * @return this same task.
     */
    public abstract Type execute();

    /**
     * Cancels this task.
     * For tasks that has been already canceled,
     * this has no effect.
     */
    public abstract void cancel(boolean mayInterruptIfRunning);

    /**
     * Gets the {@link State state} of this task.
     */
    public final State getState() {
        return state;
    }

    protected final void setState(final State state) {
        synchronized (this) {
            setState0(state);
            switch (state) {
                case RUNNING:
                    uiThreadHandler.post(taskListener::onPreExecute);
                    Looper.prepare();
                    break;
                case CANCELED:
                    uiThreadHandler.post(taskListener::onCanceled);
                    completeShutdown();
                    break;
                case FINISHED:
                    completeShutdown();
                    break;
            }
        }
    }

    private void setState0(State newState) {
        synchronized (this) {
            if (newState == null) {
                throw new IllegalArgumentException("'null' is not a valid state!");
            }
            if (newState.level < state.level) {
                throw new IllegalStateException("Downgrading task state is not permitted. You have tried to downgrade from " +
                        state.nameWithLevel() + " to " + newState.nameWithLevel());
            }
            this.state = newState;
        }
    }

    protected final void completeShutdown() {
        if (executor != null) {
            executor.shutdown();
        }
        if (Looper.myLooper() != null) {
            Looper.myLooper().quit();
        }
        // To reduce footprint
        executor = null;
        taskListener = null;
    }

    /**
     * Represents the current state of a task.
     */
    public enum State {
        /**
         * Means the task is created but has been
         * not executed yet.
         */
        IDLE(0),
        /**
         * Means the task is currently running.
         */
        RUNNING(25),
        /**
         * Means the task has been previously canceled by the user.
         * When in this state, the task is not capable of get back
         * to the RUNNING state and trying to do that will throw an
         * {@link IllegalStateException}.
         */
        CANCELED(50),
        /**
         * Means the task has finished its execution normally
         * (with no calls to {@link #cancel(boolean)}). When you're using
         * {@link ResultTask} and reach this state, you may be capable
         * of getting a result from {@link ResultTask#getResult()}.
         * When in this state, the task is not capable of get back to
         * the RUNNING state and trying to do that will throw an
         * {@link IllegalStateException}.
         */
        FINISHED(75);

        int level;

        State(int level) {
            this.level = level;
        }

        String nameWithLevel() {
            return name() + "(Level: " + level + ")";
        }
    }
}
