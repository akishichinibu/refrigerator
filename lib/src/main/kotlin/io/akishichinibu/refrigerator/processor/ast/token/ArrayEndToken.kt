package io.akishichinibu.refrigerator.processor.ast.token

import io.akishichinibu.refrigerator.JSONTokenType

class ArrayEndToken() : Token() {

    override val type: JSONTokenType = JSONTokenType.END_ARRAY

}
