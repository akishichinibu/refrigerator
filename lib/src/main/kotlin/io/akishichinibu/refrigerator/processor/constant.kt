package io.akishichinibu.refrigerator.processor

const val DUMPS_FUNCTION_NAME: String = "rDumps"
const val LOADS_FUNCTION_NAME: String = "rLoads"

const val INSTANCE_RECEIVER_NAME: String = "inst"
const val GENERATED_FILE_SUFFIX: String = "Refrigerate"


enum class JSONTokenType {
    BEGIN_OBJECT,
    END_OBJECT,
    BEGIN_ARRAY,
    END_ARRAY,
    NULL,
    NUMBER,
    STRING,
    BOOLEAN,
    SEP_COLON,
    SEP_COMMA,
}

const val NULL_LITERAL = "null"
const val FALSE_LITERAL = "false"
const val TRUE_LITERAL = "true"
const val COMMA_LITERAL = ","
const val COLON_LITERAL = ":"
const val BEGIN_OBJECT_LITERAL = "{"
const val END_OBJECT_LITERAL = "}"
const val BEGIN_ARRAY_LITERAL = "["
const val END_ARRAY_LITERAL = "]"

