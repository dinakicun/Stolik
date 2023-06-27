package com.example.stolik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private String userPhoneNumber;
    private EditText loginEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        findViewById(R.id.textView3).setOnClickListener(v -> {
            startActivity(new Intent(this, Registration.class));
        });

        findViewById(R.id.textView2).setOnClickListener(v -> {
            startActivity(new Intent(this, GetPassword.class));
        });

        findViewById(R.id.startButton).setOnClickListener(v -> {
            String phoneNumber = loginEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Проверка пользователя в базе данных
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            Query query = usersRef.orderByChild("phoneNumber").equalTo(phoneNumber);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Пользователь существует в базе данных
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user.getPassword().equals(password)) {
                                // Правильный пароль
                                Toast.makeText(MainActivity.this, "Вход выполнен", Toast.LENGTH_SHORT).show();
                                userPhoneNumber = user.getPhoneNumber();
                                startActivity(new Intent(MainActivity.this, RestaurantsList.class));
                                finish();
                            } else {
                                // Неправильный пароль
                                Toast.makeText(MainActivity.this, "Неправильный пароль", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        // Пользователь не существует в базе данных
                        Toast.makeText(MainActivity.this, "Такого пользователя не существует", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Обработка ошибок при чтении данных из базы данных
                    Toast.makeText(MainActivity.this, "Ошибка: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
