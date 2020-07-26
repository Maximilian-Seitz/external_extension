package de.maxi_seitz.demo

import de.maxi_seitz.annotation.ExternalExtension

@ExternalExtension(Vector::class)
fun Vector1D.scale(multiplier: Float): Vector = Vector1D(multiplier * length)

@ExternalExtension(Vector::class)
fun Vector2D.scale(multiplier: Float): Vector = Vector2D(multiplier * x, multiplier * y)

@ExternalExtension(Vector::class)
fun Vector3D.scale(multiplier: Float): Vector = Vector3D(multiplier * x, multiplier * y, multiplier * z)
