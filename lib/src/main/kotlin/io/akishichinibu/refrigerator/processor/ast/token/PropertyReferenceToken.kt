package io.akishichinibu.refrigerator.processor.ast.token

import io.akishichinibu.refrigerator.JSONTokenType

class PropertyReferenceToken(
    type: JSONTokenType,
    val ref: String,
) : Token() {

    override val type: JSONTokenType = type

}
