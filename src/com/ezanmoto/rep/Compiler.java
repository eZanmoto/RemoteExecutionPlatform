package com.ezanmoto.rep;

import java.io.File;

public interface Compiler<T> {

    public T compile( File source );
}
