package io.akishichinibu.refrigerator.processor.ast.token

import io.akishichinibu.refrigerator.JSONTokenType
import io.akishichinibu.refrigerator.ast.Variable


abstract class Token() : Variable() {

    abstract val type: JSONTokenType

}
