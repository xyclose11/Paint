package com.test;

import com.paint.resource.RightTriangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RightTriangleTest {
	@Test
	public void testValidRightTriangle() {
		// This should create a valid right triangle and not throw an exception
		assertDoesNotThrow(() -> {
			new RightTriangle(0, 3, 0, 0, 0, 4);
		});
	}

	@Test
	public void testPythagoreanTriple() {
		// check valid right triangle
		assertDoesNotThrow(() -> {
			new RightTriangle(0, 5, 0, 0, 0, 12);
		});
	}

}
