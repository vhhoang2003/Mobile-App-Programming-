package com.example.lab1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Second_activity extends AppCompatActivity {

    TextView fullNameDisplay, studentIdDisplay, classDisplay, planDisplay;
    Button btnBack, btnCall, btnMessage, btnCamera;
    ImageView capturedImage;
    StudentInfo student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        fullNameDisplay = findViewById(R.id.fullNameDisplay);
        studentIdDisplay = findViewById(R.id.studentIdDisplay);
        classDisplay = findViewById(R.id.classDisplay);
        planDisplay = findViewById(R.id.planDisplay);
        btnBack = findViewById(R.id.button);
        btnCall = findViewById(R.id.btnCall);
        btnMessage = findViewById(R.id.btnMessage);
        btnCamera = findViewById(R.id.btnCamera);
        capturedImage = findViewById(R.id.capturedImage);

        student = (StudentInfo) getIntent().getSerializableExtra("studentData");

        if (student != null) {
            fullNameDisplay.setText("Full Name: " + student.fullName);
            studentIdDisplay.setText("Student ID: " + student.studentID);
            classDisplay.setText("Class: " + student.className + " | Year: " + student.yearLevel + "\nDept: " + student.departments);
            planDisplay.setText("Plan:\n" + student.plan);
        }

        btnBack.setOnClickListener(v -> finish());

        btnCall.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                callStudent();
            }
        });

        btnMessage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS}, 2);
            } else {
                messageStudent();
            }
        });

        btnCamera.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        });
    }

    private void callStudent() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + student.phoneNumber));
        startActivity(callIntent);
    }

    private void messageStudent() {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:" + student.phoneNumber));
        smsIntent.putExtra("sms_body", "Hello " + student.fullName);
        startActivity(smsIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            capturedImage.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            callStudent();
        } else if (requestCode == 1) {
            Toast.makeText(this, "Permission denied to make call", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == 2 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            messageStudent();
        } else if (requestCode == 2) {
            Toast.makeText(this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
        }
    }
}
