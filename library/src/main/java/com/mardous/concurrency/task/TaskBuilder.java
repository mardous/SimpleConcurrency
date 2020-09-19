package com.mardous.concurrency.task;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import com.mardous.concurrency.Utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Christians Martínez Alvarado (mardous)
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
     * @see #usingPoolExecutor()
     */
    Executor executor = Executors.newSingleThreadExecutor(threadFactory);

    Lifecycle lifecycle;

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
     * Configures the {@link Executor} that will execute the task.
     *
     * <p>NOTE: By default, we use a single worker thread, which executes tasks off an
     * unbounded queue. This can benefit for long-running tasks, such as network operations.
     *
     * @param executor The executor to use.
     * @return this same builder instance.
     */
    public TaskBuilder<TaskType> using(@NonNull Executor executor) {
        Utils.assertNonNull(executor, "Executor");
        this.executor = executor;
        return this;
    }

    /**
     * Attach this {@link Task task} to the provided {@link Lifecycle}.
     *
     * @param lifecycle The lifecycle to attach to.
     * @return this same builder instance.
     */
    public TaskBuilder<TaskType> attachLifecycle(@NonNull Lifecycle lifecycle) {
        Utils.assertNonNull(lifecycle, "Lifecycle");
        this.lifecycle = lifecycle;
        return this;
    }

    /**
     * Creates a new {@link Task task}.
     * <p>Its behaviour will depend mainly on the type of builder you're using.
     *
     * @return The created task.
     */
    public abstract TaskType create();

    /**
     * Creates a new {@link Task task} and immediately proceeds to execute it.
     *
     * @return The executed task.
     */
    public abstract TaskType execute();
}
