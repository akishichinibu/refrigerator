package io.akishichinibu.refrigerator.ast

class LexicalException(
    val source: String,
    val posStart: Int,
    val posEnd: Int,
    private val reason: String,
): Exception("[LexicalException] $reason") {}
