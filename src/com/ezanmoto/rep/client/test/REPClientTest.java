package com.ezanmoto.rep.client.test;

import com.ezanmoto.rep.client.REPClient;
import com.ezanmoto.rep.client.StandardREPClient;
import com.ezanmoto.rep.server.REPServer;
import com.ezanmoto.rep.server.StandardREPServer;
import com.ezanmoto.rep.test.CallableCompilerTest;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

@org.junit.Ignore( "server isn't starting properly" )
public class REPClientTest {

    private static final File TEST_DIR = new File( "build/junit/src" );

    private static final REPClient<Integer> CLIENT =
        StandardREPClient.newInstance();

    private REPServer server = StandardREPServer.newInstance();

    @Before
    public void startServer() {
        server.start();
    }

    @Test
    public void call() {
        final File source = new File( TEST_DIR, "Sum.java" );
        CallableCompilerTest.writeTestFile( source,
            "import com.ezanmoto.rep.Callable;\n\n"
          + "public class Sum implements Callable<Integer> {\n"
          + "   public Object call() {\n"
          + "       return new Integer( 1 + 1 );\n"
          + "   }\n"
          + "}"
        );
        final Integer result = CLIENT.call( source );
        assertEquals( new Integer( 2 ), result );
    }

    @After
    public void stopServer() {
        server.stop();
    }
}
