package com.ezanmoto.rep.client;

import com.ezanmoto.rep.REPException;
import com.ezanmoto.rep.SerializableObject;
import com.ezanmoto.rep.Sockets;
import com.ezanmoto.rep.server.REPServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.io.Files;

public class StandardREPClient implements REPClient {

    public static void main( String[] args ) {
        newInstance().call( new File( "build/junit/Test.java" ) );
    }

    public static StandardREPClient newInstance() {
        return new StandardREPClient();
    }

    private StandardREPClient() {
    }

    @Override
    public Object call( File f ) {
        final Socket socket = Sockets.newSocket( findHost(), REPServer.PORT );
        final PrintWriter out = Sockets.getPrintWriterFor( socket );
        final ObjectInputStream in = Sockets.newObjectInputStreamFrom( socket );
        final String contents = readContentsOf( f ).trim();
        try {
            out.write( f.getName() + "\n" + contents + "\n<EOF>\n" );
            out.flush();
            System.out.println(
                ( (SerializableObject) in.readObject() ).getObject() );
            return null;
        } catch ( IOException e ) {
            throw new REPException( e );
        } catch ( ClassNotFoundException e ) {
            throw new REPException( e );
        }
    }

    private String readContentsOf( File f ) {
        try {
            final FileReader fr = new FileReader( f );
            final BufferedReader in = new BufferedReader( fr );
            final StringBuilder builder = new StringBuilder();
            String line = "";
            while ( ( line = in.readLine() ) != null ) {
                builder.append( line );
                builder.append( '\n' );
            }
            return builder.toString();
        } catch ( IOException e ) {
            throw new REPException( e );
        }
    }

    private String findHost() {
        return "localhost";
    }
}
