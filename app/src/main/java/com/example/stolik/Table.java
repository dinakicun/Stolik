package com.example.stolik;

public class Table {
    private String name;
    private int count;
    private String type;
    private String restaurantKey;

    public Table() {
        // Обязательный пустой конструктор для Firebase
    }

    public Table(String name, int count, String type, String restaurantKey) {
        this.name = name;
        this.count = count;
        this.type = type;
        this.restaurantKey = restaurantKey;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public String getType() {
        return type;
    }

    public String getRestaurantKey() {
        return restaurantKey;
    }
}

