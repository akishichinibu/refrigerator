package io.akishichinibu.refrigerator.ast

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import io.akishichinibu.refrigerator.INSTANCE_RECEIVER_NAME
import io.akishichinibu.refrigerator.JSONTokenType
import io.akishichinibu.refrigerator.ast.token.*


class JSONObject() : Variable(), Parseable {

    private val entities: MutableList<Pair<StringToken, Variable>> = mutableListOf()

    private inline fun addEntry(key: StringToken, value: Token): JSONObject {
        this.entities.add(Pair(key, value))
        return this
    }

    fun addEntry(key: String, value: Token): JSONObject {
        return this.addEntry(StringToken(key), value)
    }

    fun addEntry(key: String, value: String): JSONObject {
        return this.addEntry(StringToken(key), StringToken(value))
    }

    fun addPropertyEntry(property: KSPropertyDeclaration): JSONObject {
        val propertyName = property.simpleName.asString()
        val type = property.type.toString()
        val ref = "$INSTANCE_RECEIVER_NAME.$propertyName"

        val valueType = when (type) {
            "String" -> JSONTokenType.STRING
            "Int" -> JSONTokenType.NUMBER
            "Boolean" -> JSONTokenType.BOOLEAN
            else -> {
                throw Exception("Unknown value type $type")
            }
        }

        val refToken = PropertyReferenceToken(valueType, ref)
        return this.addEntry(propertyName, refToken)
    }

    override fun parse(): Sequence<Token> {
        val n = entities.size
        return sequence {
            yield(ObjectBeginToken())

            entities
                .forEachIndexed { index, (key, value) ->
                    yield(key)
                    yield(ColonToken())

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

                    if (index < n - 1) {
                        yield(CommaToken())
                    }
                }

            yield(ObjectEndToken())
        }
    }

}
