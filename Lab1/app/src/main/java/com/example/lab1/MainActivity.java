package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText inputFullName, inputStudentID, inputClass, inputSchoolYear, inputSelfPlanning;
    RadioGroup yearRadioGroup;
    CheckBox checkboxElectronics, checkboxCE, checkboxNetcom;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Đảm bảo file XML tên là activity_main.xml

        inputFullName = findViewById(R.id.inputFullName);
        inputStudentID = findViewById(R.id.inputStudentID);
        inputClass = findViewById(R.id.inputClass);
        inputSchoolYear = findViewById(R.id.inputSchoolYear);
        inputSelfPlanning = findViewById(R.id.inputSelfPlanning);
        yearRadioGroup = findViewById(R.id.yearRadioGroup);

        checkboxElectronics = findViewById(R.id.checkboxElectronics);
        checkboxCE = findViewById(R.id.checkboxCE);
        checkboxNetcom = findViewById(R.id.checkboxNetcom);

        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            int selectedYearId = yearRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedYear = findViewById(selectedYearId);
            String year = selectedYear != null ? selectedYear.getText().toString() : "N/A";

            StringBuilder departments = new StringBuilder();
            if (checkboxElectronics.isChecked()) departments.append("Electronics, ");
            if (checkboxCE.isChecked()) departments.append("Embedded Systems, ");
            if (checkboxNetcom.isChecked()) departments.append("Telecommunications, ");
            if (departments.length() > 0) departments.setLength(departments.length() - 2);

            StudentInfo student = new StudentInfo(
                    inputFullName.getText().toString(),
                    inputStudentID.getText().toString(),
                    inputClass.getText().toString(),
                    inputSchoolYear.getText().toString(),
                    year,
                    departments.toString(),
                    inputSelfPlanning.getText().toString()
            );

            Intent intent = new Intent(MainActivity.this, Second_activity.class);
            intent.putExtra("studentData", student);
            startActivity(intent);
        });

        // Khôi phục trạng thái nếu có
        if (savedInstanceState != null) {
            inputFullName.setText(savedInstanceState.getString("fullName"));
            inputStudentID.setText(savedInstanceState.getString("studentID"));
            inputClass.setText(savedInstanceState.getString("className"));
            inputSchoolYear.setText(savedInstanceState.getString("schoolYear"));
            inputSelfPlanning.setText(savedInstanceState.getString("plan"));
            yearRadioGroup.check(savedInstanceState.getInt("yearChecked"));
            checkboxElectronics.setChecked(savedInstanceState.getBoolean("check1"));
            checkboxCE.setChecked(savedInstanceState.getBoolean("check2"));
            checkboxNetcom.setChecked(savedInstanceState.getBoolean("check3"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("fullName", inputFullName.getText().toString());
        outState.putString("studentID", inputStudentID.getText().toString());
        outState.putString("className", inputClass.getText().toString());
        outState.putString("schoolYear", inputSchoolYear.getText().toString());
        outState.putString("plan", inputSelfPlanning.getText().toString());
        outState.putInt("yearChecked", yearRadioGroup.getCheckedRadioButtonId());
        outState.putBoolean("check1", checkboxElectronics.isChecked());
        outState.putBoolean("check2", checkboxCE.isChecked());
        outState.putBoolean("check3", checkboxNetcom.isChecked());
    }
}
