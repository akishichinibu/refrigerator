package io.akishichinibu.refrigerator.processor.ast.token

import io.akishichinibu.refrigerator.JSONTokenLiteral
import io.akishichinibu.refrigerator.JSONTokenType

class ObjectBeginToken() : LiteralToken(JSONTokenType.BEGIN_OBJECT, JSONTokenLiteral.BEGIN_OBJECT) {}
