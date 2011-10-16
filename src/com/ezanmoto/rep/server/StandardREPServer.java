package com.ezanmoto.rep.server;

import com.ezanmoto.rep.Callable;
import com.ezanmoto.rep.CallableCompiler;
import com.ezanmoto.rep.REPException;
import com.ezanmoto.rep.SerializableObject;
import com.ezanmoto.rep.Sockets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;

/**
 * Executes received functions.
 *
 * @author S. M. Kelleher
 */
public class StandardREPServer implements REPServer, Runnable {

    private static final File TEMP_DIR = Files.createTempDir();

    private boolean running;

    /**
     * Start a basic {@code REPServer}.
     *
     * @param args the arguments to this {@code REPServer}
     */
    public static void main( String[] args ) {
        newInstance().start();
    }

    /**
     * Create a new {@code StandardREPServer}.
     *
     * @return a new {@code StandardREPServer}
     */
    public static StandardREPServer newInstance() {
        return new StandardREPServer();
    }

    private StandardREPServer() {
    }

    @Override
    public void start() {
        new Thread( this ).start();
    }

    @Override
    public void run() {
        final ServerSocket server = Sockets.newSocketServer( PORT );
        running = true;
        listenTo( server );
    }

    private void listenTo( ServerSocket server ) {
        while ( running ) {
            final Socket client = Sockets.acceptFrom( server );
            final InputStream in = Sockets.getInputStreamFrom( client );
            final String filename = readStringFrom( in );
            final String contents = readStringFrom( in );
            final File f = new File( TEMP_DIR, filename );
            writeFile( f, contents );
            Callable method = CallableCompiler.getInstance().compile( f );
            final Object result = method.call();
            Sockets.writeObjectTo( client, SerializableObject.from( result ) );
        }
    }

    private static String readStringFrom( InputStream is ) {
        try {
            final InputStreamReader isr = new InputStreamReader( is );
            final BufferedReader in = new BufferedReader( isr );
            final StringBuilder builder = new StringBuilder();
            String line;
            while ( ( line = in.readLine() ) != null ) {
                builder.append( line );
            }
            return builder.toString();
        } catch ( IOException e ) {
            throw new REPException( e );
        }
    }

    private static void writeFile( File file, String contents ) {
        try {
            FileWriter out = new FileWriter( file );
            out.write( contents );
        } catch ( IOException e ) {
            throw new REPException( e );
        }
    }

    @Override
    public void stop() {
        running = false;
    }
}
