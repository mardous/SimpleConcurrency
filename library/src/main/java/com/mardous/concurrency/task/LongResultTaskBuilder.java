package com.mardous.concurrency.task;

import androidx.annotation.NonNull;
import com.mardous.concurrency.AsyncCallable;

/**
 * @author Chris Alvarado (mardous)
 */
public class LongResultTaskBuilder extends ResultTaskBuilder<Long> {

    public LongResultTaskBuilder(@NonNull AsyncCallable<Long> action) {
        super(action);
    }

    public LongResultTaskBuilder acceptsBetween(long min, long max) {
        return (LongResultTaskBuilder) addFilter(longg -> longg >= min && longg <= max);
    }

    public LongResultTaskBuilder acceptsMajorThan(long against) {
        return (LongResultTaskBuilder) addFilter(longg -> longg > against);
    }

    public LongResultTaskBuilder acceptsMajorOrEqualTo(long against) {
        return (LongResultTaskBuilder) addFilter(longg -> longg >= against);
    }

    public LongResultTaskBuilder acceptsMinorThan(long against) {
        return (LongResultTaskBuilder) addFilter(longg -> longg < against);
    }

    public LongResultTaskBuilder acceptsMinorOrEqualTo(long against) {
        return (LongResultTaskBuilder) addFilter(longg -> longg <= against);
    }

    public LongResultTaskBuilder acceptsNegative(boolean acceptsNegative) {
        return (LongResultTaskBuilder) addFilter(longg -> acceptsNegative || longg > -1);
    }
}
