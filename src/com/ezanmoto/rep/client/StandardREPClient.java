package com.ezanmoto.rep.client;

import com.ezanmoto.rep.REPException;
import com.ezanmoto.rep.Sockets;
import com.ezanmoto.rep.server.REPServer;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.io.Files;

public class StandardREPClient implements REPClient {

    public static StandardREPClient newInstance() {
        return new StandardREPClient();
    }

    private StandardREPClient() {
    }

    @Override
    public Object call( File f ) {
        final Socket socket = Sockets.newSocket( findHost(), REPServer.PORT );
        final OutputStream out = Sockets.getOutputStreamFor( socket );
        final ObjectInputStream in = Sockets.newObjectInputStreamFrom( socket );
        /*
        try {
            List<String> lines = Files.readLines( f, Charset.defaultCharset() );
            for ( String line : lines ) {
                for ( char c : line.toCharArray() ) {
                    out.write( c );
                }
            }
            out.flush();
            return in.readObject();
        } catch ( IOException e ) {
            throw new REPException( e );
        } catch ( ClassNotFoundException e ) {
            throw new REPException( e );
        }
        */
        return null;
    }

    private String findHost() {
        return "localhost";
    }
}
