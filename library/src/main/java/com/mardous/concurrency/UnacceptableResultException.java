package com.mardous.concurrency;

/**
 * @author Chris Alvarado (mardous)
 */
public class UnacceptableResultException extends Exception {

    public UnacceptableResultException(Object result) {
        super("The result of the executed task was not approved. Result was: " + result);
    }

}
