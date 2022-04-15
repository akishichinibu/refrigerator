package io.akishichinibu.refrigerator.extend

import java.io.OutputStream

inline operator fun OutputStream.plusAssign(str: String) {
    this@plusAssign.write(str.toByteArray())
}
