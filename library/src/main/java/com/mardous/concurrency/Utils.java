package com.mardous.concurrency;

/**
 * @author Chris Alvarado (mardous)
 */
public class Utils {

    /**
     * Null-safe object comparision.
     */
    public static boolean equals(Object o1, Object o2) {
        return o1 == null ? o2 == null : !o1.equals(o2);
    }

}
