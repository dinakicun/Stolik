package com.example.stolik;

import com.example.stolik.Reservation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Date;
import java.text.ParseException;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Tables extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    private StorageReference storageReference;

    private Button dateButton;
    private Button timeButton;
    private Calendar selectedDate;
    private Calendar selectedTime;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private String userPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        findViewById(R.id.imageButton4).setOnClickListener(v -> {
            startActivity(new Intent(this, MyTables.class));
        });
        findViewById(R.id.imageButton2).setOnClickListener(v -> {
            startActivity(new Intent(this, RestaurantsList.class));
        });
        // Получение переданного ресторана из Intent
        Restaurant selectedRestaurant = getIntent().getParcelableExtra("restaurant");

        userPhoneNumber = getIntent().getStringExtra("userPhoneNumber"); // Сохранение значения в userPhoneNumber

        // Отображение названия ресторана
        TextView restaurantNameTextView = findViewById(R.id.restaurantNameTextView);
        restaurantNameTextView.setText(selectedRestaurant.getName());

        // Отображение описания ресторана
        TextView restaurantDescriptionTextView = findViewById(R.id.restaurantDescriptionTextView);
        restaurantDescriptionTextView.setText(selectedRestaurant.getDescription());

        // Инициализация базы данных Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tables");

        // Инициализация Firebase Storage
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

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

        // Получение списка столов для выбранного ресторана
        Query tablesQuery = databaseReference.orderByChild("restaurantKey").equalTo(selectedRestaurant.getName());

        tablesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Table> tablesList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Table table = snapshot.getValue(Table.class);
                    tablesList.add(table);
                }
                // Инициализация RecyclerView с полученным списком столов
                initializeTables(tablesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при чтении из базы данных
            }
        });

        // Инициализация кнопок выбора даты и времени
        dateButton = findViewById(R.id.dateSpinner);
        timeButton = findViewById(R.id.timeSpinner);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Установка текущей даты и времени по умолчанию
        selectedDate = Calendar.getInstance();
        selectedTime = Calendar.getInstance();
        selectedTime.add(Calendar.HOUR_OF_DAY, +4); // Добавление 1 часа к текущему времени

        // Отображение текущей даты и времени на кнопках
        dateButton.setText(dateFormatter.format(selectedDate.getTime()));
        timeButton.setText(timeFormatter.format(selectedTime.getTime()));

        // Обработчик нажатия на кнопку выбора даты
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Обработчик нажатия на кнопку выбора времени
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
    }

    private void initializeTables(List<Table> tablesList) {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tableAdapter = new TableAdapter(tablesList, dateButton, timeButton, userPhoneNumber);
        recyclerView.setAdapter(tableAdapter);

    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 5); // Adding 5 days to the current date

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, monthOfYear, dayOfMonth);

                if (selectedCalendar.before(Calendar.getInstance())) {
                    // Selected date is in the past, show an error message or warning
                    Toast.makeText(Tables.this, "Пожалуйста, выберите корректную дату", Toast.LENGTH_SHORT).show();
                } else {
                    selectedDate.set(year, monthOfYear, dayOfMonth);
                    dateButton.setText(dateFormatter.format(selectedDate.getTime()));
                }
            }
        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis()); // Set the minimum date to today
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()); // Set the maximum date
        datePickerDialog.show();
    }

    private TimePickerDialog timePickerDialog; // Declare as a field

    private void showTimePicker() {
        int currentHour = selectedTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = selectedTime.get(Calendar.MINUTE);

        // Define the minimum and maximum times
        int minHour = 10;
        int maxHour = 22;

        // Create a custom dialog
        Dialog timePickerDialog = new Dialog(this);
        timePickerDialog.setContentView(R.layout.dialog_time_picker);

        // Get a reference to the custom TimePicker view
        TimePicker customTimePicker = timePickerDialog.findViewById(R.id.customTimePicker);

        // Set the time picker mode
        customTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        // Set the minimum and maximum times
        customTimePicker.setHour(minHour);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            customTimePicker.setHour(maxHour);
        } else {
            customTimePicker.setCurrentHour(maxHour);
        }

        // Set the current time
        customTimePicker.setHour(currentHour);
        customTimePicker.setMinute(currentMinute);

        // Set a listener to handle time selection
        customTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // Validate the selected time
                if (hourOfDay >= minHour && hourOfDay <= maxHour) {
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedTime.set(Calendar.MINUTE, minute);
                    timeButton.setText(timeFormatter.format(selectedTime.getTime()));
                } else {
                    // Invalid time, show an error message or warning
                    Toast.makeText(Tables.this, "Пожалуйста, выберите время между 10 AM и 11 PM", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Show the dialog
        timePickerDialog.show();
    }};