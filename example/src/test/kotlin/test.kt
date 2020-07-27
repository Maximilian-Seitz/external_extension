package org.kotlin.test

import de.maxi_seitz.demo.*
import org.junit.Assert.assertEquals
import org.junit.Test

class AnnotationTest {
	private val onesVector: Map<Int, Vector> = mapOf(
			1 to Vector1D(1f),
			2 to Vector2D(1f, 1f),
			3 to Vector3D(1f, 1f, 1f)
	)
	
	@Test fun simpleTest() {
		assertEquals(onesVector[1]?.inverse(), Vector1D(1f).inverse())
		assertEquals(onesVector[2]?.inverse(), Vector2D(1f, 1f).inverse())
		assertEquals(onesVector[3]?.inverse(), Vector3D(1f, 1f, 1f).inverse())
	}
	
	@Test fun parameterTest() {
		val x = 3f
		
		assertEquals(onesVector[1]?.scale(x), Vector1D(1f).scale(x))
		assertEquals(onesVector[2]?.scale(x), Vector2D(1f, 1f).scale(x))
		assertEquals(onesVector[3]?.scale(x), Vector3D(1f, 1f, 1f).scale(x))
	}
	
	@Test fun alternateNameTest() {
		val l = 3f
		
		assertEquals(onesVector[1]?.scaleToLength(l), Vector1D(1f).withLength(l))
		assertEquals(onesVector[2]?.scaleToLength(l), Vector2D(1f, 1f).scaleToLength(l))
		assertEquals(onesVector[3]?.scaleToLength(l), Vector3D(1f, 1f, 1f).scaleToLength(l))
	}
}
