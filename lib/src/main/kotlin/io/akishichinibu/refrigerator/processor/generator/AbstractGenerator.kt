package io.akishichinibu.refrigerator.processor.generator

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.io.OutputStream

abstract class AbstractGenerator(
    protected val file: OutputStream,
    protected val logger: KSPLogger,
) : KSVisitorVoid() {

    protected inline fun info(vararg args: String) {
        this.logger.warn("[@@@] ${args.joinToString(" ")}")
    }

    protected inline fun error(vararg args: String) {
        this.logger.error("[@@@] ${args.joinToString(" ")}")
    }

}
