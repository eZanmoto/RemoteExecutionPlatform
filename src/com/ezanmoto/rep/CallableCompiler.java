package com.ezanmoto.rep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.google.common.io.Files;

public enum CallableCompiler implements Compiler<Callable> {
    INSTANCE;

    private static final File TEMP_DIR    = Files.createTempDir();
    private static final File ERROR_LOG   = new File( TEMP_DIR, "stderr" );
    private static final File CLASSES_DIR = new File( TEMP_DIR, "classes" );
    private static final String CLASSPATH = "src";

    private static final String CP_FLAG   = "-classpath";
    private static final String DEST_FLAG = "-d";

    public static CallableCompiler getInstance() {
        return INSTANCE;
    }

    public Callable compile( File source ) {
        if ( source.exists() ) {
            javac( DEST_FLAG, CLASSES_DIR.toString(),
                   CP_FLAG, CLASSPATH,
                   source.toString() );
            final String name = removeExtensionFrom( source.getName() );
            Class c = loadClass( Callable.class, name );
            return newInstanceOf( c );
        } else {
            throw new REPException( "'" + source + "' does not exist" );
        }
    }

    private static void javac( String... command ) {
        final PrintWriter out = newPrintWriter( ERROR_LOG );
        final int exitCode = com.sun.tools.javac.Main.compile( command, out );
        if ( exitCode != 0 ) {
            throw new REPException( "Error compiling, check '"
                + ERROR_LOG.getAbsolutePath() + "' for details" );
        }
    }

    private static PrintWriter newPrintWriter( File f ) {
        try {
            return new PrintWriter( f );
        } catch ( FileNotFoundException e ) {
            throw new REPException( e );
        }
    }

    private static String removeExtensionFrom( String s ) {
        return s.substring( 0, s.lastIndexOf( '.' ) );
    }

    private static Class loadClass( Class parent, String name ) {
        ClassLoader parentLoader = parent.getClassLoader();
        URLClassLoader childLoader = new URLClassLoader(
            new URL[] { urlFrom( CLASSES_DIR ) }, parentLoader
        );
        try {
            return childLoader.loadClass( name );
        } catch ( ClassNotFoundException e ) {
            throw new REPException( e );
        }
    }

    private static URL urlFrom( File f ) {
        try {
            return f.toURL();
        } catch ( MalformedURLException e ) {
            throw new REPException( e );
        }
    }

    private static Callable newInstanceOf( Class c ) {
        Exception exception = null;
        try {
            return (Callable) c.newInstance();
        } catch ( InstantiationException e ) {
            exception = e;
        } catch( IllegalAccessException e ) {
            exception = e;
        }
        throw new REPException( exception );
    }
}
