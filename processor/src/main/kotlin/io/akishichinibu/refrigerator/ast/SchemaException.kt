package io.akishichinibu.refrigerator.ast

class SchemaException(
    val posStart: Int,
    val posEnd: Int,
    private val reason: String,
): Exception("[SchemaException] $reason") {}
