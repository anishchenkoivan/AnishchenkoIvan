package org.example.zoo.animals;

import org.example.zoo.food.Grass;

public class Horse implements LandInterface, HerbivoreInterface{
    @Override
    public void eat(Grass food) {
        System.out.println("Horse is eating grass");
    }

    @Override
    public void walk() {
        System.out.println("Horse is walking");
    }
}
