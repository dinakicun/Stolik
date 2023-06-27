package com.example.stolik;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.Toast;
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    private List<Table> tablesList;
    private Button dateButton;
    private Button timeButton;
    private TextView restaurantNameTextView;
    private String phoneNumber;

    public TableAdapter(List<Table> tablesList, Button dateButton, Button timeButton, String phoneNumber) {
        this.tablesList = tablesList;
        this.dateButton = dateButton;
        this.timeButton = timeButton;
        this.phoneNumber = phoneNumber;
        this.restaurantNameTextView = restaurantNameTextView;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Table table = tablesList.get(position);
        holder.tableNameTextView.setText(table.getName());
        holder.tableTypeTextView.setText(table.getType());
        holder.tableCountTextView.setText(String.valueOf(table.getCount()));
    }

    @Override
    public int getItemCount() {
        return tablesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tableNameTextView;
        TextView tableTypeTextView;
        TextView tableCountTextView;
        Button reserveButton;

        public ViewHolder(View itemView) {
            super(itemView);
            tableNameTextView = itemView.findViewById(R.id.tableName);
            tableTypeTextView = itemView.findViewById(R.id.tableType);
            tableCountTextView = itemView.findViewById(R.id.tableCount);
            reserveButton = itemView.findViewById(R.id.button);
            reserveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Table table = tablesList.get(position);
                        // Получите выбранный столик и другие данные, которые вам нужны
                        String restaurantName = table.getRestaurantName();
                        String tableName = table.getName();
                        String date = dateButton.getText().toString(); // Получите выбранную дату из активности Tables
                        String time = timeButton.getText().toString(); // Получите выбранное время из активности Tables
                        String endTime = calculateEndTime(time);

                        // Создайте объект Reservation с полученными данными
                        Reservation reservation = new Reservation();
                        reservation.setRestaurantName(restaurantName);
                        reservation.setTableName(tableName);
                        reservation.setDate(date);
                        reservation.setTime(time);
                        reservation.setEndTime(endTime);
                        reservation.setUser(TableAdapter.this.phoneNumber); // Используйте переменную phoneNumber класса TableAdapter

                        // Сохраните объект Reservation в базе данных Firebase
                        DatabaseReference reservationsRef = FirebaseDatabase.getInstance().getReference("Reservation");
                        String reservationId = reservationsRef.push().getKey(); // Генерируйте уникальный ключ для каждой записи
                        reservationsRef.child(reservationId).setValue(reservation)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Бронирование успешно сохранено в базе данных
                                            DatabaseReference reservationRef = reservationsRef.child(reservationId);
                                            reservationRef.child("endTime").setValue(endTime);

                                            Toast.makeText(itemView.getContext(), "Бронь успешно создана", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Произошла ошибка при сохранении бронирования
                                            Toast.makeText(itemView.getContext(), "Ошибка при создании брони", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });
        }

        private String calculateEndTime(String time) {
            // Преобразуйте строку времени в формат, позволяющий выполнить арифметические операции
            String[] timeParts = time.split(":");
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);

            // Добавьте 2 часа к времени
            hours += 2;

            // Проверьте, превышает ли время 24 часа и выполните соответствующие действия
            if (hours >= 24) {
                hours = 23;
                minutes = 00;
                // Можно выполнить дополнительные действия, если необходимо, например, увеличить дату
            }

            // Преобразуйте время обратно в строку в формате "чч:мм"
            String endTime = String.format("%02d:%02d", hours, minutes);

            return endTime;
        }
    }
}
