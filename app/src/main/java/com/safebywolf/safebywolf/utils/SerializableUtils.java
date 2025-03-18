package com.safebywolf.safebywolf.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableUtils {

    public static byte[] serializarObjeto(Serializable objeto) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(objeto);
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();

    }

    public static Object deserializarObjeto(byte[] datosSerializados) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datosSerializados);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object objetoDeserializado = objectInputStream.readObject();
        objectInputStream.close();
        return objetoDeserializado;
    }

}
