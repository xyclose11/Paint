package com.paint.controller;

import com.paint.model.PaintStateModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public class ToolController {
    private PaintStateModel paintStateModel;
    private GraphicsContext graphicsContext;

    private double startX = 0;
    private double startY = 0;

    public void handleToolGeneralOnPress(Shape currentShape, String currentTool, MouseEvent mouseEvent) {
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
