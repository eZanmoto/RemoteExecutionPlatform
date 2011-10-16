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

    private static final File TEST_DIR  = new File( ""  );
    private static final File TEST_FILE = new File( TEST_DIR, "" );

    private static final String EXPECTED_RESULT = "0xDEAD_BEEF";

    private final Compiler<Callable> compiler = CallableCompiler.getInstance();

    @Test( expected = REPException.class )
    public void testAbsentFile() {
        compiler.compile( new File( "" ) );
    }

    @Test
    public void compile() {
        writeTestFile();
        Callable method = compiler.compile( TEST_FILE );
        final Object result = method.call();
        assertEquals( EXPECTED_RESULT, result );
    }

    private static void writeTestFile() {
        BufferedWriter out = null;
        try {
            final FileWriter writer  = new FileWriter( TEST_FILE );
            out = new BufferedWriter( writer );
            writeTestContentsTo( out );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        } finally {
            close( out );
        }
    }

    private static void writeTestContentsTo( BufferedWriter out )
            throws IOException {
        out.write(
            "public class TestClass implements Callable {\n"
          + "    public Object call() {\n"
          + "        return " + EXPECTED_RESULT + ";\n"
          + "    }\n"
          + "}"
        );
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
