package com.ezanmoto.rep;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Executes received functions.
 *
 * @author S. M. Kelleher
 */
public enum StandardREPServer implements REPServer, Runnable {
    INSTANCE;

    private boolean running;

    /**
     * Start a basic {@code REPServer}.
     *
     * @param args the arguments to this {@code REPServer}
     */
    public static void main( String[] args ) {
        getInstance().start();
    }

    /**
     * Returns the only instance of this class.
     *
     * @return this instance
     */
    public static StandardREPServer getInstance() {
        return INSTANCE;
    }

    @Override
    public void start() {
        new Thread( getInstance() ).start();
    }

    @Override
    public void run() {
        final ServerSocket server = Sockets.newSocketServer( REP_PORT );
        running = true;
        listenTo( server );
    }

    private void listenTo( ServerSocket server ) {
        while ( running ) {
            Socket client = Sockets.acceptFrom( server );
            Callable function = (Callable) Sockets.readObjectFrom( client );
            final Object result = function.call();
            Sockets.writeObjectTo( client, SerializableObject.from( result ) );

            // Accept socket
            // Receive file name (sans extention) and output file to that file
            // Compile file
            // Use reflection and file name to get class
            // Execute Callable method .call()
            // Return result (don't need to change Serializable implementation)
        }
    }

    @Override
    public void stop() {
        running = false;
    }
}
