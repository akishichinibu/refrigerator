package io.akishichinibu.refrigerator.processor.generator

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.akishichinibu.refrigerator.LOADS_FUNCTION_NAME
import io.akishichinibu.refrigerator.extend.plusAssign
import java.io.OutputStream

class LoadsFunctionGenerator(
    file: OutputStream,
    logger: KSPLogger,
) : AbstractGenerator(file, logger) {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val classPackageName = classDeclaration.packageName.asString()
        val className = classDeclaration.simpleName.asString()

        file += """
inline fun <T: $className> $LOADS_FUNCTION_NAME(clazz: KClass<T>, s: String): T {
    throw Exception()
}
"""
    }

}
