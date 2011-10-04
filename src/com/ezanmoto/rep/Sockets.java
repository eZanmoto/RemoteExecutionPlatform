package com.ezanmoto.rep;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

class Sockets {

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
        InputStream is = Sockets.getInputStreamFrom( socket );
        ObjectInputStream in = newObjectInputStreamFrom( is );
        try {
            return in.readObject();
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not read object from input stream", e );
        } catch ( ClassNotFoundException e ) {
            throw new REPException(
                    "Could not read object from input stream", e );
        }
    }

    private static InputStream getInputStreamFrom( Socket client ) {
        try {
            return client.getInputStream();
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not get input stream from '" + client + "'", e );
        }
    }

    private static ObjectInputStream newObjectInputStreamFrom(
            InputStream in ) {
        try {
            return new ObjectInputStream( in );
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not create new object input stream", e );
        }
    }

    public static void writeObjectTo( Socket client, Object o ) {
        final OutputStream os = getOutputStreamFor( client );
        final ObjectOutputStream out = newObjectOutputStreamFor( os );
        try {
            out.writeObject( o );
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not write object", e );
        }
    }

    private static OutputStream getOutputStreamFor( Socket client ) {
        try {
            return client.getOutputStream();
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not get output stream for '" + client + "'", e );
        }
    }

    private static ObjectOutputStream newObjectOutputStreamFor(
            OutputStream out ) {
        try {
            return new ObjectOutputStream( out );
        } catch ( IOException e ) {
            throw new REPException(
                    "Could not create new object output stream", e );
        }
    }
}
