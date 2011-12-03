package com.ezanmoto.rep.client;

import java.io.File;

/**
 * An object which takes a piece of code and passes processing to a remote
 * device.
 *
 * @author S. M. Kelleher
 */
public interface REPClient<T> {

    /**
     * Compile the {@code Callable} class in {@code f} on the local device, run
     * its {@code call()} method, and return the result.
     *
     * @param f the {@code Callable} java file to run on the local device
     * @return the result of executing {@code call()} on the local device
     */
    public T callLocally( File f );

    /**
     * Compile the {@code Callable} class in {@code f} on the remote device, run
     * its {@code call()} method, and return the result.
     *
     * @param f the {@code Callable} java file to run on the remote device
     * @param host the address of the remote device
     * @return the result of executing {@code call()} on the remote device
     */
    public T callWithHost( File f, String host );
}
