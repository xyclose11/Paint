package com.paint.view;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class CanvasView{
	private HBox layout;
	private ScrollPane scrollPane;
	private Canvas canvas;
	private StackPane stackPane;
	private ImageView imageView;

	public CanvasView() {
//		super();
		canvas = new Canvas();
		layout = new HBox();
		scrollPane = new ScrollPane();
		stackPane = new StackPane();
		imageView = new ImageView();

		canvas.setHeight(300);
		canvas.setWidth(600);
		canvas.autosize();
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.setFill(Color.BLACK);

		WritableImage writableImage = new WritableImage(300, 600);
		imageView.setImage(writableImage);

		stackPane.getChildren().addAll(canvas, imageView);
		HBox hBox = new HBox(stackPane);

		scrollPane.setContent(hBox);
		layout.getChildren().add(scrollPane);
		layout.setFillHeight(false);
		layout.setAlignment(Pos.CENTER);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public HBox getLayout() {
		return layout;
	}

	public void setLayout(HBox layout) {
		this.layout = layout;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public ScrollPane getScrollPane() {
		return scrollPane;
	}

	public StackPane getStackPane() {
		return stackPane;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public void setScrollPane(ScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public void setStackPane(StackPane stackPane) {
		this.stackPane = stackPane;
	}

	public void setImageViewImage(Image image) {
		this.imageView.setImage(image);
	}
}
