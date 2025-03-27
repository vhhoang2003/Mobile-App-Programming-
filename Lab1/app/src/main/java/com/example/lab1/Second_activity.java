package com.example.lab1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Second_activity extends AppCompatActivity {

    TextView fullNameDisplay, studentIdDisplay, classDisplay, planDisplay;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second); // XML chứa CardView + Back

        fullNameDisplay = findViewById(R.id.fullNameDisplay);
        studentIdDisplay = findViewById(R.id.studentIdDisplay);
        classDisplay = findViewById(R.id.classDisplay);
        planDisplay = findViewById(R.id.planDisplay);
        btnBack = findViewById(R.id.button);

        StudentInfo student = (StudentInfo) getIntent().getSerializableExtra("studentData");

        if (student != null) {
            fullNameDisplay.setText("Full Name: " + student.fullName);
            studentIdDisplay.setText("Student ID: " + student.studentID);
            classDisplay.setText("Class: " + student.className + " | Year: " + student.yearLevel + "\nDept: " + student.departments);
            planDisplay.setText("Plan:\n" + student.plan);
        }

        btnBack.setOnClickListener(v -> finish());
    }
}
