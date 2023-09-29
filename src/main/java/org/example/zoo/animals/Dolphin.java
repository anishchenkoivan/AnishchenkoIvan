package org.example.zoo.animals;

import org.example.zoo.food.Fish;

public class Dolphin implements SwimmingInterface, CarnivoreInterface<Fish>{
    @Override
    public void eat(Fish food) {
        System.out.println("Dolphin is eating fish");

    }

    @Override
    public void swim() {
        System.out.println("Dolphin is swimming");
    }
}
