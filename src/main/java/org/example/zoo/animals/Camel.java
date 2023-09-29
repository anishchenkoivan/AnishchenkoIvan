package org.example.zoo.animals;

import org.example.zoo.food.Grass;

public class Camel implements LandInterface, HerbivoreInterface {
    @Override
    public void eat(Grass food) {
        System.out.println("Camel is eating grass");
    }

    @Override
    public void walk() {
        System.out.println("Camel is walking");
    }
}
