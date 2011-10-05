package com.ezanmoto.rep;

import java.io.Serializable;

class SerializableObject implements Serializable {

    private final Object object;

    public static SerializableObject from( Object object ) {
        return new SerializableObject( object );
    }

    private SerializableObject( Object object ) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
