package com.mardous.concurrency;

import android.util.Log;
import com.mardous.concurrency.internal.Predicate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class ResultFilter<T> {

    private List<Predicate<T>> filters = Collections.synchronizedList(new LinkedList<>());

    public void add(Predicate<T> predicate) {
        filters.add(predicate);
    }

    public boolean acceptable(T result) {
        for (Predicate<T> p : filters) {
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
