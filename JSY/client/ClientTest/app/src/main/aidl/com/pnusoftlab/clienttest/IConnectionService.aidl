// IConnectionService.aidl
package com.pnusoftlab.clienttest;

// Declare any non-default types here with import statements

interface IConnectionService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int getStatus();
    void setSocket(String ip);
    void connect();
    void disconnect();
    void send(String buffer);
    void receive();
}