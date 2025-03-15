package com.example.quadraticequation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các view từ XML
        EditText editTextA = findViewById(R.id.editTextNumberA);
        EditText editTextB = findViewById(R.id.editTextNumberB);
        EditText editTextC = findViewById(R.id.editTextNumberC);
        Button btnSolve = findViewById(R.id.btnSolve);

        // Xử lý sự kiện khi nhấn nút "Calculate"
        btnSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Lấy dữ liệu từ EditText và chuyển thành số
                    double a = Double.parseDouble(editTextA.getText().toString());
                    double b = Double.parseDouble(editTextB.getText().toString());
                    double c = Double.parseDouble(editTextC.getText().toString());

                    // Kiểm tra nếu A = 0 -> Không phải phương trình bậc hai
                    if (a == 0) {
                        Toast.makeText(MainActivity.this, "A phải khác 0!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Tính delta
                    double delta = b * b - 4 * a * c;
                    String result;

                    // Xác định số nghiệm
                    if (delta > 0) {
                        double x1 = (-b + Math.sqrt(delta)) / (2 * a);
                        double x2 = (-b - Math.sqrt(delta)) / (2 * a);
                        result = "Phương trình có hai nghiệm:\nX1 = " + x1 + "\nX2 = " + x2;
                    } else if (delta == 0) {
                        double x = -b / (2 * a);
                        result = "Phương trình có nghiệm kép:\nX = " + x;
                    } else {
                        result = "Phương trình vô nghiệm!";
                    }

                    // Tạo Bundle để truyền dữ liệu
                    Bundle bundle = new Bundle();
                    bundle.putDouble("A", a);
                    bundle.putDouble("B", b);
                    bundle.putDouble("C", c);
                    bundle.putString("RESULT", result);

                    // Tạo Intent để chuyển sang SecondActivity
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtras(bundle); // Gửi dữ liệu

                    // Chuyển sang SecondActivity
                    startActivity(intent);

                } catch (NumberFormatException e) {
                    // Xử lý nếu người dùng nhập không hợp lệ
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ hệ số!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
