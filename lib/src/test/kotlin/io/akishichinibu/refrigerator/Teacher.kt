package io.akishichinibu.refrigerator

import io.akishichinibu.refrigerator.annotation.Refrigerate


@Refrigerate
data class Teacher(
    val name: String,
    val hasStudent: Boolean,
)
