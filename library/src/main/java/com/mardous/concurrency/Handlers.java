package com.mardous.concurrency;

import android.os.Handler;
import android.os.Looper;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class Handlers {

    /**
     * Creates a new {@link Handler} bound to the main thread.
     *
     * @return the created thread.
     */
    public static Handler forMainThread() {
        return new Handler(Looper.getMainLooper());
    }

    /**
     * Creates a new {@link Handler} bound to the thread from
     * which this method was called.
     *
     * @return the created thread.
     */
    public static Handler forThread() {
        return new Handler(Looper.myLooper());
    }

    /**
     * Creates a new {@link Handler} bound to the main thread
     * and uses it to post an action.
     *
     * @param runnable The action to post.
     */
    public static void postMainThread(Runnable runnable) {
        forMainThread().post(runnable);
    }

    /**
     * Creates a new {@link Handler} bound to the main thread
     * and uses it to post an action after some amount of time.
     *
     * @param runnable The action to post.
     * @param delayMs The amount of milliseconds to wait before
     *                posting the action.
     */
    public static void postDelayedMainThread(Runnable runnable, long delayMs) {
        forMainThread().postDelayed(runnable, delayMs);
    }

    /**
     * Creates a new {@link Handler} bound to the thread from
     * which this method was called and uses it to post an action.
     *
     * @param runnable The action to post.
     */
    public static void postHere(Runnable runnable) {
        forThread().post(runnable);
    }

	/**
     * Creates a new {@link Handler} bound to the thread from
     * which this method was called and uses it to post an action
     * after some amount of time.
     *
     * @param runnable The action to post.
     * @param delayMs The amount of milliseconds to wait before
     *                posting the action.
     */
    public static void postDelayedHere(Runnable runnable, long delayMs) {
        forThread().postDelayed(runnable, delayMs);
    }
}
