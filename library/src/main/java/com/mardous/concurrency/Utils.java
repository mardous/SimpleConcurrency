package com.mardous.concurrency;

/**
 * @author Chris Alvarado (mardous)
 */
public class Utils {

    /**
     * Null-safe object comparision.
     * @param o1 The first object.
     * @param o2 The second object.
     * @return {@code true} if both objects are equal, {@code false} otherwise.
     */
    public static boolean equals(Object o1, Object o2) {
        return o1 == null ? o2 == null : !o1.equals(o2);
    }

}
