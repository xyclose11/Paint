package com.paint.view;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CanvasView{
	private HBox rootContainer;
	private ScrollPane scrollPane;
	private Canvas canvas;

	public CanvasView() {
//		super();
		canvas = new Canvas();
		rootContainer = new HBox();
		scrollPane = new ScrollPane();
	}

//	@Override
	public HBox Init() {
		canvas.setHeight(300);
		canvas.setWidth(600);
		HBox hBox = new HBox(canvas);

		scrollPane.setContent(hBox);
		rootContainer.getChildren().add(scrollPane);
		rootContainer.setFillHeight(false);
		rootContainer.setAlignment(Pos.CENTER);
		return rootContainer;
	}
}
