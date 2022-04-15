package io.akishichinibu.refrigerator.processor.ast.token

import io.akishichinibu.refrigerator.JSONTokenType


open class StringToken(
    val value: String,
) : Token() {

    override val type: JSONTokenType = JSONTokenType.STRING

}
