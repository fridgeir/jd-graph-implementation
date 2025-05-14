package com.example.network;

import java.util.ArrayList;
import java.util.List;

// Graph node
public class AlertSystem {

    private final String name;

    private final List<AlertSystem> connections = new ArrayList<>();

    public AlertSystem(String service) {
        name = service;
    }

    public void addConnection(AlertSystem target) {
        connections.add(target);
    }

    public List<AlertSystem> getConnections() {
        return connections;
    }

    public String getName() {
        return name;
    }
}
