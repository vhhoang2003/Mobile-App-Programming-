package com.example.testapi2.model


import java.io.Serializable

data class StudentInfo(
    val fullName: String,
    val studentID: String,
    val className: String,
    val schoolYear: String,
    val yearLevel: String,
    val departments: String,
    val plan: String,
    val phoneNumber: String
) : Serializable
