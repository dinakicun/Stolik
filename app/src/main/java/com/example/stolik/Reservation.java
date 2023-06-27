package com.example.stolik;

public class Reservation {
    private String restaurantName;
    private String tableName;
    private String date;
    private String time;

    private String endTime;
    private String user;

    public Reservation() {
        // Обязательный пустой конструктор для Firebase
    }

    public Reservation(String restaurantName, String tableName, String date, String time, String user, String endTime) {
        this.restaurantName = restaurantName;
        this.tableName = tableName;
        this.date = date;
        this.endTime = endTime;
        this.time = time;
        this.user = user;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
