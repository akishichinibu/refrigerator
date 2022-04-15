package io.akishichinibu.refrigerator.processor.ast.scanner

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import io.akishichinibu.refrigerator.JSONTokenType
import io.akishichinibu.refrigerator.ast.SchemaException
import io.akishichinibu.refrigerator.ast.SyntaxException
import io.akishichinibu.refrigerator.ast.token.*
import java.util.*


inline fun syntaxScanner(tokenStream: Sequence<Triple<Token, Int, Int>>, classDeclaration: KSClassDeclaration): Sequence<Token> {
    val targetModel = classDeclaration
        .getAllProperties()
        .filter { it.validate() }
        .map {
            Pair(it.simpleName.asString(), it.type.toString())
        }
        .toMap()

    val iter = tokenStream.iterator()
    val stack = Stack<Token>()

    return sequence {
        while (iter.hasNext()) {
            val (token, start, end) = iter.next()
            val top = stack.top()

            when (token) {
                is ObjectBeginToken -> {
                    stack.add(token)
                }
                is ObjectEndToken -> {
                    stack.pop()
                }
                is ColonToken -> {
                    if (top.type != JSONTokenType.STRING) {
                        throw SyntaxException(start, end, "require a string before a colon but get a $top")
                    }
                    stack.push(token)
                }
                is CommaToken -> {
                    if (stack.top().type != JSONTokenType.BEGIN_OBJECT) {
                        throw SyntaxException(start, end, "the comma should at the end of a key value pair")
                    }
                    stack.pop()
                }
                is StringToken -> {
                    when (top.type) {
                        JSONTokenType.BEGIN_OBJECT -> {
                            // is a key
                            if (!targetModel.contains(token.value)) {
                                throw SchemaException(start, end, "Unknown key ${token.value}")
                            }
                            stack.add(token)
                        }
                        JSONTokenType.SEP_COLON -> {
                            // is a value
                            // pop twice here is safe
                            stack.pop()
                            val keyToken = stack.pop() as StringToken
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
                }
                else -> {
                    throw Exception("")
                }
            }
        }
    }
}
