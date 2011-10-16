package com.ezanmoto.rep.test;

import com.ezanmoto.rep.Callable;
import com.ezanmoto.rep.CallableCompiler;
import com.ezanmoto.rep.Compiler;
import com.ezanmoto.rep.REPException;

import java.io.File;

import org.junit.Test;

public class CallableCompilerTest {

    private static final File TEST_DIR  = new File( ""  );
    private static final File TEST_FILE = new File( TEST_DIR, "" );

    private final Compiler<Callable> compiler = CallableCompiler.getInstance();

    @Test( expected = REPException.class )
    public void testAbsentFile() {
        compiler.compile( new File( "" ) );
    }
}
