package de.maxi_seitz.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ExternalExtension(val target: KClass<*>, val superName: String = "")