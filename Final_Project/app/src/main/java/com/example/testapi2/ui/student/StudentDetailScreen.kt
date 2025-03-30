package com.example.testapi2.ui.student

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testapi2.R
import com.example.testapi2.model.StudentInfo

@Composable
fun StudentDetailScreen(
    student: StudentInfo,
    onBack: () -> Unit,
    onCall: () -> Unit,
    onMessage: () -> Unit,
    onOpenCamera: () -> Unit,
    capturedBitmap: Bitmap? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Image(
                    bitmap = capturedBitmap?.asImageBitmap()
                        ?: painterResource(R.drawable.ic_launcher_background).toBitmap().asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Full Name: ${student.fullName}", fontWeight = FontWeight.Bold)
                    Text("Student ID: ${student.studentID}")
                    Text("Class: ${student.className} | ${student.yearLevel}")
                    Text("Departments: ${student.departments}")
                    Text("Plan:\n${student.plan}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = onBack) { Text("Back") }
            Button(onClick = onCall) { Text("Call") }
            Button(onClick = onMessage) { Text("Message") }
            Button(onClick = onOpenCamera) { Text("Camera") }
        }
    }
}

// Optional fallback bitmap for preview
@Composable
fun Painter.toBitmap(): Bitmap {
    return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}

@Preview(showBackground = true)
@Composable
fun StudentDetailScreenPreview() {
    val mockStudent = StudentInfo(
        fullName = "John Doe",
        studentID = "123456",
        className = "ET01",
        schoolYear = "2021-2025",
        yearLevel = "Junior",
        departments = "Electronics, Embedded Systems",
        plan = "Become an AI Engineer",
        phoneNumber = "0123456789"
    )
    StudentDetailScreen(
        student = mockStudent,
        onBack = {},
        onCall = {},
        onMessage = {},
        onOpenCamera = {},
        capturedBitmap = null
    )
}
