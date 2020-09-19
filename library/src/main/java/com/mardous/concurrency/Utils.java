package com.mardous.concurrency;

import androidx.annotation.Nullable;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class Utils {

    public static void assertNonNull(@Nullable Object object, @Nullable String objectName) {
        if (object == null) {
            objectName = objectName == null ? "The given object" : objectName;
            throw new NullPointerException(objectName + " must not be null.");
        }
    }

    /**
     * Null-safe object comparision.
     *
     * @param o1 The first object.
     * @param o2 The second object.
     * @return {@code true} if both objects are equal, {@code false} otherwise.
     */
    public static boolean equals(Object o1, Object o2) {
        return o1 == null ? o2 == null : !o1.equals(o2);
    }

}
