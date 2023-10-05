package org.example.zoo.animals;

import org.example.zoo.food.Beef;
import org.example.zoo.food.Meat;

public class Tiger implements CarnivoreInterface<Beef>, LandInterface{
    @Override
    public void eat(Beef food) {
        System.out.println("Tiger is eating beef");
    }

    @Override
    public void walk() {
        System.out.println("Tiger is walking");
    }
}
