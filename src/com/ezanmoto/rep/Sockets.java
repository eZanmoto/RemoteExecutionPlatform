package com.ezanmoto.rep;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sockets {

    private Sockets() {
    }

    public static ServerSocket newSocketServer( int port ) {
        try {
            return new ServerSocket( port );
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not create server socket on port " + port, e );
        }
    }

    public static Socket newSocket( String host, int port ) {
        try {
            return new Socket( host, port );
        } catch ( UnknownHostException e ) {
            throw new REPException(
                    "Could not find host: '" + host + "'", e );
        } catch ( IOException e ) {
            throw new REPException(
                    "Unable to bind to port " + port, e );
        }
    }

    public static Socket acceptFrom( ServerSocket server ) {
        try {
            return server.accept();
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not accept client connection", e );
        }
    }

    public static Object readObjectFrom( Socket socket ) {
        ObjectInputStream in = newObjectInputStreamFrom( socket );
        Exception exception = null;
        try {
            return in.readObject();
        } catch ( IOException e ) {
            exception = e;
        } catch ( ClassNotFoundException e ) {
            exception = e;
        }
        throw new REPException( "Could not read object from input stream",
                                exception );
    }

    public static InputStream getInputStreamFrom( Socket client ) {
        try {
            return client.getInputStream();
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not get input stream from '" + client + "'", e );
        }
    }

    public static ObjectInputStream newObjectInputStreamFrom( Socket client ) {
        try {
            final InputStream in = getInputStreamFrom( client );
            return new ObjectInputStream( in );
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not create new object input stream", e );
        }
    }

    public static void writeObjectTo( Socket client, Serializable o ) {
        final ObjectOutputStream out = newObjectOutputStreamFor( client );
        try {
            out.writeObject( o );
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not write object", e );
        }
    }

    public static OutputStream getOutputStreamFor( Socket client ) {
        try {
            return client.getOutputStream();
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not get output stream for '" + client + "'", e );
        }
    }

    public static ObjectOutputStream newObjectOutputStreamFor( Socket client ) {
        try {
            final OutputStream out = getOutputStreamFor( client );
            return new ObjectOutputStream( out );
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not create new object output stream", e );
        }
    }
}
