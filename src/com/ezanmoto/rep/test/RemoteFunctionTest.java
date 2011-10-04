package com.ezanmoto.rep.test;

import com.ezanmoto.rep.Callable;
import com.ezanmoto.rep.FunctionExecutor;
import com.ezanmoto.rep.RemoteFunction;
import com.ezanmoto.rep.REPServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RemoteFunctionTest {

    private final REPServer server = FunctionExecutor.getInstance();

    @Before
    public void startServer() {
        server.start();
    }

    @Test
    public void sumToN() {
        final int expected = 42;
        RemoteFunction f = RemoteFunction.calls( new Callable<Integer>() {
            public Integer call() {
                return expected;
            }
        } );
        assertEquals( Integer.valueOf( expected ), f.call() );
    }

    @After
    public void stopServer() {
        server.stop();
    }
}
