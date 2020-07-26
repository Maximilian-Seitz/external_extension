package de.maxi_seitz.demo

import kotlin.math.sqrt

data class Vector2D(
		val x: Float,
		val y: Float
) : Vector {
	override val length: Float
		get() = sqrt(x*x + y*y)
}