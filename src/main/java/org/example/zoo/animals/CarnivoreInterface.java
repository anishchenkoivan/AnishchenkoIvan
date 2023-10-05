package org.example.zoo.animals;

import org.example.zoo.food.Meat;

public interface CarnivoreInterface<T extends Meat> {
    void eat(T food);
}
