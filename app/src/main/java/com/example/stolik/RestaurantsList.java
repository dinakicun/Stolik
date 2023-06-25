package com.example.stolik;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class RestaurantsList extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_list);

        databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants");
        storageReference = FirebaseStorage.getInstance().getReference();

        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Ничего не делаем перед изменением текста
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Вызываем метод для фильтрации списка ресторанов при изменении текста в поисковой строке
                filterRestaurants(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Ничего не делаем после изменения текста
            }
        });

        // Получаем данные из базы данных Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Получаем ресторан из базы данных
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    if (restaurant != null) {
                        // Создаем представление ресторана и добавляем его в макет
                        addRestaurantToLayout(restaurant);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при чтении данных из базы данных Firebase
            }
        });
    }

    private void addRestaurantToLayout(Restaurant restaurant) {
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Проверяем, есть ли уже такой ресторан в GridLayout
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View childView = gridLayout.getChildAt(i);
            TextView textView = childView.findViewById(R.id.textView6);

            if (textView.getText().toString().equals(restaurant.getName())) {
                // Ресторан уже существует, пропускаем добавление
                return;
            }
        }

        // Создаем представление ресторана из макета
        View restaurantView = LayoutInflater.from(this).inflate(R.layout.restaurant_item, null);

        // Находим элементы в представлении ресторана
        ImageView imageView = restaurantView.findViewById(R.id.imageView4);
        TextView textView = restaurantView.findViewById(R.id.textView6);

        // Заполняем элементы данными ресторана
        textView.setText(restaurant.getName());

        // Получение ссылки на изображение ресторана из Firebase Storage
        StorageReference imageRef = storageReference.child(restaurant.getImage());

        // Получение URL-адреса изображения
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Загрузка изображения ресторана в ImageView с помощью библиотеки Picasso
            Picasso.get().load(uri).into(imageView);
        }).addOnFailureListener(exception -> {
            // Обработка ошибок при загрузке изображения
        });

        // Добавляем представление ресторана в GridLayout
        gridLayout.addView(restaurantView);
    }




    private void filterRestaurants(String searchText) {
        // Очищаем GridLayout от предыдущих представлений ресторанов
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();

        // Получаем данные из базы данных Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Получаем ресторан из базы данных
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    if (restaurant != null) {
                        // Проверяем, содержит ли название ресторана введенный текст
                        if (restaurant.getName().toLowerCase().contains(searchText.toLowerCase())) {
                            // Создаем представление ресторана и добавляем его в макет
                            addRestaurantToLayout(restaurant);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при чтении данных из базы данных Firebase
            }
        });
    }
}
