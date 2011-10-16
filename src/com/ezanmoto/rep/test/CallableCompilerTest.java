package com.ezanmoto.rep.test;

import com.ezanmoto.rep.Callable;
import com.ezanmoto.rep.CallableCompiler;
import com.ezanmoto.rep.Compiler;
import com.ezanmoto.rep.REPException;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CallableCompilerTest {

    private static final File TEST_DIR  = new File( "test" );
    private static final File TEST_FILE =
        new File( TEST_DIR, "TestClass.java" );

    private static final String EXPECTED_RESULT = "0xDEAD_BEEF";

    private final Compiler<Callable> compiler = CallableCompiler.getInstance();

    @Test( expected = REPException.class )
    public void testAbsentFile() {
        compiler.compile( new File( "" ) );
    }

    @Test
    public void compile() {
        writeTestFile(
            "import com.ezanmoto.rep.Callable;\n\n"
          + "public class TestClass implements Callable {\n"
          + "    public Object call() {\n"
          + "        return \"" + EXPECTED_RESULT + "\";\n"
          + "    }\n"
          + "}"
        );
        Callable method = compiler.compile( TEST_FILE );
        final Object result = method.call();
        assertEquals( EXPECTED_RESULT, result );
    }

    private static void writeTestFile( String contents ) {
        BufferedWriter out = null;
        try {
            TEST_FILE.getParentFile().mkdirs();
            final FileWriter writer  = new FileWriter( TEST_FILE );
            out = new BufferedWriter( writer );
            out.write( contents );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        } finally {
            close( out );
        }
    }

    @Test
    public void compileSum() {
        final Integer expected = 2;
        writeTestFile(
            "import com.ezanmoto.rep.Callable;\n\n"
          + "public class TestClass implements Callable {\n"
          + "    public Object call() {\n"
          + "        return new Integer( 1 + 1 );\n"
          + "    }\n"
          + "}"
        );
        Callable method = compiler.compile( TEST_FILE );
        final Object result = method.call();
        assertEquals( expected, result );
    }

    private static void close( Closeable c ) {
        try {
            if ( c != null ) {
                c.close();
            }
        } catch ( IOException e ) {
            // TODO log exception
        }
    }
}
