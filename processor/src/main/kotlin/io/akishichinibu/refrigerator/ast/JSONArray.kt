package io.akishichinibu.refrigerator.ast

import io.akishichinibu.refrigerator.ast.token.*


class JSONArray() : Variable(), Parseable {

    private val elements: MutableList<Variable> = mutableListOf()

    fun addElement(value: Token): JSONArray {
        this.elements.add(value)
        return this
    }

    fun addElement(value: Number): JSONArray {
        return this.addElement(NumberToken(value))
    }

    fun addElement(value: String): JSONArray {
        return this.addElement(StringToken(value))
    }

    override fun parse(): Sequence<Token> {
        return sequence {
            yield(ArrayBeginToken())

            elements
                .forEach { value ->
                    when (value) {
                        is JSONArray -> yieldAll(value.parse())
                        is JSONObject -> yieldAll(value.parse())
                        is Token -> {
                            yield(value)
                        }
                        else -> {
                            throw Exception("unknown $value")
                        }
                    }
                    yield(CommaToken())
                }

            yield(ArrayEndToken())
        }
    }

}
