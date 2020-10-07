package com.mardous.concurrency;

import android.os.Handler;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;

/**
 * @param <T> The result type of method {@link #call()}
 * @author Christians Martínez Alvarado (mardous)
 */
public abstract class AsyncProgressCallable<T> extends AsyncCallable<T> {

    private Handler uiThreadHandler = Handlers.forMainThread();

    @Override
    public void onPreExecute() {
        updateProgress(0, 0);
    }

    /**
     * Used to update the progress.
     *
     * @param progress The current progress.
     * @param max      The max progress.
     */
    @WorkerThread
    protected void updateProgress(long progress, long max) {
        if (!isCancelled()) {
            uiThreadHandler.post(() -> onProgressUpdate(progress, max));
        }
    }

    /**
     * Called when the progress of this task has been updated using {@link #updateProgress(long, long)}.
     *
     * <p>If the task was cancelled previously, this method will never be called.
     *
     * @param progress The current progress.
     * @param max      The max progress.
     */
    @UiThread
    public abstract void onProgressUpdate(long progress, long max);
}
