package com.ezanmoto.rep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public enum CallableCompiler implements Compiler<Callable> {
    INSTANCE;

    private static final File TEMP_DIR    = createTempDir();
    private static final File ERROR_LOG   = createNewFile( TEMP_DIR, "stderr" );
    private static final File CLASSES_DIR = createNewDir( TEMP_DIR, "classes" );

    private static final String CLASSPATH =
        System.getProperty( "user.dir" ) + "/" + "REPServer.jar"
            + File.pathSeparator + CLASSES_DIR;

    private static final String CP_FLAG   = "-classpath";
    private static final String DEST_FLAG = "-d";

    public static CallableCompiler getInstance() {
        return INSTANCE;
    }

    public static File createTempDir() {
        try {
            final File dir = File.createTempFile( "REP", ".d" );
            delete( dir );
            if ( ! dir.mkdir() ) {
                throw new REPException( "Couldn't create temporary directory" );
            }
            return dir;
        } catch ( IOException e ) {
            throw new REPException( e );
        }
    }

    private static void delete( File f ) {
        if ( ! f.delete() ) {
            throw new REPException( "Could not delete file '" + f + "'" );
        }
    }

    public static File createNewFile( File dir, String filename ) {
        try {
            final File f = new File( dir, filename );
            if ( ! f.createNewFile() ) {
                throw new REPException( "File '" + f + "' already exists" );
            }
            return f;
        } catch ( IOException e ) {
            throw new REPException( e );
        }
    }

    public static File createNewDir( File dir, String name ) {
        final File f = new File( dir, name );
        if ( ! f.mkdir() ) {
            throw new REPException(
                "Could not create directory '" + name + "'" );
        } else {
            return f;
        }
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
        out.write( "javac\n" );
        for ( String c : command ) {
            out.write( '\t' );
            out.write( c );
            out.write( "\n\n" );
        }
        int exitCode;
        try {
            exitCode = com.sun.tools.javac.Main.compile( command, out );
        } catch ( Exception e ) {
            throw new REPException( e );
        }
        if ( exitCode != 0 ) {
            throw new REPException( "Error compiling, return code " + exitCode
                + ", check '" + ERROR_LOG.getAbsolutePath() + "' for details; "
                + joinWith( " ", command ) + "\n"
                + System.getProperty( "user.dir" ) );
        }
    }

    private static String joinWith( String separator, String... ss ) {
        StringBuilder builder = new StringBuilder();
        String sep = "";
        for ( String s : ss ) {
            builder.append( sep ).append( s );
            sep = separator;
        }
        return builder.toString();
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
