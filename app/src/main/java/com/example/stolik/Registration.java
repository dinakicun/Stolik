package com.example.stolik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity {

    private EditText loginEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private Button registrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Инициализация полей ввода и кнопки регистрации
        loginEditText = findViewById(R.id.loginEditText);
        nameEditText = findViewById(R.id.nameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registrationButton = findViewById(R.id.button);

        // Установка обработчика события нажатия на кнопку регистрации
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение значений из полей ввода
                String phoneNumber = loginEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Создание нового объекта пользователя
                User user = new User();
                user.setPhoneNumber(phoneNumber);
                user.setName(name);
                user.setPassword(password);

                // Регистрация пользователя
                registerUser(user);
            }
        });

        findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, MainActivity.class));
            }
        });
    }

    private void registerUser(User user) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseRef.orderByChild("phoneNumber").equalTo(user.getPhoneNumber());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Пользователь уже существует
                    Toast.makeText(Registration.this, "Пользователь с таким номером телефона уже существует", Toast.LENGTH_SHORT).show();
                } else {
                    // Регистрация нового пользователя
                    String userId = databaseRef.push().getKey();
                    databaseRef.child(userId).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Регистрация успешна
                                        Toast.makeText(Registration.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Registration.this, RestaurantsList.class));
                                    } else {
                                        // Ошибка при регистрации
                                        Toast.makeText(Registration.this, "Ошибка регистрации: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при чтении из базы данных
                Toast.makeText(Registration.this, "Ошибка: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
