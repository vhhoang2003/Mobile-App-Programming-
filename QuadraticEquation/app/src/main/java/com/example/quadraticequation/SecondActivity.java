package com.example.quadraticequation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        // Ánh xạ các view từ XML
        TextView tvResult = findViewById(R.id.tvResult);
        Button btnBack = findViewById(R.id.button2);

        // Nhận dữ liệu từ Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            double a = bundle.getDouble("A");
            double b = bundle.getDouble("B");
            double c = bundle.getDouble("C");
            String result = bundle.getString("RESULT");

            // Hiển thị kết quả
            tvResult.setText("Phương trình: " + a + "x² + " + b + "x + " + c + " = 0\n\n" + result);
        }

        // Xử lý khi nhấn nút "Back" để quay lại MainActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Kết thúc SecondActivity
            }
        });
    }
}
