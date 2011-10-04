package com.ezanmoto.rep;

/**
 * Allows a remote device to execute commands on this device.
 *
 * @author S. M. Kelleher
 */
public interface REPServer {

    /**
     * The port on which all {@code REPServer} classes should be listening for
     * requests.
     */
    public static final int REP_PORT = 1309;

    /**
     * Starts this {@code REPServer} processing incoming processing requests.
     */
    public void start();

    /**
     * Stops this {@code REPServer} processing incoming processing requests.
     */
    public void stop();
}
