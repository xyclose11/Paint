package com.paint.controller;

import com.paint.model.PaintStateModel;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public class ToolController {
    private PaintStateModel paintStateModel;
    private GraphicsContext graphicsContext;

    private double startX = 0;
    private double startY = 0;

    public void handleToolGeneralOnPress(Shape currentShape, String currentTool, MouseEvent mouseEvent, Pane drawingPane) {
        switch (currentTool){
            case "Eraser":
                graphicsContext.clearRect(startX, startY, 60, 60);
                break;
            case "colorGrabber":
                // Get the color of the pixel under the mouse
                WritableImage image = graphicsContext.getCanvas().snapshot(null, null);
                PixelReader pixelReader = image.getPixelReader();
                Color selectedColor = pixelReader.getColor((int) mouseEvent.getX(), (int) mouseEvent.getY());
                this.paintStateModel.setCurrentPaintColor(selectedColor);
                break;
            case "TextTool": // Create a textarea for user to type into and use that for String input -> strokeText
                TextArea textArea = new TextArea();
                textArea.setPrefColumnCount(6);
                textArea.setPrefRowCount(1);
                textArea.setWrapText(true);
                textArea.setBackground(null);
                textArea.setLayoutX(mouseEvent.getX()); // Places the text area
                textArea.setLayoutY(mouseEvent.getY() - 15);


                // listener to alert whenever the text area is in focus
                textArea.focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
//                    textArea.setFont();
                }));


                textArea.requestFocus();

                drawingPane.getChildren().add(textArea);

                textArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.ESCAPE) {
                            // Apply text to canvas
                            applyTextToCanvas(textArea.getText(), mouseEvent.getX(), mouseEvent.getY());

                            // Remove text area from pane
                            drawingPane.getChildren().clear();
                        }
                    }
                });

                break;

        }

        if (currentShape != null) {
            loadDefaultShapeAttributes(currentShape);
        }
    }

    private void loadDefaultShapeAttributes(Shape currentShape) {
        currentShape.setStroke(this.paintStateModel.getCurrentPaintColor()); // This controls the outline color
        currentShape.setStrokeWidth(this.paintStateModel.getCurrentShapeLineStrokeWidth());
        currentShape.setFill(null); // Set this to null to get 'outline' of shapes
        currentShape.setMouseTransparent(false);
        currentShape.setStrokeType(StrokeType.OUTSIDE);

        // Set current shape in model
        this.paintStateModel.setCurrentShape(currentShape);
    }

    public void applyTextToCanvas(String text, double x, double y) {
        graphicsContext.strokeText(text, x, y);
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    public PaintStateModel getPaintStateModel() {
        return paintStateModel;
    }

    public void setPaintStateModel(PaintStateModel paintStateModel) {
        this.paintStateModel = paintStateModel;
    }
}
