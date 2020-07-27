package de.maxi_seitz.demo

import de.maxi_seitz.annotation.ExternalExtension

@ExternalExtension(Vector::class, superName = "scaleToLength")
fun Vector1D.withLength(newLength: Float): Vector = Vector1D(length)

@ExternalExtension(Vector::class)
fun Vector2D.scaleToLength(newLength: Float): Vector = scale(newLength/length)

@ExternalExtension(Vector::class)
fun Vector3D.scaleToLength(newLength: Float): Vector = scale(newLength/length)
