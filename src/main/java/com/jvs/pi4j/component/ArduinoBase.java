package com.jvs.pi4j.component;

import java.io.UnsupportedEncodingException;

import com.pi4j.component.ComponentBase;

public abstract class ArduinoBase extends ComponentBase implements Arduino {

	@Override
    public void write(String data) {
        try{
            write(data.getBytes("UTF-8"));
         }
        catch(UnsupportedEncodingException e){
            throw new RuntimeException(e);
         }
    }

    @Override
    public void write(String data, Object...arguments) {
        write(String.format(data, arguments));
    }

    @Override
    public void write(char[] data) {
        for(char c : data)
            write(c);
    }

    @Override
    public void write(byte[] data) {
        for(byte b : data)
            write(b);
    }

    @Override
    public void write(char data) {
        write((byte)data);
    }
    
    @Override
    public abstract void write(byte data);
    
    @Override
    public abstract int read();

}
