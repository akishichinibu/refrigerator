package io.akishichinibu.refrigerator

import io.akishichinibu.refrigerator.ast.JSONArray
import io.akishichinibu.refrigerator.ast.JSONObject
import kotlin.test.Test

class LibraryTest {
    @Test fun someLibraryMethodReturnsTrue() {

        val t = JSONObject().apply {
            addEntry("a", "hello")
            addEntry("b", 12345)
            addEntry("c", JSONArray().apply {
                addElement("hello")
                addElement(12345)
            })
        }

        println(t.dumps().joinToString(", "))
    }
}
