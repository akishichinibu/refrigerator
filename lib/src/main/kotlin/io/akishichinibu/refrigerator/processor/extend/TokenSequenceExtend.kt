package io.akishichinibu.refrigerator.processor.extend

import io.akishichinibu.refrigerator.JSONTokenType
import io.akishichinibu.refrigerator.NULL_LITERAL
import io.akishichinibu.refrigerator.ast.token.*


inline fun <reified T : Token> toPrintableString(token: T): String {
    return when (token) {
        is ObjectBeginToken -> {
            "{"
        }
        is ObjectEndToken -> {
            "}"
        }
        is ArrayBeginToken -> {
            "["
        }
        is ArrayEndToken -> {
            "]"
        }
        is TrueToken -> {
            "true"
        }
        is FalseToken -> {
            "false"
        }
        is NullToken -> {
            "null"
        }
        is ColonToken -> {
            ":"
        }
        is CommaToken -> {
            ","
        }
        is NumberToken -> {
            token.value.toString()
        }
        is StringToken -> {
            "\\\"${token.value}\\\""
        }
        else -> {
            throw Exception("unknown value type")
        }
    }
}


inline fun Sequence<Token>.generatePrintableStringStream(): Sequence<String> {
    val buffer = mutableListOf<Token>()

    return sequence {
        this@generatePrintableStringStream.forEach { it ->
            when (it) {
                is PropertyReferenceToken -> {
                    val s = buffer.joinToString("") { toPrintableString(it) }
                    yield(quoteIt(s))
                    buffer.clear()

                    when (it.type) {
                        JSONTokenType.STRING -> yield(refIt(it.ref))
                        JSONTokenType.BOOLEAN -> yield(forBooleanRef(it.ref))
                        JSONTokenType.NULL -> yield(NULL_LITERAL)
                        else -> yield(stringifyIt(it.ref))
                    }
                }
                else -> {
                    buffer.add(it)
                }
            }
        }

        if (buffer.isNotEmpty()) {
            val s = buffer.joinToString("") { toPrintableString(it) }
            yield(quoteIt(s))
        }
    }
}
