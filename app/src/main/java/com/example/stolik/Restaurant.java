package com.example.stolik;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {
    private String name;
    private String description;
    private int seatingCapacity;
    private String image;
    private String image2;

    public Restaurant() {
        // Обязательный пустой конструктор для Firebase
    }

    public Restaurant(String name, String description, int seatingCapacity, String image, String image2) {
        this.name = name;
        this.description = description;
        this.seatingCapacity = seatingCapacity;
        this.image = image;
        this.image2 = image2;
    }

    protected Restaurant(Parcel in) {
        name = in.readString();
        description = in.readString();
        seatingCapacity = in.readInt();
        image = in.readString();
        image2 = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

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

    public String getImage2() {
        return image2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(seatingCapacity);
        dest.writeString(image);
        dest.writeString(image2);
    }
}