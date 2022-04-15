package io.akishichinibu.refrigerator.processor.extend

import io.akishichinibu.refrigerator.FALSE_LITERAL
import io.akishichinibu.refrigerator.TRUE_LITERAL

inline fun quoteIt(it: String): String {
    return "\"$it\""
}

inline fun refIt(it: String): String {
    return quoteIt("\${${it}}")
}

inline fun stringifyIt(it: String): String {
    return "${it}.toString()"
}

inline fun forBooleanRef(it: String): String {
    return "(if (${it}) { ${quoteIt(TRUE_LITERAL)} } else { ${quoteIt(FALSE_LITERAL)} })"
}

inline fun Sequence<String>.quote(): Sequence<String> {
    return this.map { quoteIt(it) }
}

inline fun Sequence<Char>.escapeMap(): Sequence<Char> {
    val iter = this@escapeMap.iterator()

    return sequence {
        while (iter.hasNext()) {
            val r = when (val c = iter.next()) {
                '\\' -> {
                    if (iter.hasNext()) {
                        val ec = iter.next()
                        ec
                    } else {
                        throw Exception("error")
                    }
                }
                else -> c
            }

            yield(r)
        }
    }
}
