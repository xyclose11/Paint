package com.paint.controller;

import com.paint.model.CanvasModel;
import com.paint.model.PaintStateModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CanvasController {
    @FXML
    Canvas mainCanvas;

    private GraphicsContext graphicsContext;

    private PaintStateModel paintStateModel;
    @FXML
    private Group canvasGroup;
    private CanvasModel canvasModel;

    @FXML
    // Used to house all Shape objects that are drawn
    private Pane drawingPane;

    @FXML
    private StackPane canvasDrawingStackPane;

    public void setCanvasModel(CanvasModel canvasModel) {
        this.canvasModel = canvasModel;
        updateCanvasSize();
    }

    public void setPaintStateModel(PaintStateModel paintStateModel) {
        this.paintStateModel = paintStateModel;
    }

    private void updateCanvasSize() {
        if (canvasModel != null) {
            canvasModel.setCanvasWidth(mainCanvas.getWidth());
            canvasModel.setCanvasHeight(mainCanvas.getHeight());
        }
    }
    
    // DRAWING SECTION START
    // stores the mouse starting POS
    double startX = 0;
    double startY = 0;

    // Maintain the state of the current shape being drawn
    Shape currentShape;
    @FXML
    private void handleShapeMousePressed(MouseEvent mouseEvent) {
        startX = mouseEvent.getX();
        startY = mouseEvent.getY();

        switch (this.paintStateModel.getCurrentTool()) {
            case "StLine":
                currentShape = new Line(startX, startY, startX, startY);
                break;
            case "Rectangle":
                // x, y, width, height, Paint fill
                currentShape = new Rectangle(startX, startY, 0, 0);
                break;
        }

        if (currentShape != null) {
            currentShape.setStroke(Color.BLACK);
//            currentShape.setStrokeWidth(.5);
            currentShape.setMouseTransparent(true);
            currentShape.setStrokeType(StrokeType.CENTERED);
            drawingPane.getChildren().add(currentShape);


        } else {
            // Error for if the currentShape is null (Ideally there should always be a tool selected)
            Alert noToolSelectedAlert = new Alert(Alert.AlertType.ERROR, "NO TOOL SELECTED. Please select a tool in the tool bar above.");
            noToolSelectedAlert.setTitle("No Tool Selected");
            noToolSelectedAlert.setHeaderText("");
            noToolSelectedAlert.showAndWait();
        }

        // TODO Add functionality so that when the scroll pane scrolls the canvas and drawing pane resize (BIND)
        // TODO Remove scroll pane & just add 2 separate scroll bars
    }

    // TODO MOVE MOUSE EVENT HANDLERS TO NEW PaneController AND CONVERT THESE INTO BRUSH CONTROLLERS ITF
    @FXML
    private void handleShapeMouseDragged(MouseEvent mouseEvent) {
        if (currentShape != null) {
            double curX = mouseEvent.getX();
            double curY = mouseEvent.getY();

            if (currentShape instanceof Line line) {
                line.setEndX(curX);
                line.setEndY(curY);
            } else if (currentShape instanceof Rectangle rect) {
                // Check if cursor is going in Quadrant 4 (Meaning that it doesn't require any calculation swaps)
                if (curX >= startX && curY >= startY) {
                    rect.setWidth((curX - startX));
                    rect.setHeight((curY - startY));
                    return;
                }

                // Check if cursor is in Quadrants 2 OR 3
                if (curX < startX) {
                    // Swap calculation setters
                    rect.setX(curX);
                    rect.setWidth(Math.abs(startX - curX));
                    rect.setHeight(Math.abs(curY - startY));
                }

                // Check if cursor is in Quadrants 2 OR 1
                if (curY < startY) {
                    // Swap calculation setters
                    rect.setY(curY);
                    rect.setWidth(Math.abs(curX - startX));
                    rect.setHeight(Math.abs(startY - curY));
                }


                // Used for rounded corners
//                rect.setArcHeight(20);
//                rect.setArcWidth(20);
            }
        } else {
            // Error for if the currentShape is null (Ideally there should always be a tool selected)
            Alert noToolSelectedAlert = new Alert(Alert.AlertType.ERROR, "NO TOOL SELECTED. Please select a tool in the tool bar above.");
            noToolSelectedAlert.setTitle("No Tool Selected");
            noToolSelectedAlert.setHeaderText("");
            noToolSelectedAlert.showAndWait();
        }
    }

    @FXML
    private void handleShapeMouseReleased(MouseEvent mouseEvent) {
        currentShape.setMouseTransparent(false);
        currentShape = null;
    }
    // DRAWING SECTION END

    @FXML
    private void initialize() {
        // Initialize canvas sizing
        mainCanvas.widthProperty().addListener((obs, oldVal, newVal) -> updateCanvasSize());
        mainCanvas.heightProperty().addListener((obs, oldVal, newVal) -> updateCanvasSize());

        // Initialize graphics context to enable drawing
        graphicsContext = mainCanvas.getGraphicsContext2D();
    }

    public void setCanvas(Image image) {
        // Set canvas dimensions to match image dimensions
        mainCanvas.setWidth(image.getWidth());
        mainCanvas.setHeight(image.getHeight());

        // Update

        // Get pixelReader to convert Image to a WritableImage to set the main canvas
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage(pixelReader, (int) (image.getWidth()), (int)(image.getHeight()));

        // Set canvas to the writableImage
        mainCanvas.getGraphicsContext2D().drawImage(writableImage, 0, 0);
    }

    // Takes a snapshot of the canvas & saves it to the designated file
    public void saveImageFromCanvas(File file, String fileExtension) {
        System.out.println(fileExtension);
//        if (fileExtension == "bmp" || fileExtension == "dib") {
//            saveBMPFormat(file, fileExtension);
//        }


        WritableImage writableImage = new WritableImage((int)(mainCanvas.getWidth()), (int) (mainCanvas.getHeight()));
        // Take a snapshot of the current canvas and save it to the writableImage
        //this.mainCanvas.snapshot(null, writableImage);
        this.canvasGroup.snapshot(null, writableImage);

        // Create a BufferedImage obj to store image data since BufferedImage requires an alpha channel
        BufferedImage imageData = new BufferedImage((int) writableImage.getWidth(), (int) writableImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, imageData);

        // Check if the saved image is null
        if (bufferedImage == null) {
            new Alert(Alert.AlertType.ERROR, "ERROR SAVING FILE");
            return;
        }

        try {
            // Create new file or overwrite file with same name, with designated fileExtension at the path file
            ImageIO.write(bufferedImage, fileExtension, file);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Unable to save the image at this time. Stack Trace: " + e.getMessage() );
            e.printStackTrace();
        }
    }
}
