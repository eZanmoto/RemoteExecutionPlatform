package com.ezanmoto.rep;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Executes a function on a remote device.
 *
 * @author S. M. Kelleher
 */
public class RemoteFunction {

    private RemoteFunction() {
    }

    public static void main( String[] args ) {
        final int result = RemoteFunction.invoke( new Callable<Integer>() {
            @Override
            public Integer call() {
                System.out.println( "Hello, world!" );
                return 42;
            }
        } );
        System.out.println( result );
    }

    /**
     * Run {@code function} on a remote device.
     *
     * @param function the function to run on the remote device
     * @return the return value of {@code function}
     */
    public static <T> T invoke( Callable<T> function ) {
        Socket socket = Sockets.newSocket( "192.168.1.2", REPServer.REP_PORT );
        Sockets.writeObjectTo( socket, function );
        SerializableObject o =
            (SerializableObject) Sockets.readObjectFrom( socket );
        return (T) o.getObject();
    }
}
