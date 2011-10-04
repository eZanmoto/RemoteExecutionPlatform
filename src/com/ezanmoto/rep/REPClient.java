package com.ezanmoto.rep;

/**
 * Allows execution of commands on a remote device.
 *
 * @author S. M. Kelleher
 */
public interface REPClient<T> {

    /**
     * Execute the code this client contains on a remote device.
     *
     * @return the result of executing this code
     */
    public T call();
}
