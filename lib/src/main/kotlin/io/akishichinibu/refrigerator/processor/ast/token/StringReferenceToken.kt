package io.akishichinibu.refrigerator.processor.ast.token

import io.akishichinibu.refrigerator.JSONTokenType

typealias TokenRef = Triple<String, Int, Int>

open class StringReferenceToken(
    val ref: TokenRef,
) : Token() {

    override val type: JSONTokenType = JSONTokenType.STRING

    val value: String
        get() {
            val (source, start, end) = this.ref
            return source.slice(start..end)
        }

}
