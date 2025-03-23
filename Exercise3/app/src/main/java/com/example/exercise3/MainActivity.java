package com.example.exercise3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText StudentContent, TeacherContent;
    Button SubmitBt;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        StudentContent = findViewById(R.id.multiLineEditText);
        TeacherContent = findViewById(R.id.multiLineEditText2);
        SubmitBt = findViewById(R.id.button);

        SubmitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(MainActivity.this, Second_activity.class);

                String content = StudentContent.getText().toString();
                myintent.putExtra("Content", content);
                startActivityForResult(myintent, 99);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 99 && resultCode == RESULT_OK) {
            if (data != null) {
                String returnedText = data.getStringExtra("EditedText");
                TeacherContent.setText(returnedText); // gán nội dung đã chỉnh sửa vào EditText số 2
            }
        }
    }
}
