package com.mardous.concurrency.task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chris Alvarado (mardous)
 */
public abstract class TaskBuilder<TaskType extends Task> {

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = 20;
    private static final int KEEP_ALIVE_SECONDS = 3;

    private final ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "SimpleConcurrency-Task #" + count.getAndIncrement());
        }
    };

    /**
     * An {@link Executor} that uses a single worker thread operating off an unbounded queue.
     *
     * <p>This is the default executor, however you can change it by calling {@link #using(Executor)}
     * with an executor configured for your use case.
     *
     * @see #using(Executor)
     * @see #usePoolExecutor()
     */
    protected Executor executor = Executors.newSingleThreadExecutor(threadFactory);

    /**
     * Configures the {@link Executor} that will execute the task.
     *
     * <p>NOTE: By default, we use a single worker thread, which executes tasks off an
     * unbounded queue. This can benefit for long-running tasks, such as network operations.
     *
     * @param executor The executor to use.
     * @return this same builder instance.
     */
    public TaskBuilder<TaskType> using(Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor cannot be null.");
        }
        this.executor = executor;
        return this;
    }

    /**
     * Configures this builder to use a pool of threads instead of a single worker thread.
     *
     * <p>You should use this only if you are planing to execute a small, CPU-bound task,
     * which can benefit from a bounded pool and queuing.
     *
     * @see #using(Executor)
     * @return this same builder instance.
     */
    public TaskBuilder<TaskType> usingPoolExecutor() {
        return using(new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS, new SynchronousQueue<>(), threadFactory));
    }

    /**
     * Creates a new {@link Task task}.
     * <p>Its behaviour will depend mainly on the type of builder you're using.
     *
     * @return The created task.
     */
    public abstract TaskType create();

    /**
     * Creates a new {@link Task task} and immediately process to execute it.
     *
     * @return The executed task.
     */
    public abstract TaskType execute();
}
