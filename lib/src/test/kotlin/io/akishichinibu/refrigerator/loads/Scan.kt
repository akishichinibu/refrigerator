package io.akishichinibu.refrigerator.loads

import org.junit.Test
import io.akishichinibu.refrigerator.ast.scanner.parser

class Scan {

    @Test
    fun test1() {
        val s = "{\"a\": 1, \"b\": \"hello\", \"c\": 111, \"d\": true }"

        for (r in parser(s, arrayOf())) {
            println(r)
        }
    }

}