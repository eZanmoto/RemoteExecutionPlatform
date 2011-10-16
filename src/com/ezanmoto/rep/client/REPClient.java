package com.ezanmoto.rep.client;

import java.io.File;

public interface REPClient<T> {

    public T call( File f );
}
