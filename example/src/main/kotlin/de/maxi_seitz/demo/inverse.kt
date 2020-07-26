package de.maxi_seitz.demo

import de.maxi_seitz.annotation.ExternalExtension

@ExternalExtension(Vector::class)
fun Vector1D.inverse(): Vector = Vector1D(-length)

@ExternalExtension(Vector::class)
fun Vector2D.inverse(): Vector = Vector2D(-x, -y)

@ExternalExtension(Vector::class)
fun Vector3D.inverse(): Vector = Vector3D(-x, -y, -z)
