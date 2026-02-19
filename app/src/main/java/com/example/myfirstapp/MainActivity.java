package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Для навигации (из Практики 2)
    private ListView lvScreens;

    // Для SQLite
    private DatabaseHelper dbHelper;
    private ListView listViewTasks;
    private ArrayAdapter<String> adapter;
    private List<Task> tasks = new ArrayList<>();
    private int selectedTaskId = -1;

    private EditText etTitle, etDesc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ========== КОД ИЗ ПРАКТИКИ 2 (НАВИГАЦИЯ) ==========
        lvScreens = findViewById(R.id.lvScreens);

        String[] screens = {
                "Открыть профиль",
                "Открыть экран с расчётом",
                "Открыть экран настроек"
        };

        ArrayAdapter<String> navAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                screens
        );

        lvScreens.setAdapter(navAdapter);

        lvScreens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if (position == 0) {
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                } else if (position == 1) {
                    intent = new Intent(MainActivity.this, CalcActivity.class);
                } else if (position == 2) {
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                }
                if (intent != null) {
                    startActivity(intent);
                    // Анимация перехода (из Практики 2)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
        // ========== КОНЕЦ КОДА НАВИГАЦИИ ==========

        // ========== НОВЫЙ КОД ДЛЯ SQLite CRUD ==========
        // Инициализация
        dbHelper = new DatabaseHelper(this);
        listViewTasks = findViewById(R.id.listViewTasks);
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);

        // Кнопка "Добавить"
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(MainActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.addTask(title, desc)) {
                Toast.makeText(MainActivity.this, "Задача добавлена!", Toast.LENGTH_SHORT).show();
                refreshList();
                clearFields();
            } else {
                Toast.makeText(MainActivity.this, "Ошибка при добавлении", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка "Обновить"
        Button btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(v -> refreshList());

        // Обработка клика по задаче в ListView
        listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
            Task selectedTask = tasks.get(position);
            selectedTaskId = selectedTask.getId();
            etTitle.setText(selectedTask.getTitle());
            etDesc.setText(selectedTask.getDescription());
            Toast.makeText(MainActivity.this, "Выбрана задача: " + selectedTask.getTitle(), Toast.LENGTH_SHORT).show();
        });

        // Кнопка "Редактировать" (самостоятельное задание)
        Button btnEdit = findViewById(R.id.btnEditSelected);
        btnEdit.setOnClickListener(v -> {
            if (selectedTaskId == -1) {
                Toast.makeText(MainActivity.this, "Сначала выберите задачу", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = etTitle.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(MainActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            Task updatedTask = new Task(title, desc);
            updatedTask.setId(selectedTaskId);

            if (dbHelper.updateTask(updatedTask)) {
                Toast.makeText(MainActivity.this, "Задача обновлена!", Toast.LENGTH_SHORT).show();
                refreshList();
                clearFields();
                selectedTaskId = -1;
            } else {
                Toast.makeText(MainActivity.this, "Ошибка при обновлении", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка "Удалить"
        Button btnDelete = findViewById(R.id.btnDeleteSelected);
        btnDelete.setOnClickListener(v -> {
            if (selectedTaskId == -1) {
                Toast.makeText(MainActivity.this, "Сначала выберите задачу", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.deleteTask(selectedTaskId)) {
                Toast.makeText(MainActivity.this, "Задача удалена!", Toast.LENGTH_SHORT).show();
                refreshList();
                clearFields();
                selectedTaskId = -1;
            } else {
                Toast.makeText(MainActivity.this, "Ошибка при удалении", Toast.LENGTH_SHORT).show();
            }
        });

        // Загружаем список при старте
        refreshList();
    }

    // Обновление списка задач
    private void refreshList() {
        tasks.clear();
        tasks.addAll(dbHelper.getAllTasks());

        List<String> taskTitles = new ArrayList<>();
        for (Task task : tasks) {
            taskTitles.add(task.getTitle() + " (" + task.getDescription() + ")");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskTitles);
        listViewTasks.setAdapter(adapter);
    }

    // Очистка полей ввода
    private void clearFields() {
        etTitle.setText("");
        etDesc.setText("");
    }
}