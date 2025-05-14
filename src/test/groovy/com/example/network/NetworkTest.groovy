package com.example.network

import spock.lang.Specification
import spock.lang.Subject

class NetworkTest extends Specification {

    @Subject
    Network network = new Network()

    def "Test adding new services"() {
        when:
        services.forEach { s -> network.addService(s) }

        then:
        network.getServices() == services

        where:
        services                       | _
        ["service"]                    | _
        ["a", "b", "c", "d", "e", "f"] | _
    }

    def "Test add dependencies"() {
        given:
        network.addService("a")
        network.addService("b")
        network.addService("c")
        network.addDependency("a", "b")
        network.addDependency("a", "c")
        network.addDependency("b", "c")

        expect:
        network.getDependencies("a") == ["b", "c"]
        network.getDependencies("b") == ["c"]
        network.getDependencies("c") == []
    }

    def "Check shortest path for target being source"() {
        given:
        network.addService("a")

        expect:
        network.findAlertPropagationPath("a", "a") == ["a"]
    }

    def "Check shortest path when neighbours"() {
        given:
        network.addService("a")
        network.addService("b")
        network.addDependency("a", "b")

        expect:
        network.findAlertPropagationPath("a", "b") == ["a", "b"]
    }

    def "Check shortest path for more complex graph"(){
        given:
        network.addService("a")
        network.addService("b")
        network.addService("c")
        network.addService("d")
        network.addService("e")
        network.addService("f")
        network.addDependency("a", "b")
        network.addDependency("b", "d")
        network.addDependency("d", "e")
        network.addDependency("a", "c")
        network.addDependency("c", "e")
        network.addDependency("e", "f")

        expect:
        network.findAlertPropagationPath("a", "f") == ["a", "c", "e", "f"]
        network.findAlertPropagationPath("a", "d") == ["a", "b", "d"]
        network.findAlertPropagationPath("b", "f") == ["b", "d", "e", "f"]
    }

}
