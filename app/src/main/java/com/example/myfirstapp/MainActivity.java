package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим ListView
        ListView lvScreens = findViewById(R.id.lvScreens);

        // Массив пунктов меню
        String[] screens = {
                "Открыть профиль",
                "Открыть экран с расчётом",
                "Открыть экран настроек"
        };

        // Адаптер для отображения строк
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                screens
        );

        // Привязываем адаптер
        lvScreens.setAdapter(adapter);

        // Обработчик кликов с АНИМАЦИЯМИ
        lvScreens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                if (position == 0) {
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                } else if (position == 1) {
                    intent = new Intent(MainActivity.this, CalcActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                }

                // Запускаем Activity
                startActivity(intent);

                // Добавляем АНИМАЦИЮ перехода
                // slide_in_right - новая Activity выезжает справа
                // slide_out_left - текущая Activity уезжает влево
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}