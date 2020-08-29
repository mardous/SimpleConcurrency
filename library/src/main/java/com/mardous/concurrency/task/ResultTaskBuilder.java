package com.mardous.concurrency.task;

import androidx.annotation.NonNull;
import com.mardous.concurrency.AsyncCallable;
import com.mardous.concurrency.ResultFilter;
import com.mardous.concurrency.Utils;
import com.mardous.concurrency.internal.Predicate;

/**
 * @author Chris Alvarado (mardous)
 */
public class ResultTaskBuilder<T> extends TaskBuilder {

    private final AsyncCallable<T> action;

    final ResultFilter<T> resultFilter = new ResultFilter<>();

    public ResultTaskBuilder(@NonNull AsyncCallable<T> action) {
        this.action = action;
    }

    public ResultTaskBuilder<T> addFilter(Predicate<T> predicate) {
        this.resultFilter.add(predicate);
        return this;
    }

    public ResultTaskBuilder<T> notAccepts(T not) {
        this.resultFilter.add(t -> !Utils.equals(t, not));
        return this;
    }

    public ResultTaskBuilder<T> acceptsNull(boolean acceptsNull) {
        this.resultFilter.add(result -> acceptsNull || result != null);
        return this;
    }

    @Override
    public Task execute() {
        return new ResultTask<T>(executor, action, resultFilter).execute();
    }
}
