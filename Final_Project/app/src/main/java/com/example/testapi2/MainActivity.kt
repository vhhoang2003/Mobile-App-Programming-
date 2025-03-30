package com.example.testapi2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapi2.model.StudentInfo
import com.example.testapi2.ui.student.StudentFormScreen
import com.example.testapi2.ui.student.StudentDetailScreen
import com.example.testapi2.ui.theme.TestAPi2Theme

sealed class ScreenState {
    object Form : ScreenState()
    data class Detail(val student: StudentInfo) : ScreenState()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestAPi2Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val context = LocalContext.current
                    val viewModel: BakingViewModel = viewModel()
                    var screenState by remember { mutableStateOf<ScreenState>(ScreenState.Form) }
                    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
                    var prompt by rememberSaveable { mutableStateOf("") }
                    var studyQuestion by rememberSaveable { mutableStateOf("") }
                    var responseLog by remember { mutableStateOf("") }

                    val uiState by viewModel.uiState.collectAsState()

                    val cameraLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.TakePicturePreview()
                    ) { bitmap: Bitmap? ->
                        if (bitmap != null) capturedImage = bitmap
                    }

                    when (val screen = screenState) {
                        is ScreenState.Form -> {
                            StudentFormScreen { student ->
                                screenState = ScreenState.Detail(student)
                            }
                        }

                        is ScreenState.Detail -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                StudentDetailScreen(
                                    student = screen.student,
                                    capturedBitmap = capturedImage,
                                    onBack = { screenState = ScreenState.Form },
                                    onCall = {
                                        if (ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.CALL_PHONE
                                            ) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                            ActivityCompat.requestPermissions(
                                                this@MainActivity,
                                                arrayOf(Manifest.permission.CALL_PHONE), 1
                                            )
                                        } else {
                                            val callIntent = Intent(Intent.ACTION_CALL)
                                            callIntent.data = Uri.parse("tel:" + screen.student.phoneNumber)
                                            context.startActivity(callIntent)
                                        }
                                    },
                                    onMessage = {
                                        if (ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.SEND_SMS
                                            ) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                            ActivityCompat.requestPermissions(
                                                this@MainActivity,
                                                arrayOf(Manifest.permission.SEND_SMS), 2
                                            )
                                        } else {
                                            val smsIntent = Intent(Intent.ACTION_SENDTO)
                                            smsIntent.data = Uri.parse("smsto:" + screen.student.phoneNumber)
                                            smsIntent.putExtra("sms_body", "Hello " + screen.student.fullName)
                                            context.startActivity(smsIntent)
                                        }
                                    },
                                    onOpenCamera = {
                                        cameraLauncher.launch(null)
                                    }
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = prompt,
                                    onValueChange = { prompt = it },
                                    label = { Text("Ask Gemini about this student...") },
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                )

                                Button(
                                    onClick = {
                                        capturedImage?.let {
                                            viewModel.sendPrompt(it, prompt)
                                        }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(top = 8.dp)
                                ) {
                                    Text("Ask Gemini")
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                OutlinedTextField(
                                    value = studyQuestion,
                                    onValueChange = { studyQuestion = it },
                                    label = { Text("Enter your study question...") },
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .fillMaxWidth()
                                )

                                Button(
                                    onClick = {
                                        viewModel.sendPrompt(
                                            bitmap = capturedImage ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
                                            prompt = studyQuestion,
                                            mode = "study"
                                        )
                                    },
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(bottom = 8.dp)
                                ) {
                                    Text("Ask Gemini for Study Help")
                                }

                                OutlinedTextField(
                                    value = responseLog,
                                    onValueChange = {},
                                    label = { Text("Response Log") },
                                    readOnly = true,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                        .height(250.dp),
                                    maxLines = 10
                                )

                                when (uiState) {
                                    is UiState.Loading -> CircularProgressIndicator()
                                    is UiState.Success -> {
                                        val output = (uiState as UiState.Success).outputText
                                        responseLog = "Q: $studyQuestion\n\nA: $output"
                                    }
                                    is UiState.Error -> {
                                        responseLog = "Error: ${(uiState as UiState.Error).errorMessage}"
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
