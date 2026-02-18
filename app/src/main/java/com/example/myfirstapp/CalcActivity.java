package com.example.myfirstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.MathUtils;

public class CalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        EditText etNumber1 = findViewById(R.id.etNumber1);
        EditText etNumber2 = findViewById(R.id.etNumber2);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        TextView tvResult = findViewById(R.id.tvResult);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n1Str = etNumber1.getText().toString();
                String n2Str = etNumber2.getText().toString();

                if (n1Str.isEmpty() || n2Str.isEmpty()) {
                    Toast.makeText(CalcActivity.this, "Введите оба числа", Toast.LENGTH_SHORT).show();
                    tvResult.setText("Результат: ?");
                    return;
                }

                try {
                    int n1 = Integer.parseInt(n1Str);
                    int n2 = Integer.parseInt(n2Str);
                    int sum = MathUtils.add(n1, n2);
                    tvResult.setText("Результат: " + sum);
                    Toast.makeText(CalcActivity.this, "Сумма: " + sum, Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(CalcActivity.this, "Ошибка ввода", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Для системной кнопки "Назад"
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Анимация при возврате
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}