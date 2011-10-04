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
public enum FunctionExecutor implements REPServer, Runnable {
    INSTANCE;

    private boolean running;

    /**
     * Returns the only instance of this class.
     *
     * @return this instance
     */
    public static FunctionExecutor getInstance() {
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

    public void listenTo( ServerSocket server ) {
        while ( running ) {
            Socket client = Sockets.acceptFrom( server );
            Callable function = (Callable) Sockets.readObjectFrom( client );
            final Object result = function.call();
            Sockets.writeObjectTo( client, result );
        }
    }

    @Override
    public void stop() {
        running = false;
    }
}
