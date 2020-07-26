package de.maxi_seitz.demo

import kotlin.math.sqrt

data class Vector3D(
		val x: Float,
		val y: Float,
		val z: Float
) : Vector {
	override val length: Float
		get() = sqrt(x*x + y*y + z*z)
}