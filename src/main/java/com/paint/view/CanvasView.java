package com.paint.view;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CanvasView{
	private HBox hLayout;
	public VBox vLayout;
	private ScrollPane scrollPane; // TODO replace this with a regular scrollbar for vertical & horizontal to avoid current layout issues
	private Canvas canvas;
	private WritableImage writableImage;

	public CanvasView() {
		canvas = new Canvas();
		hLayout = new HBox();
		vLayout = new VBox();
		scrollPane = new ScrollPane();

		// Set initial canvas dimensions
		canvas.setHeight(1170);
		canvas.setWidth(1475);

		scrollPane.setContent(canvas);

		vLayout.getChildren().add(scrollPane);
		vLayout.setAlignment(Pos.CENTER);
		vLayout.setFillWidth(true);

		// Bind vLayout's width to allow vLayout's children to adjust hLayouts size
		hLayout.prefWidthProperty().bind(vLayout.widthProperty());
		hLayout.getChildren().addAll(vLayout);
		hLayout.setFillHeight(true);
		hLayout.setAlignment(Pos.CENTER);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public HBox getLayout() {
		return hLayout;
	}

	public void setLayout(HBox layout) {
		this.hLayout = layout;
	}


	public ScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(ScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public void setWritableImage(WritableImage writableImage) {
		this.writableImage = writableImage;
	}

	public WritableImage getWritableImage() {
		return writableImage;
	}

	public VBox getVLayout() {
		return vLayout;
	}

	public void setVLayout(VBox vLayout) {
		this.vLayout = vLayout;
	}
}
