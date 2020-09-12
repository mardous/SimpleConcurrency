package com.mardous.concurrency;

import android.os.Handler;

/**
 * @param <T> The result type of method {@link #call()}
 * @author Chris Alvarado (mardous)
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
    protected void updateProgress(long progress, long max) {
        uiThreadHandler.post(() -> onProgressUpdate(progress, max));
    }

    /**
     * Called when the progress of this task has been updated
     * using {@link #updateProgress(long, long)}.
     *
     * <p>This method will always be called from the main thread.
     *
     * @param progress The current progress.
     * @param max      The max progress.
     */
    public abstract void onProgressUpdate(long progress, long max);
}
