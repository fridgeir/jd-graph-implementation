package com.example.network;

import java.util.*;

public class Network implements AlertNetwork {

    private final Map<String, AlertSystem> systems = new HashMap<>();

    public void addService(String service) {
        AlertSystem alertSystem = new AlertSystem(service);
        systems.put(service, alertSystem);
    }

    public void addDependency(String fromService, String toService) {
        AlertSystem source = systems.get(fromService);
        AlertSystem target = systems.get(toService);

        source.addConnection(target);
    }

    public List<String> getDependencies(String service) {
        AlertSystem alertSystem = systems.get(service);
        return alertSystem.getConnections().stream()
                .map(AlertSystem::getName)
                .toList();
    }

    public List<String> getServices() {
        return systems.keySet().stream().toList();
    }

    public List<String> findAlertPropagationPath(String source, String target) {
        AlertSystem from = systems.get(source);
        AlertSystem to = systems.get(target);
        if (from.equals(to)) {
            return List.of(source);
        }

        Map<AlertSystem, Integer> visited = new HashMap<>();
        Map<Integer, Set<AlertSystem>> distances = new HashMap<>();

        HashSet<AlertSystem> init = new HashSet<>();
        init.add(from);
        visited.put(from, 0);
        distances.put(0, init);


        int distance = 0;
        boolean finished = false;
        do {
            Set<AlertSystem> currentSystems = distances.get(distance);
            distance += 1;
            for (AlertSystem aS : currentSystems) {
                Set<AlertSystem> distanceSet = new HashSet<>();
                for (AlertSystem neighbour : aS.getConnections()) {
                    if (visited.containsKey(neighbour)) {
                        continue;
                    }
                    visited.put(neighbour, distance);
                    distanceSet.add(neighbour);
                    if (neighbour == to) {
                        finished = true;
                        break;
                    }
                }
                distances.put(distance, distanceSet);
                if (finished) {
                    break;
                }
            }
        } while (!finished);

        String[] path = new String[distance + 1];
        path[distance] = target;

        AlertSystem seekPath = to;
        for (int i = distance - 1; i >= 0; i--) {
            Set<AlertSystem> search = distances.get(i);
            for(AlertSystem s : search){
                if (s.getConnections().contains(seekPath)) {
                    path[i] = s.getName();
                    seekPath = s;
                    break;
                }
            }
        }

        return List.of(path);
    }

    public List<String> getAffectedServices(String source) {
        return List.of();
    }
}
