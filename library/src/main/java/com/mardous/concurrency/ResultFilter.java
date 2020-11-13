package com.mardous.concurrency;

import android.util.Log;
import com.mardous.concurrency.internal.Predicate;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A class designed to hold {@link Predicate}s that can be used
 * to test any object resulting from the execution of {@link AsyncCallable#call()}.
 *
 * @author Christians Martínez Alvarado (mardous)
 */
public final class ResultFilter<T> {

    private List<Predicate<T>> filters = Collections.synchronizedList(new LinkedList<>());

    public void add(Predicate<T> predicate) {
        filters.add(predicate);
    }

    public boolean acceptable(T result) {
        for (int i = 0; i < filters.size(); i++) {
            Predicate<T> p = filters.remove(i);
            try {
                if (p != null && !p.test(result))
                    return false;
            } catch (NullPointerException e) {
                Log.w("AsyncWorker", "An error occurred while testing a result.", e);
            }
        }
        return true;
    }
}
