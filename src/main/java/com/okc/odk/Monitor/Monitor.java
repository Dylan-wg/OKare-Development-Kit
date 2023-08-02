package com.okc.odk.Monitor;

import com.okc.odk.Port.Port;

import java.util.HashMap;

public class Monitor {

    private String name;
    private HashMap<String, Port> Ports = new HashMap<String,Port>();

    public Monitor(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name =  name;
    }

    public void addPorts(String name,Port port){
        this.Ports.put(name,port);
    }

}
