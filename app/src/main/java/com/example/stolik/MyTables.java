package com.example.stolik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MyTables extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tables);
        findViewById(R.id.imageButton2).setOnClickListener(v -> {
            startActivity(new Intent(this, RestaurantsList.class));
        });
    }
}