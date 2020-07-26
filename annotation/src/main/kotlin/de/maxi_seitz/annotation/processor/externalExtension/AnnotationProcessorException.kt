package de.maxi_seitz.annotation.processor.externalExtension

import javax.annotation.processing.AbstractProcessor

class AnnotationProcessorException(message: String) : Exception(message)

fun AbstractProcessor.err(message: String): Nothing = throw AnnotationProcessorException(message)