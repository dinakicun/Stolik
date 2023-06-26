package com.example.stolik;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import android.os.Bundle;

public class Tables extends AppCompatActivity {
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
        // Получение переданного ресторана из Intent
        Restaurant selectedRestaurant = getIntent().getParcelableExtra("restaurant");

        // Отображение названия ресторана
        TextView restaurantNameTextView = findViewById(R.id.restaurantNameTextView);
        restaurantNameTextView.setText(selectedRestaurant.getName());

        TextView restaurantDescriptionTextView = findViewById(R.id.restaurantDescriptionTextView);
        restaurantDescriptionTextView.setText(selectedRestaurant.getDescription());

        storageReference = FirebaseStorage.getInstance().getReference();

        // Отображение картинки ресторана
        ImageView restaurantImageView = findViewById(R.id.restaurantImageView);

        // Получение ссылки на изображение ресторана из Firebase Storage
        StorageReference imageRef = storageReference.child(selectedRestaurant.getImage2());

        // Получение URL-адреса изображения
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Загрузка изображения ресторана в ImageView с помощью библиотеки Picasso
            Picasso.get().load(uri).into(restaurantImageView);
        }).addOnFailureListener(exception -> {
            // Обработка ошибок при загрузке изображения
        });
    }
}