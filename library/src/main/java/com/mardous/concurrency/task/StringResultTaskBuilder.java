package com.mardous.concurrency.task;

import androidx.annotation.NonNull;
import com.mardous.concurrency.AsyncCallable;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class StringResultTaskBuilder extends ResultTaskBuilder<String> {

    public StringResultTaskBuilder(@NonNull AsyncCallable<String> action) {
        super(action);
    }

    public StringResultTaskBuilder acceptsEmpty(boolean acceptsEmpty) {
        return (StringResultTaskBuilder) addFilter(s -> acceptsEmpty || !"".equals(s));
    }

    public StringResultTaskBuilder acceptsMinimumLength(int minimumLength) {
        return (StringResultTaskBuilder) addFilter(s -> s.length() >= minimumLength);
    }

    public StringResultTaskBuilder acceptsMaximumLength(int maximumLength) {
        return (StringResultTaskBuilder) addFilter(s -> s.length() <= maximumLength);
    }

    public StringResultTaskBuilder acceptsLengthBetween(int min, int max) {
        return (StringResultTaskBuilder) addFilter(s -> {
            int length = s.length();
            return length >= min && length <= max;
        });
    }
}
