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
    public Star(int numPoints, double centerX, double centerY, double innerRadius, double outerRadius) {
        super();
        updateStar(numPoints, centerX, centerY, innerRadius, outerRadius);
    }

    public void updateStar(int numPoints, double centerX, double centerY, double innerRadius, double outerRadius) {
        getPoints().clear();

        double angleStep = 2 * Math.PI / (numPoints * 2);

        // Loop through twice the number of points (alternating inner and outer vertices)
        for (int i = 0; i < numPoints * 2; i++) {
            // Calculate the angle for this vertex
            double angle = i * angleStep;

            // Alternate between outer and inner radius
            double r = (i % 2 == 0) ? outerRadius : innerRadius;

            // Calculate the x and y coordinates for this vertex
            double x = centerX + r * Math.cos(angle);
            double y = centerY + r * Math.sin(angle);

            // Add the vertex to the polygon
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
