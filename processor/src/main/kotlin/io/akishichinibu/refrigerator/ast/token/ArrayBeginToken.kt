package io.akishichinibu.refrigerator.ast.token

import io.akishichinibu.refrigerator.JSONTokenType

class ArrayBeginToken() : Token() {

    override val type: JSONTokenType = JSONTokenType.BEGIN_ARRAY

}
