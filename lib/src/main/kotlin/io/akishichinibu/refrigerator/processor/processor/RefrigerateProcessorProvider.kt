package io.akishichinibu.refrigerator.processor.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider


class RefrigerateProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return RefrigerateProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
    }
}
