package com.paint.resource;

import java.util.Arrays;

/**
 * This class extends Triangle to add very cool and awesome trigonometry things to ensure
 * that the less superior 'right-triangle' triangle will conform to their math laws
 *
 * @since 1.2
 * */
public class RightTriangle extends Triangle {

	/**
	 * Instantiates a new Right triangle.
	 *
	 * @param x1 the x 1
	 * @param x2 the x 2
	 * @param x3 the x 3
	 * @param y1 the y 1
	 * @param y2 the y 2
	 * @param y3 the y 3
	 */
	public RightTriangle(double x1, double x2, double x3, double y1, double y2, double y3) {
		super(x1, x2, x3, y1, y2, y3);

		// Validate if points construct a valid right triangle
		if (!isValidRT(x1, x2, x3, y1, y2, y3)) {
			System.out.println("not valid right triangle");
			throw new IllegalArgumentException("Not a valid right triangle");
		}
	}

	private boolean isValidRT(double x1, double x2, double x3, double y1, double y2, double y3) {
		// Calculate squared lengths of sides
		double a2 = Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
		double b2 = Math.pow(x2 - x3, 2) + Math.pow(y2 - y3, 2);
		double c2 = Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2);

		// Sort the lengths
		double[] sides = {a2, b2, c2};
		Arrays.sort(sides);

		// Check for Pythagorean theorem
		return Math.abs(sides[0] + sides[1] - sides[2]) < 1e-10;
	}


}
