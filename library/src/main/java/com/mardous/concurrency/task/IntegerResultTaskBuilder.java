package com.mardous.concurrency.task;

import androidx.annotation.NonNull;
import com.mardous.concurrency.AsyncCallable;

/**
 * @author Chris Alvarado (mardous)
 */
public class IntegerResultTaskBuilder extends ResultTaskBuilder<Integer> {

    public IntegerResultTaskBuilder(@NonNull AsyncCallable<Integer> action) {
        super(action);
    }

    public IntegerResultTaskBuilder acceptsBetween(int min, int max) {
        this.resultFilter.add(integer -> integer >= min && integer <= max);
        return this;
    }

    public IntegerResultTaskBuilder acceptsMajorThan(int against) {
        this.resultFilter.add(integer -> integer > against);
        return this;
    }

    public IntegerResultTaskBuilder acceptsMajorOrEqualTo(int against) {
        this.resultFilter.add(integer -> integer >= against);
        return this;
    }

    public IntegerResultTaskBuilder acceptsMinorThan(int against) {
        this.resultFilter.add(integer -> integer < against);
        return this;
    }

    public IntegerResultTaskBuilder acceptsMinorOrEqualTo(int against) {
        this.resultFilter.add(integer -> integer <= against);
        return this;
    }

    public IntegerResultTaskBuilder acceptsNegative(boolean acceptsNegative) {
        this.resultFilter.add(integer -> acceptsNegative || integer > -1);
        return this;
    }
}
