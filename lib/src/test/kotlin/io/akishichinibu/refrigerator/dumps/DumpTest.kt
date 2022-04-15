package io.akishichinibu.refrigerator.dumps

import org.junit.Test
import io.akishichinibu.refrigerator.Student
import io.akishichinibu.refrigerator.Teacher
import io.akishichinibu.refrigerator.toJsonString

class DumpTest {

    @Test
    fun test1() {
        val s = Student(name = "a", number = 2, isValid = true)
        val t = Teacher(name = "a", hasStudent = false)
        println(toJsonString(s))
        println(toJsonString(t))
    }

}
