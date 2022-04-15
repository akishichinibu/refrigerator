package io.akishichinibu.refrigerator

import io.akishichinibu.refrigerator.annotation.Refrigerate


@Refrigerate
data class Student(
    val name: String,
    val number: Int,
    val isValid: Boolean,
)
