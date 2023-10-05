package org.example.zoo.animals;

import org.example.zoo.food.Meat;

public class Eagle implements CarnivoreInterface<Meat>, FlyingInterface {
    @Override
    public void eat(Meat food) {
        System.out.println("Eagle is eating meat");
    }

    @Override
    public void fly() {
        System.out.println("Eagle is flying");
    }
}
