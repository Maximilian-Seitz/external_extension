package de.maxi_seitz.annotation.processor.externalExtension

import org.jetbrains.annotations.NotNull
import javax.lang.model.element.Element

val Element.isNullable: Boolean
	get() = getAnnotation(NotNull::class.java) == null