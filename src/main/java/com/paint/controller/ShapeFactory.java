package com.paint.controller;

import javafx.scene.shape.Line;

public class ShapeFactory {

    public static Line createLine(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        return line;
    }


}
