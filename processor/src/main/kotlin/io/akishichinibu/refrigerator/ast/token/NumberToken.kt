package io.akishichinibu.refrigerator.ast.token

import io.akishichinibu.refrigerator.JSONTokenType


open class NumberToken(
    val value: Number,
) : Token() {

    override val type: JSONTokenType = JSONTokenType.NUMBER

}
