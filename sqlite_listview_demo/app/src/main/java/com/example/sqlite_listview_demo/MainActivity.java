package com.example.sqlite_listview_demo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAdd;
    // Khai báo ListView để hiển thị danh sách sinh viên
    ListView listViewStudents;

    // Khai báo ArrayAdapter giúp kết nối dữ liệu (ArrayList) với ListView
    ArrayAdapter<String> adapter;

    // Khai báo ArrayList chứa danh sách sinh viên (đọc từ SQLite)
    ArrayList<String> studentList;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        buttonAdd = findViewById(R.id.buttonAdd);
        // Gán ListView trong layout vào biến Java
        listViewStudents = findViewById(R.id.listViewStudents);

        // Lấy toàn bộ danh sách sinh viên từ cơ sở dữ liệu thông qua DBHelper
        dbHelper = new DBHelper(this);
        studentList = dbHelper.getAllStudents();

        // Gắn adapter vào ListView để hiển thị dữ liệu
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        listViewStudents.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!name.isEmpty()) {
                    dbHelper.insertStudent(name);
                    studentList.clear();
                    studentList.addAll(dbHelper.getAllStudents());
                    adapter.notifyDataSetChanged();
                    editTextName.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
