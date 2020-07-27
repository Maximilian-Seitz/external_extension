package org.kotlin.test

import de.maxi_seitz.demo.*
import org.junit.Assert.assertEquals
import org.junit.Test

class AnnotationTest {
	private val unitVectors = mapOf(
			1 to Vector1D(1f),
			2 to Vector2D(1f, 1f),
			3 to Vector3D(1f, 1f, 1f)
	)
	
	@Test fun simpleTest() {
		assertEquals(unitVectors[1]?.inverse(), Vector1D(-1f))
		assertEquals(unitVectors[2]?.inverse(), Vector2D(-1f, -1f))
		assertEquals(unitVectors[3]?.inverse(), Vector3D(-1f, -1f, -1f))
	}
	
	@Test fun parameterTest() {
		val x = 3f
		
		assertEquals(unitVectors[1]?.scale(x), Vector1D(x))
		assertEquals(unitVectors[2]?.scale(x), Vector2D(x, x))
		assertEquals(unitVectors[3]?.scale(x), Vector3D(x, x, x))
	}
}
