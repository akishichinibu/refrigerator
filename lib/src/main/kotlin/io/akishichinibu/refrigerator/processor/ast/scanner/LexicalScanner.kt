package io.akishichinibu.refrigerator.processor.ast.scanner

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import io.akishichinibu.refrigerator.JSONTokenType
import io.akishichinibu.refrigerator.ast.LexicalException
import io.akishichinibu.refrigerator.ast.SchemaException
import io.akishichinibu.refrigerator.ast.SyntaxException
import io.akishichinibu.refrigerator.ast.token.*
import java.util.*
import kotlin.collections.ArrayDeque


inline fun literalExtraMatch(literal: String, iter: Iterator<IndexedValue<Char>>): String {
    var t = 0

    while (true) {
        if (!iter.hasNext()) {
            throw LexicalException(literal, 0, 0, "Try to match [$literal] but reached the end")
        }

        val (subIndex, subC) = iter.next()

        if (subC != literal[t]) {
            throw Exception("\" has not match")
        }

        t += 1

        if (t == literal.length) {
            return literal
        }
    }
}


inline fun scanCurrentStringToken(source: String, iter: Iterator<IndexedValue<Char>>): Pair<Int, Int> {
    var start = -1
    var end = -1

    while (true) {
        if (!iter.hasNext()) {
            throw LexicalException(source, start, end, "The '\"' at the tail of source doesn't have a matched quote")
        }

        val (index, c) = iter.next()

        if (start < 0) {
            start = index
        }

        when (c) {
            '\\' -> {
                if (!iter.hasNext()) {
                    throw Exception("\\ not match at $index")
                }

                val (subIndex, subC) = iter.next()

                val r = when (subC) {
                    ' ', '"', '\\' -> subC
                    'b' -> Char(2)
                    else -> {
                        throw Exception("unknown char $c$subC")
                    }
                }

            }
            '"' -> {
                end = index
                break
            }
        }
    }

    return Pair(start, end)
}


inline fun scanCurrentNumberToken(
    currentIndex: Int,
    iter: Iterator<IndexedValue<Char>>
): Pair<Pair<Int, Int>, IndexedValue<Char>?> {
    var end = -1

    while (true) {
        if (!iter.hasNext()) {
            break
        }

        val (index, c) = iter.next()

        when (c) {
            in '0'..'9' -> {
                continue
            }
            else -> {
                end = index
                return Pair(Pair(currentIndex, end), IndexedValue(index, c))
            }
        }
    }

    return Pair(Pair(currentIndex, end), null)
}


typealias TokenTriple = Triple<Token, Int, Int>


inline fun buildLiteralTokenTriple(token: LiteralToken, start: Int): TokenTriple {
    return Triple(token, start, start + token.value.length)
}


inline fun <T: Token> popStackTwice(stack: Stack<Token>): T {
    if (stack.size < 2) {
        throw SyntaxException(0, "error!!")
    }

    val t = stack.pop()

    if (t.type != JSONTokenType.SEP_COLON) {
        throw SyntaxException(0, "error!!")
    }

    return stack.pop() as T
}


inline fun parser(s: String, classDeclaration: KSClassDeclaration): Sequence<TokenTriple> {
    val buffer = ArrayDeque<IndexedValue<Char>>()
    val stack = Stack<Token>()

    val targetModel = classDeclaration
        .getAllProperties()
        .filter { it.validate() }
        .map {
            Pair(it.simpleName.asString(), it.type.toString())
        }
        .toMap()

    val charIterator = s.asSequence().iterator().withIndex()

    sequence {
        while (buffer.isNotEmpty() || charIterator.hasNext()) {
            val (index, c) = if (buffer.isNotEmpty()) {
                buffer.removeFirst()
            } else {
                charIterator.next()
            }

            val top = stack.firstOrNull()

            yield(
                when (c) {
                    ' ' -> {
                        continue
                    }
                    '{', '}', '[', ']', ',', ':' -> {
                        val token = getTokenFromLiteral(c)
                        buildLiteralTokenTriple(getTokenFromLiteral(c), index)

                        when (token.type) {
                            JSONTokenType.BEGIN_OBJECT -> {
                                stack.add(token)
                            }
                            JSONTokenType.END_OBJECT -> {
                                if (top?.type != JSONTokenType.BEGIN_OBJECT) {
                                    throw SyntaxException(index, "not match begin at $index")
                                }
                                stack.pop()
                            }
                            JSONTokenType.SEP_COLON -> {
                                if (top?.type != JSONTokenType.STRING) {
                                    throw SyntaxException(index, "require a string before a colon but get a $top")
                                }
                                stack.push(token)
                            }
                            JSONTokenType.SEP_COMMA -> {
                                if (stack.top().type != JSONTokenType.BEGIN_OBJECT) {
                                    throw SyntaxException(index, "the comma should at the end of a key value pair")
                                }
                                stack.pop()
                            }
                        }
                    }
                    '"' -> {
                        when (top?.type) {
                            JSONTokenType.BEGIN_OBJECT -> {
                                // is a key
                                val (start, end) = scanCurrentStringToken(s, charIterator)
                                val token = StringReferenceToken(Triple(s, start, end))

                                if (!targetModel.contains(token.value)) {
                                    throw SchemaException(start, end, "Unknown key ${token.value}")
                                }

                                stack.add(token)
                            }
                            JSONTokenType.SEP_COLON -> {
                                // is a value
                                // pop twice here is safe
                                val keyToken = popStackTwice<StringToken>(stack)
                                val type = targetModel[keyToken.value]!!

                                when (type) {
                                    "String" -> {
                                        token.type != JSONTokenType.STRING
                                    }
                                }
                            }
                            else -> {
                                throw Exception("")
                            }
                        }
                        val (start, end) = scanCurrentStringToken(s, charIterator)
                        val ref = StringReferenceToken(Triple(s, start, end))
                        Triple(ref, index, index + end - start + 1)
                    }
                    'n' -> {
                        val keyToken = popStackTwice<StringToken>(stack)
                        val type = targetModel[keyToken.value]!!

                        if (type != "Null") {
                            throw SyntaxException(index, "")
                        }

                        literalExtraMatch(getLiteralFromFirst(c), charIterator)
                        buildLiteralTokenTriple(getTokenFromLiteral(c), index)
                    }
                    't', 'f' -> {
                        // should be a value
                        val keyToken = popStackTwice<StringToken>(stack)
                        val type = targetModel[keyToken.value]!!

                        if (type != "Boolean") {
                            throw SyntaxException(index, "")
                        }

                        literalExtraMatch(getLiteralFromFirst(c), charIterator)
                        buildLiteralTokenTriple(getTokenFromLiteral(c), index)
                    }
                    in '0'..'9' -> {
                        // should be a value
                        val keyToken = popStackTwice<StringToken>(stack)
                        val type = targetModel[keyToken.value]!!

                        if (type != "Number") {
                            throw SyntaxException(index, "")
                        }

                        literalExtraMatch(getLiteralFromFirst(c), charIterator)
                        buildLiteralTokenTriple(getTokenFromLiteral(c), index)

                        val (range, over) = scanCurrentNumberToken(index, charIterator)

                        over?.let {
                            buffer.add(it)
                        }

                        val value = s.slice(range.first until range.second).toInt()
                        Triple(NumberToken(value), range.first, range.second)
                    }
                    else -> {
                        throw Exception("eerror!")
                    }
                }
            )
        }
    }
}
