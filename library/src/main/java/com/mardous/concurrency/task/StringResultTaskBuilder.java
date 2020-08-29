package com.mardous.concurrency.task;

import androidx.annotation.NonNull;
import com.mardous.concurrency.AsyncCallable;

/**
 * @author Chris Alvarado (mardous)
 */
public class StringResultTaskBuilder extends ResultTaskBuilder<String> {

    public StringResultTaskBuilder(@NonNull AsyncCallable<String> action) {
        super(action);
    }

    public StringResultTaskBuilder acceptsEmpty(boolean acceptsEmpty) {
        this.resultFilter.add(s -> acceptsEmpty || !"".equals(s));
        return this;
    }

    public StringResultTaskBuilder acceptsMinimumLength(int minimumLength) {
        this.resultFilter.add(s -> s.length() >= minimumLength);
        return this;
    }

    public StringResultTaskBuilder acceptsMaximumLength(int maximumLength) {
        this.resultFilter.add(s -> s.length() <= maximumLength);
        return this;
    }

    public StringResultTaskBuilder acceptsLengthBetween(int min, int max) {
        this.resultFilter.add(s -> {
            int length = s.length();
            return length >= min && length <= max;
        });
        return this;
    }
}
