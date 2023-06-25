package com.example.stolik;

public class Restaurant {
    private String name;
    private String description;
    private int seatingCapacity;
    private String image;

    public Restaurant() {
        // Обязательный пустой конструктор для Firebase
    }

    public Restaurant(String name, String description, int seatingCapacity, String image) {
        this.name = name;
        this.description = description;
        this.seatingCapacity = seatingCapacity;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public String getImage() {
        return image;
    }
}

