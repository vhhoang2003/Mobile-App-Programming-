package com.example.testapi2.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.style.LineHeightStyle
import com.example.testapi2.model.StudentInfo
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement

@Composable
fun StudentFormScreen(
    onSubmit: (StudentInfo) -> Unit
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var studentID by rememberSaveable { mutableStateOf("") }
    var className by rememberSaveable { mutableStateOf("") }
    var schoolYear by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var plan by rememberSaveable { mutableStateOf("") }

    var selectedYear by rememberSaveable { mutableStateOf("Fresher") }
    val yearOptions = listOf("Fresher", "Sophomore", "Junior", "Senior")

    val departments = remember {
        mutableStateMapOf(
            "Electronics" to false,
            "Embedded Systems" to false,
            "Telecommunications" to false
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Student's Information", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = studentID, onValueChange = { studentID = it }, label = { Text("Student ID") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = className, onValueChange = { className = it }, label = { Text("Class") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = schoolYear, onValueChange = { schoolYear = it }, label = { Text("School Year") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))
        Text("Year Level:")
        yearOptions.forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selectedYear == it, onClick = { selectedYear = it })
                Text(it)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Department:")
        departments.forEach { (key, value) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = value, onCheckedChange = { departments[key] = it })
                Text(key)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = plan,
            onValueChange = { plan = it },
            label = { Text("Self-develop Plan") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 10
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val departmentList = departments.filterValues { it }.keys.joinToString(", ")
                val student = StudentInfo(fullName, studentID, className, schoolYear, selectedYear, departmentList, plan, phoneNumber)
                onSubmit(student)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentFormScreenPreview() {
    StudentFormScreen(onSubmit = {})
}
