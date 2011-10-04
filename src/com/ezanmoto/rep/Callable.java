package com.ezanmoto.rep;

import java.io.Serializable;

/**
 * A piece of code which can be called and which returns a value.
 *
 * @author S. M. Kelleher
 */
public interface Callable<T> extends Serializable {

    /**
     * Executes this code and returns a result.
     *
     * @return the result of executing this code
     */
    public T call();
}
