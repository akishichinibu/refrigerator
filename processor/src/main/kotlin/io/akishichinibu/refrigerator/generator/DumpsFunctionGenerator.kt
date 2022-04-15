package io.akishichinibu.refrigerator.generator

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import io.akishichinibu.refrigerator.DUMPS_FUNCTION_NAME
import io.akishichinibu.refrigerator.INSTANCE_RECEIVER_NAME
import io.akishichinibu.refrigerator.ast.JSONObject
import io.akishichinibu.refrigerator.extend.generatePrintableStringStream
import io.akishichinibu.refrigerator.extend.plusAssign
import java.io.OutputStream


class DumpsFunctionGenerator(
    file: OutputStream,
    logger: KSPLogger,
) : AbstractGenerator(file, logger) {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val className = classDeclaration.simpleName.asString()

        val objectToken = JSONObject()

        classDeclaration
            .getAllProperties()
            .filter { it.validate() }
            .forEach {
                objectToken.addPropertyEntry(it)
            }

        file += """
inline fun <T: $className> $DUMPS_FUNCTION_NAME($INSTANCE_RECEIVER_NAME: T): String {
    return arrayOf(
        ${
            objectToken.parse()
                .generatePrintableStringStream()
                .joinToString(", \n")
        }
    ).joinToString("")
}
"""
    }

}
