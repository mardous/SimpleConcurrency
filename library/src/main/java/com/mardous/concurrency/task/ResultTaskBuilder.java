package com.mardous.concurrency.task;

import androidx.annotation.NonNull;
import com.mardous.concurrency.AsyncCallable;
import com.mardous.concurrency.ResultFilter;
import com.mardous.concurrency.Utils;
import com.mardous.concurrency.internal.Predicate;

/**
 * The {@link TaskBuilder builder} used to create {@link ResultTask result tasks}.
 *
 * @author Christians Martínez Alvarado (mardous)
 */
public class ResultTaskBuilder<Result> extends TaskBuilder<ResultTask<Result>> {

    private final AsyncCallable<Result> action;
    private final ResultFilter<Result> resultFilter = new ResultFilter<>();

    public ResultTaskBuilder(@NonNull AsyncCallable<Result> action) {
        this.action = action;
    }

    /**
     * Adds a {@link Predicate predicate} that will be used to
     * test the result of this task.
     * <p>All predicates are added to a {@link ResultFilter filter},
     * so when the task finishes its execution, and there is a result,
     * the filter will proceed to iterate through all predicates testing
     * then on the result.
     * <p>If a test is not passed, that result is declared as unacceptable,
     * and therefore, the {@link AsyncCallable#onBadResult(Object)} method
     * is called immediately.
     *
     * @param predicate The {@link Predicate predicate} that will be used
     *                  to test the result of this task.
     * @return This same builder.
     */
    public ResultTaskBuilder<Result> addFilter(Predicate<Result> predicate) {
        this.resultFilter.add(predicate);
        return this;
    }

    public ResultTaskBuilder<Result> notAccepts(Result not) {
        return addFilter(result -> !Utils.equals(result, not));
    }

    public ResultTaskBuilder<Result> acceptsNull(boolean acceptsNull) {
        return addFilter(result -> acceptsNull || result != null);
    }

    @Override
    public ResultTask<Result> create() {
        return new ResultTask<Result>(this, action, resultFilter);
    }

    @Override
    public ResultTask<Result> execute() {
        return create().execute();
    }
}
