package io.akishichinibu.refrigerator.processor.ast

class SchemaException(
    val posStart: Int,
    val posEnd: Int,
    private val reason: String,
): Exception("[SchemaException] $reason") {}
