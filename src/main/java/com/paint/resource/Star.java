package com.paint.resource;

import javafx.scene.shape.Polygon;

/**
 * This class extends Polygon and uses a bunch of confusing math concepts that I probably
 * should have remembered from trigonometry, but at the time I decided that I surely
 * would never need to know how to mathematically calculate the points for a star? Right?
 *
 * NOTE: The star is in fact off centered. Trust me. I know.
 *
 * Source: https://martiancraft.com/blog/2017/03/geometry-of-stars/
 *
 * @since 1.2
 * */
public class Star extends Polygon {
    public Star(int numPoints, double centerX, double centerY, double radius) {
        super();
        updateStar(numPoints, centerX, centerY, radius);
    }

    // Used https://math.stackexchange.com/questions/2135982/math-behind-creating-a-perfect-star for math background
    public void updateStar(int numPoints, double centerX, double centerY, double radius) {
        getPoints().clear();

        double outerRadius = radius;
        double innerRadius = radius * 0.5;

        double angleStep = Math.PI / numPoints;

        for (int i = 0; i < numPoints * 2; i++) {
            double currentRadius = (i % 2 == 0) ? outerRadius : innerRadius;

            double angle = i * angleStep;

            double x = centerX + currentRadius * Math.cos(angle);
            double y = centerY + currentRadius * Math.sin(angle);

            getPoints().addAll(x, y);
        }
    }

    public void applyTranslation(double xT, double yT) {
        for (int i = 0; i < getPoints().size(); i += 2) {
            double x = getPoints().get(i);
            double y = getPoints().get(i + 1);
            getPoints().set(i, x + xT);
            getPoints().set(i + 1, y + yT);
        }
    }

}
