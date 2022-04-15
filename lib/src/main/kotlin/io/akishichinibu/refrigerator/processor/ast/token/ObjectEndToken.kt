package io.akishichinibu.refrigerator.processor.ast.token

import io.akishichinibu.refrigerator.JSONTokenType

class ObjectEndToken() : Token() {

    override val type: JSONTokenType = JSONTokenType.END_OBJECT

}
