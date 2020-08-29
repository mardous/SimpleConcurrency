package com.mardous.concurrency.task;

import androidx.annotation.NonNull;
import com.mardous.concurrency.AsyncCallable;

import java.io.File;

/**
 * @author Chris Alvarado (mardous)
 */
public class FileResultTaskBuilder extends ResultTaskBuilder<File> {

    public FileResultTaskBuilder(@NonNull AsyncCallable<File> action) {
        super(action);
    }

    public FileResultTaskBuilder mustBeFile(boolean mustBeFile) {
        this.resultFilter.add(file -> !mustBeFile || file.isFile());
        return this;
    }

    public FileResultTaskBuilder mustBeDirectory(boolean mustBeDirectory) {
        this.resultFilter.add(file -> !mustBeDirectory || file.isDirectory());
        return this;
    }

    public FileResultTaskBuilder mustBeEmptyDirectory(boolean mustBeEmpty) {
        this.resultFilter.add(file -> {
            if (mustBeEmpty) {
                String[] files = file.list();
                if (files != null) {
                    return files.length == 0;
                }
                return false;
            }
            return true;
        });
        return this;
    }

    public FileResultTaskBuilder acceptsMinimumLength(long minimumLength) {
        this.resultFilter.add(file -> file.length() >= minimumLength);
        return this;
    }

    public FileResultTaskBuilder acceptsMaximumLength(long maximumLength) {
        this.resultFilter.add(file -> file.length() <= maximumLength);
        return this;
    }
}
