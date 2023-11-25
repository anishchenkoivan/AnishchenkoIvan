package org.example;

import org.example.controller.Controller;

import java.util.List;

public class Application {

    public final List<Controller> controllers;

    public Application(List<Controller> controllers) {
        this.controllers = controllers;
    }

    public void start() {
        for (Controller controller : controllers) {
            controller.initializeEndpoints();
        }
    }
}
