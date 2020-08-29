package com.mardous.concurrency.task;

import android.os.Handler;
import com.mardous.concurrency.Handlers;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Represents a asynchronous task.
 *
 * @author Chris Alvarado (mardous)
 */
public abstract class Task {

    protected final Handler uiThreadHandler = Handlers.forMainThread();
    protected final Executor executor;

    private State state = State.IDLE;

    Task(Executor executor) {
        this.executor = executor;
    }

    /**
     * Executes this task.
     *
     * @return this same task.
     */
    public abstract Task execute();

    /**
     * Cancels this task.
     * For tasks that has been already canceled,
     * this has not effect.
     */
    public final void cancel() {
        if (executor instanceof ExecutorService) {
            ((ExecutorService) executor).shutdown();
        }
        setState(State.CANCELED);
    }

    /**
     * Gets the {@link State state} of this task.
     */
    public final State getState() {
        return state;
    }

    final void setState(State newState) {
        synchronized (this) {
            if (newState == null) {
                throw new IllegalArgumentException("Using null as task state is not permitted.");
            }
            if (newState.level < state.level) {
                throw new IllegalStateException("Downgrading task state is not permitted. You tried to downgrade from " +
                        state.nameWithLevel() + " to " + newState.nameWithLevel());
            }
            this.state = newState;
        }
    }

    /**
     * Represents the current execution state
     * of a task.
     * <p>1:) <b>IDLE</b>: Means the task is created but not
     * executed yet. Generally this state is never
     * available to the user as this is only used
     * internally by the library when creating the
     * {@link TaskBuilder builder}.
     * <p>2:) <b>RUNNING</b>: Means the task is currently running,
     * in other words, means that we are working.
     * <p>3:) <b>CANCELED</b>: Means the task has been previously
     * canceled by the user. When in this state, the
     * task is not capable of get back to the RUNNING state and
     * trying to do that will throw an {@link IllegalStateException}.
     * <p>4:) <b>FINISHED</b>: Means the task has finished its
     * execution normally (with no calls to {@link #cancel()}).
     * When you're using {@link ResultTask} and reach this state,
     * you may be capable of getting a result from {@link ResultTask#getResult()}.
     * When in this state, the task is not capable of get back to
     * the RUNNING state and trying to do that will throw an {@link IllegalStateException}.
     */
    public enum State {
        IDLE(0),
        RUNNING(25),
        CANCELED(50),
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
