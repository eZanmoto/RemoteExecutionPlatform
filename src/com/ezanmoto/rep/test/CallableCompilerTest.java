package com.ezanmoto.rep.test;

import com.ezanmoto.rep.Callable;
import com.ezanmoto.rep.CallableCompiler;
import com.ezanmoto.rep.Compiler;
import com.ezanmoto.rep.REPException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.google.common.io.Files;

public class CallableCompilerTest {

    private static final File TEST_DIR  = new File( "build/junit/src" );
    private static final File TEST_FILE =
        new File( TEST_DIR, "TestClass.java" );

    private static final String EXPECTED_RESULT = "0xDEAD_BEEF";

    private final Compiler<Callable> compiler = CallableCompiler.getInstance();

    @Test( expected = NullPointerException.class )
    public void testNull() {
        compiler.compile( null );
    }

    @Test( expected = REPException.class )
    public void testAbsentFile() {
        compiler.compile( new File( "" ) );
    }

    @Test
    public void compile() {
        final String filename = "TestClass";
        final File source = fileNamed( filename );
        writeTestFile( source,
            "import com.ezanmoto.rep.Callable;\n\n"
          + "public class " + filename + " implements Callable {\n"
          + "    public Object call() {\n"
          + "        return \"" + EXPECTED_RESULT + "\";\n"
          + "    }\n"
          + "}"
        );
        Callable method = compiler.compile( source );
        final Object result = method.call();
        assertEquals( EXPECTED_RESULT, result );
    }

    private static File fileNamed( String name ) {
        return new File( TEST_DIR, name + ".java" );
    }

    public static void writeTestFile( File testFile, String contents ) {
        try {
            testFile.getParentFile().mkdirs();
            Files.write( contents, testFile, Charset.defaultCharset() );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    @Test
    public void compileSum() {
        final Integer expected = 2;
        final String filename = "Add";
        final File source = fileNamed( filename );
        writeTestFile( source,
            "import com.ezanmoto.rep.Callable;\n\n"
          + "public class " + filename + " implements Callable {\n"
          + "    public Object call() {\n"
          + "        return new Integer( 1 + 1 );\n"
          + "    }\n"
          + "}"
        );
        Callable method = compiler.compile( source );
        final Object result = method.call();
        assertEquals( expected, result );
    }
}
