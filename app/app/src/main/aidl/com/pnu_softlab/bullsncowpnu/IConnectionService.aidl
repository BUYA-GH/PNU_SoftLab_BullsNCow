// IConnectionService.aidl
package com.pnu_softlab.bullsncowpnu;

// Declare any non-default types here with import statements

interface IConnectionService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int getStatus();
    void setSocket(String ip);
    void connect(String name);
    void disconnect();
    void send(String buffer);
    void receive();
}