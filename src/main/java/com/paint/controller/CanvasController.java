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
import javafx.scene.shape.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CanvasController {
    @FXML
    Canvas mainCanvas;

    private GraphicsContext graphicsContext;

    private BrushController brushController;

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

    // BRUSH EVENT HANDLER START
    // TODO move these to BrushController after testing
    @FXML
    private void onMouseDownBrush(MouseEvent event) {
        // TODO verify that the selected tool is a brush
        System.out.println(this.paintStateModel.getCurrentBrush());
        switch (this.paintStateModel.getCurrentBrush()) {
            case ("regular"):
                GraphicsContext gc = mainCanvas.getGraphicsContext2D();

                  gc.setStroke(Color.BLACK);
                  gc.beginPath();
                break;
        }
    }

    @FXML
    private void onMouseDraggedBrush(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

//        mainCanvas.getGraphicsContext2D().moveTo(x, y);
//        mainCanvas.getGraphicsContext2D().stroke();
//        mainCanvas.getGraphicsContext2D().setLineWidth(2);
//        currentShape.setC

    }

    @FXML
    private void onMouseReleaseBrush(MouseEvent event) {
//        mainCanvas.getGraphicsContext2D().closePath();
        // TODO set it so that after the path is done being created it gets "saved"
    }
    // BRUSH EVENT HANDLER END

    // Maintain the state of the current shape being drawn
//    Shape currentShape;
    @FXML
    private void handleMousePressed(MouseEvent mouseEvent) {
        startX = mouseEvent.getX();
        startY = mouseEvent.getY();
        String currentTool = this.paintStateModel.getCurrentTool();
        String currentToolType = this.paintStateModel.getCurrentToolType();
        Shape currentShape = this.paintStateModel.getCurrentShape();

        switch (currentToolType) {
            case "shape":
                handleToolShapeOnPress(currentShape, currentTool);
                break;
            case "brush":
                handleToolBrushOnPress();
                break;
        }
        // TODO Add functionality so that when the scroll pane scrolls the canvas and drawing pane resize (BIND)
        // TODO Remove scroll pane & just add 2 separate scroll bars
    }

    private void handleToolShapeOnPress(Shape currentShape, String currentTool) {
        switch (currentTool) {
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
            currentShape.setMouseTransparent(true);
            currentShape.setStrokeType(StrokeType.CENTERED);
            drawingPane.getChildren().add(currentShape);

            // Set current shape in model
            this.paintStateModel.setCurrentShape(currentShape);
        } else {
            // Error for if the currentShape is null (Ideally there should always be a tool selected)
            Alert noToolSelectedAlert = new Alert(Alert.AlertType.ERROR, "NO TOOL SELECTED. Please select a tool in the tool bar above.");
            noToolSelectedAlert.setTitle("No Tool Selected");
            noToolSelectedAlert.setHeaderText("");
            noToolSelectedAlert.showAndWait();
        }
    }

    private void handleToolBrushOnPress() {
        switch (this.paintStateModel.getCurrentBrush()) {
            case "regular":
                GraphicsContext gc = mainCanvas.getGraphicsContext2D();

                gc.setStroke(Color.BLACK);
                gc.beginPath();
                break;
        }
    }

    // TODO MOVE MOUSE EVENT HANDLERS TO NEW PaneController AND CONVERT THESE INTO BRUSH CONTROLLERS ITF
    @FXML
    private void handleMouseDragged(MouseEvent mouseEvent) {
        Shape currentShape = this.paintStateModel.getCurrentShape();
        String currentToolType = this.paintStateModel.getCurrentToolType();

        double curX = mouseEvent.getX();
        double curY = mouseEvent.getY();

        switch (currentToolType) {
            case "shape":
                handleToolShapeOnDragged(currentShape, curX, curY);
                break;
            case "brush":
                handleToolBrushOnDragged(curX, curY);
                break;
        }



        // Set currentShape in model
        this.paintStateModel.setCurrentShape(currentShape);
    }

    private void handleToolShapeOnDragged(Shape currentShape, double curX, double curY) {

        if (currentShape != null) {
            if (currentShape instanceof Line line) {
                line.setEndX(curX);
                line.setEndY(curY);
            } else if (currentShape instanceof Rectangle rect) {
//                rect.getStyleClass().add("my-rect");
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

            }

        } else {
            // Error for if the currentShape is null (Ideally there should always be a tool selected)
            Alert noToolSelectedAlert = new Alert(Alert.AlertType.ERROR, "NO TOOL SELECTED. Please select a tool in the tool bar above.");
            noToolSelectedAlert.setTitle("No Tool Selected: DRAGGED");
            noToolSelectedAlert.setHeaderText("");
            noToolSelectedAlert.showAndWait();
        }

    }

    private void handleToolBrushOnDragged(double curX, double curY) {
        GraphicsContext gc = mainCanvas.getGraphicsContext2D();
        gc.lineTo(curX, curY);
        gc.stroke();
    }

    @FXML
    private void handleMouseReleased(MouseEvent mouseEvent) {
        String currentToolType = this.paintStateModel.getCurrentToolType();
        switch (currentToolType) {
            case ("shape"):
                handleToolShapeReleased(this.paintStateModel.getCurrentShape());
                break;
            case ("brush"):
                // TBD
                break;
        }


    }

    private void handleToolShapeReleased(Shape currentShape) {
        currentShape.setMouseTransparent(false);
        this.paintStateModel.setCurrentShape(null);

        // TODO translate the shape into a canvas obj
        GraphicsContext gc = mainCanvas.getGraphicsContext2D();

        if (currentShape instanceof Rectangle rect) {
            gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        }
    }
    // DRAWING SECTION END

    @FXML
    private void initialize() {
        // Initialize canvas sizing
        mainCanvas.widthProperty().addListener((obs, oldVal, newVal) -> updateCanvasSize());
        mainCanvas.heightProperty().addListener((obs, oldVal, newVal) -> updateCanvasSize());

        // Initialize graphics context to enable drawing
        graphicsContext = mainCanvas.getGraphicsContext2D();
        brushController = new BrushController(graphicsContext);

        // Set default background color -> white
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
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
