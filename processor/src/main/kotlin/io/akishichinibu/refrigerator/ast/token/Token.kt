package io.akishichinibu.refrigerator.ast.token

import io.akishichinibu.refrigerator.JSONTokenType
import io.akishichinibu.refrigerator.ast.Variable


abstract class Token() : Variable() {

    abstract val type: JSONTokenType

}
