package io.akishichinibu.refrigerator.processor.ast

class SyntaxException(
    val posStart: Int,
    val posEnd: Int,
    private val reason: String,
): Exception("[SyntaxException] $reason") {

    constructor(
        posStart: Int,
        reason: String,
    ) : this(posStart, posStart + 1, reason) {}
}
