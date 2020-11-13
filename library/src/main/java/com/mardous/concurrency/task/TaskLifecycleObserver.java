package com.mardous.concurrency.task;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Marks a {@link Task} as a {@link LifecycleObserver}.
 *
 * @author Christians Martínez Alvarado (mardous)
 */
interface TaskLifecycleObserver extends LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onLifecycleDestroyed();
}
