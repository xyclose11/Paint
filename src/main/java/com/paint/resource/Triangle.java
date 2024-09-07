package com.paint.resource;

import javafx.scene.shape.Line;

public class Triangle extends Line {
    private Line a;
    private Line b;
    private Line c;

    public Triangle() {
        super();
        this.a = new Line();
        this.b = new Line();
        this.c = new Line();
    }

    public Triangle(double x, double y, double a, double b, double c) {
        super();
        this.a = new Line(x,y,a,a);

    }


}
