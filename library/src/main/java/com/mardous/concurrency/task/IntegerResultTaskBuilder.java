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
        return (IntegerResultTaskBuilder) addFilter(integer -> integer >= min && integer <= max);
    }

    public IntegerResultTaskBuilder acceptsMajorThan(int against) {
        return (IntegerResultTaskBuilder) addFilter(integer -> integer > against);
    }

    public IntegerResultTaskBuilder acceptsMajorOrEqualTo(int against) {
        return (IntegerResultTaskBuilder) addFilter(integer -> integer >= against);
    }

    public IntegerResultTaskBuilder acceptsMinorThan(int against) {
        return (IntegerResultTaskBuilder) addFilter(integer -> integer < against);
    }

    public IntegerResultTaskBuilder acceptsMinorOrEqualTo(int against) {
        return (IntegerResultTaskBuilder) addFilter(integer -> integer <= against);
    }

    public IntegerResultTaskBuilder acceptsNegative(boolean acceptsNegative) {
        return (IntegerResultTaskBuilder) addFilter(integer -> acceptsNegative || integer > -1);
    }
}
