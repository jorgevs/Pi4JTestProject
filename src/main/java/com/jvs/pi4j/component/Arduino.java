package com.jvs.pi4j.component;

import com.pi4j.component.Component;

public interface Arduino extends Component {

    void write(String data);
    void write(String data, Object...arguments);
    void write(char[] data);
    void write(byte[] data);
    void write(char data);
    void write(byte data);
    
    int read();
}
