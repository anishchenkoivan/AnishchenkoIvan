package org.example;

import org.example.zoo.animals.*;
import org.example.zoo.food.*;

public class Main {
    public static void main(String[] args) {
        Grass grass = new Grass();
        Beef beef = new Beef();
        Fish fish = new Fish();
        Meat meat = new Meat();

        Camel camel = new Camel();
        Eagle eagle = new Eagle();
        Horse horse = new Horse();
        Tiger tiger = new Tiger();
        Dolphin dolphin = new Dolphin();

        camel.walk();
        eagle.fly();
        horse.walk();
        tiger.walk();
        dolphin.swim();

        camel.eat(grass);
        eagle.eat(fish);
        eagle.eat(meat);
        dolphin.eat(fish);
        tiger.eat(beef);
    }
}