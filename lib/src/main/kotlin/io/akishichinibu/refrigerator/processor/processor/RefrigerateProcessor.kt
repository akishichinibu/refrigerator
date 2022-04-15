package io.akishichinibu.refrigerator.processor.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.akishichinibu.refrigerator.GENERATED_FILE_SUFFIX
import io.akishichinibu.refrigerator.annotation.Refrigerate
import io.akishichinibu.refrigerator.extend.plusAssign
import io.akishichinibu.refrigerator.generator.DumpsFunctionGenerator
import io.akishichinibu.refrigerator.generator.LoadsFunctionGenerator
import kotlin.streams.asStream


class RefrigerateProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private inline fun info(vararg args: String) {
        this.logger.warn("[@@@] ${args.joinToString(" ")}")
    }

    private inline fun error(vararg args: String) {
        this.logger.error("[@@@] ${args.joinToString(" ")}")
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotationName = Refrigerate::class.qualifiedName!!
        resolver
            .getSymbolsWithAnnotation(annotationName, false)
            .filterIsInstance<KSClassDeclaration>()
            .asStream()
            .parallel()
            .forEach {
                val packageName = it.packageName.asString()
                val className = it.simpleName.asString()
                this.info("Found an annotated class [$className] in $packageName")

                val generatedFileName = "${className}$GENERATED_FILE_SUFFIX"
                val dependencies = Dependencies(true, it.containingFile!!)

                codeGenerator
                    .createNewFile(
                        dependencies = dependencies,
                        packageName = packageName,
                        fileName = generatedFileName,
                    )
                    .let { file ->
                        file += """
package $packageName

import kotlin.reflect.KClass
"""
                        it.accept(DumpsFunctionGenerator(file, this.logger), Unit)
                        it.accept(LoadsFunctionGenerator(file, this.logger), Unit)
                        file.close()
                    }
            }
        return listOf()
    }
}
