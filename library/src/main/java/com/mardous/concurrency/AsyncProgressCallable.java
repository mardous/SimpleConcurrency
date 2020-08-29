package com.mardous.concurrency;

import android.os.Handler;

public abstract class AsyncProgressCallable<T> extends AsyncCallable<T> {

    private Handler uiThreadHandler = Handlers.forMainThread();

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        updateProgress(0, 0);
    }

    /**
     * Used to update the progress.
     *
     * @param progress The current progress.
     * @param max The max progress.
     */
    protected void updateProgress(long progress, long max) {
        if (progress > max) {
            progress = max;
        }
        final long fProgress = progress;
        uiThreadHandler.post(() -> onProgressUpdate(fProgress, max));
    }

    /**
     * Called when the progress of this task has been updated
     * using {@link #updateProgress(long, long)}.
     * <p>This method will be called always from the main thread.
     *
     * @param progress The current progress.
     * @param max The max progress.
     */
    public abstract void onProgressUpdate(long progress, long max);
}
