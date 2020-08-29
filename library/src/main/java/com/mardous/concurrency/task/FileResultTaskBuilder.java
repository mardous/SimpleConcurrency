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
        return (FileResultTaskBuilder) addFilter(file -> !mustBeFile || file.isFile());
    }

    public FileResultTaskBuilder mustBeDirectory(boolean mustBeDirectory) {
        return (FileResultTaskBuilder) addFilter(file -> !mustBeDirectory || file.isDirectory());
    }

    public FileResultTaskBuilder mustBeEmptyDirectory(boolean mustBeEmpty) {
        return (FileResultTaskBuilder) addFilter(file -> {
            if (mustBeEmpty) {
                String[] files = file.list();
                if (files != null) {
                    return files.length == 0;
                }
                return false;
            }
            return true;
        });
    }

    public FileResultTaskBuilder acceptsMinimumLength(long minimumLength) {
        return (FileResultTaskBuilder) addFilter(file -> file.length() >= minimumLength);
    }

    public FileResultTaskBuilder acceptsMaximumLength(long maximumLength) {
        return (FileResultTaskBuilder) addFilter(file -> file.length() <= maximumLength);
    }
}
