package de.maxi_seitz.annotation.processor.externalExtension

import javax.lang.model.element.ExecutableElement

typealias AnnotatedFunction<Annotation> = Pair<Annotation, ExecutableElement>