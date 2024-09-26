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
    public Star(double centerX, double centerY, double radius) {
        super();
        updateStar(centerX, centerY, radius);
    }

    public void updateStar(double centerX, double centerY, double radius) {
        getPoints().clear();

        double[] xPoints = new double[10];
        double[] yPoints = new double[10];

        for (int i = 0; i < 10; i++) {
            double angle = i * Math.PI / 5;
            double r = (i % 2 == 0) ? radius : radius / 2;
            xPoints[i] = centerX + r * Math.cos(angle);
            yPoints[i] = centerY - r * Math.sin(angle);
        }

        for (int i = 0; i < 10; i++) {
            getPoints().addAll(xPoints[i], yPoints[i]);
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
