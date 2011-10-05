package com.ezanmoto.rep;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A function that can be executed on a remote device.
 *
 * @author S. M. Kelleher
 */
public class RemoteFunction<T> implements REPClient {

    private final Callable<T> function;

    /**
     * Factory method to create new {@code RemoteFunction}s.
     *
     * @param function the function to call on the remote device
     * @return a new {@code RemoteFunction}
     */
    public static<T> RemoteFunction<T> calls( Callable<T> function ) {
        return new RemoteFunction<T>( function );
    }

    private RemoteFunction( Callable<T> function ) {
        this.function = function;
    }

    @Override
    public T call() {
        Socket socket = Sockets.newSocket( "localhost", REPServer.REP_PORT );
        Sockets.writeObjectTo( socket, function );
        SerializableObject o =
            (SerializableObject) Sockets.readObjectFrom( socket );
        return (T) o.getObject();
    }
}
