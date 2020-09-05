package com.mardous.concurrency;

import com.mardous.concurrency.internal.TaskListener;

/**
 * A {@link Runnable} that implements the {@link TaskListener} {@code interface}.
 *
 * @author Chris Alvarado (mardous)
 */
public abstract class AsyncRunnable implements Runnable, TaskListener {
    @Override
    public void onPreExecute() {}

    @Override
    public void onCanceled() {}

    @Override
    public void onError(Exception e) {}
}
