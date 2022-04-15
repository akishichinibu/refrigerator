package io.akishichinibu.refrigerator.processor.extend

import java.io.OutputStream

inline operator fun OutputStream.plusAssign(str: String) {
    this@plusAssign.write(str.toByteArray())
}
