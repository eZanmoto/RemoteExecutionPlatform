package com.ezanmoto.rep;

import java.io.File;

public enum CallableCompiler implements Compiler<Callable> {
    INSTANCE;

    public static CallableCompiler getInstance() {
        return INSTANCE;
    }

    public Callable compile( File source ) {
        throw new REPException( "" );
    }
}
