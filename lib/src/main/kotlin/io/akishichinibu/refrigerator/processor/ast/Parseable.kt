package io.akishichinibu.refrigerator.processor.ast

import io.akishichinibu.refrigerator.ast.token.Token

interface Parseable {

    // parse a JSONObject, and return a Token stream
    fun parse(): Sequence<Token>

}