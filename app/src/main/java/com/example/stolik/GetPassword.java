package com.example.stolik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class GetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);
        findViewById(R.id.textView3).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}