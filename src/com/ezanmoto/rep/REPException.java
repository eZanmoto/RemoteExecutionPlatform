package com.ezanmoto.rep;

/**
 * Caused by an exceptional condition at some point in the
 * {@code RemoteExecutionPlatform} framework.
 *
 * @author S. M. Kelleher
 */
public class REPException extends RuntimeException {

    /**
     * @param message explains why this exception occurred
     */
    public REPException( String message ) {
        super( message );
    }

    /**
     * @param message explains why this exception occurred
     * @param cause may have additional information on why this exception
     * occurred
     */
    public REPException( String message, Throwable cause ) {
        super( message, cause );
    }
}
