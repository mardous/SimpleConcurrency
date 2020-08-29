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
        this.resultFilter.add(longg -> longg >= min && longg <= max);
        return this;
    }

    public LongResultTaskBuilder acceptsMajorThan(long against) {
        this.resultFilter.add(longg -> longg > against);
        return this;
    }

    public LongResultTaskBuilder acceptsMajorOrEqualTo(long against) {
        this.resultFilter.add(longg -> longg >= against);
        return this;
    }

    public LongResultTaskBuilder acceptsMinorThan(long against) {
        this.resultFilter.add(longg -> longg < against);
        return this;
    }

    public LongResultTaskBuilder acceptsMinorOrEqualTo(long against) {
        this.resultFilter.add(longg -> longg <= against);
        return this;
    }

    public LongResultTaskBuilder acceptsNegative(boolean acceptsNegative) {
        this.resultFilter.add(longg -> acceptsNegative || longg > -1);
        return this;
    }
}
