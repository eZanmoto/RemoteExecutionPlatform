package com.ezanmoto.rep.server;

import com.ezanmoto.rep.Callable;
import com.ezanmoto.rep.CallableCompiler;
import com.ezanmoto.rep.REPException;
import com.ezanmoto.rep.SerializableObject;
import com.ezanmoto.rep.Sockets;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes received functions.
 *
 * @author S. M. Kelleher
 */
public class StandardREPServer implements REPServer, Runnable {

    private static final File TEMP_DIR = CallableCompiler.createTempDir();

    private boolean running;

    public static void main( String[] args ) {
        newInstance().start();
    }

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
        showSplash();
        final ServerSocket server = Sockets.newSocketServer( PORT );
        running = true;
        listenTo( server );
    }

    private void showSplash() {
        System.out.println(
            "Standard REP Server\n"
          + "written by Sean Kelleher\n\n"
          + "[+] Temporary directory located at: " + TEMP_DIR + "\n"
          + "[!] WARNING: Security Manager is not configured\n"
        );
    }

    private void listenTo( ServerSocket server ) {
        while ( running ) {
            final Socket client = Sockets.acceptFrom( server );
            System.out.println( "Received connection from "
                              + client.getInetAddress() );
            final BufferedReader in = Sockets.getBufferedReaderFrom( client );
            final ObjectOutputStream out =
                Sockets.newObjectOutputStreamFor( client );
            final String input = readStringFrom( in );
            final String filename = firstLineOf( input );
            final String contents = lastLinesOf( input );
            final File f = new File( TEMP_DIR, filename );
            System.out.println( "Writing class file '" + f + "'" );
            writeContentsToFile( contents, f );
            System.out.println( "Wrote class file '" + f + "'" );
            Callable method = CallableCompiler.getInstance().compile( f );
            System.out.println( "Got instance of class '" + f + "'" );
            final Object result = method.call();
            System.out.println( "Invoked '" + f + ".call()'" );
            Sockets.writeObjectTo( out, SerializableObject.from( result ) );
            System.out.println( "Sent result, '" + result + "'" );
        }
    }

    private static String readStringFrom( BufferedReader in ) {
        try {
            final StringBuilder builder = new StringBuilder();
            String line;
            while ( ( line = in.readLine() ) != null
                    && ! line.equals( "<EOF>" ) ) {
                builder.append( line );
                builder.append( '\n' );
            }
            return builder.toString();
        } catch ( IOException e ) {
            throw new REPException( e );
        }
    }

    private static String firstLineOf( String s ) {
        final int newlineIndex = s.indexOf( "\n" );
        return s.substring( 0, newlineIndex );
    }

    private static String lastLinesOf( String s ) {
        final int newlineIndex = s.indexOf( "\n" );
        return s.substring( newlineIndex + 1 );
    }

    private static void writeContentsToFile( String contents, File file ) {
        try {
            final PrintWriter out = new PrintWriter( file );
            out.write( contents );
            out.flush();
        } catch ( IOException e ) {
            throw new REPException( e );
        }
    }

    @Override
    public void stop() {
        running = false;
    }
}
