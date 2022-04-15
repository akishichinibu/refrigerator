package io.akishichinibu.refrigerator.ast.token

import io.akishichinibu.refrigerator.*
import io.akishichinibu.refrigerator.ast.LexicalException

class LiteralToken(
    override val type: JSONTokenType,
    val value: String,
    ) : Token() {
}


val colonToken = LiteralToken(JSONTokenType.SEP_COLON, COLON_LITERAL)
val commaToken = LiteralToken(JSONTokenType.SEP_COMMA, COMMA_LITERAL)
val trueToken = LiteralToken(JSONTokenType.BOOLEAN, TRUE_LITERAL)
val falseToken = LiteralToken(JSONTokenType.BOOLEAN, FALSE_LITERAL)
val nullToken = LiteralToken(JSONTokenType.NULL, NULL_LITERAL)
val beginObjectToken = LiteralToken(JSONTokenType.NULL, BEGIN_OBJECT_LITERAL)
val endObjectToken = LiteralToken(JSONTokenType.NULL, END_OBJECT_LITERAL)
val beginArrayToken = LiteralToken(JSONTokenType.NULL, BEGIN_ARRAY_LITERAL)
val endArrayToken = LiteralToken(JSONTokenType.NULL, END_ARRAY_LITERAL)


inline fun getLiteralFromFirst(c: Char): String {
    return when (c) {
        'n' -> NULL_LITERAL
        't' -> TRUE_LITERAL
        'f' -> FALSE_LITERAL
        else -> {
            throw LexicalException("", 0, 0, "Unknown token starts with $c")
        }
    }
}


inline fun getTokenFromLiteral(c: Char): LiteralToken {
    return when (c) {
        '{' -> beginObjectToken
        '}' -> endObjectToken
        '[' -> beginArrayToken
        ']' -> endArrayToken
        ',' -> commaToken
        ':' -> colonToken
        'n' -> nullToken
        't' -> trueToken
        'f' -> falseToken
        else -> {
            throw LexicalException("", 0, 0, "Unknown token starts with $c")
        }
    }
}
