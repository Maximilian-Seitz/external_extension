package org.kotlin.test

import de.maxi_seitz.demo.*
import org.junit.Assert.assertEquals
import org.junit.Test

class AnnotationTest {
	@Test fun testSimple() {
		val unitVectors = listOf(
				Vector1D(1f),
				Vector2D(1f, 1f),
				Vector3D(1f, 1f, 1f)
		)
		
		assertEquals(Vector1D(-1f), unitVectors[0].inverse())
		assertEquals(Vector2D(-1f, -1f), unitVectors[1].inverse())
		assertEquals(Vector3D(-1f, -1f, -1f), unitVectors[2].inverse())
		
		//Just for a nice output
		unitVectors.forEach {
			println(it)
			println(it.inverse())
		}
	}
}
