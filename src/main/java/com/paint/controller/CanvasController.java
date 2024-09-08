package com.paint.controller;

import com.paint.model.CanvasModel;
import com.paint.model.InfoCanvasModel;
import com.paint.model.PaintStateModel;
import com.paint.resource.RightTriangle;
import com.paint.resource.Triangle;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CanvasController {
    @FXML
    Canvas mainCanvas;

    @FXML
    private Group canvasGroup;

    @FXML
    // Used to house all Shape objects that are drawn
    private Pane drawingPane;

    @FXML
    private StackPane canvasDrawingStackPane;

    private GraphicsContext graphicsContext;
    private PaintStateModel paintStateModel;
    private CanvasModel canvasModel;
    private InfoCanvasModel infoCanvasModel;

    public void setInfoCanvasModel(InfoCanvasModel infoCanvasModel) {
        this.infoCanvasModel = infoCanvasModel;
        // Initialize resolution label
        this.infoCanvasModel.setResolutionLblText(canvasModel.getCanvasWidth(), canvasModel.getCanvasHeight());
    }

    // Handles zoom state
    private double scaleFactor = 1;

    public void setCanvasModel(CanvasModel canvasModel) {
        this.canvasModel = canvasModel;
        this.canvasModel.setCanvasGroup(canvasGroup);
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

    // DRAWING EVENT HANDLERS SECTION START
    // stores the mouse starting POS
    double startX = 0;
    double startY = 0;

    @FXML
    private void handleMousePressed(MouseEvent mouseEvent) {
        startX = mouseEvent.getX();
        startY = mouseEvent.getY();

        String currentTool = this.paintStateModel.getCurrentTool();
        String currentToolType = this.paintStateModel.getCurrentToolType();
        Shape currentShape = this.paintStateModel.getCurrentShape();

        if (!this.paintStateModel.isTransformable()) {
            switch (currentToolType) {
                case "shape":
                    handleToolShapeOnPress(currentShape, currentTool);
                    break;
                case "brush":
                    handleToolBrushOnPress();
                    break;
            }
        }

    }

    private void handleToolShapeOnPress(Shape currentShape, String currentTool) {
        switch (currentTool) {
            case "StLine":
                currentShape = new Line(startX, startY, startX + 1, startY + 1);
                break;
            case "Rectangle":
                // x, y, width, height, Paint fill
                currentShape = new Rectangle(startX, startY, 1, 2);
                break;
            case "Circle":
                currentShape = new Circle(startX, startY, 1);
                break;
            case "Square":
                currentShape = new Rectangle(startX, startY, 1, 1);
                break;
            case "Ellipse":
                // Center X Center Y | Radius X Radius Y
                currentShape = new Ellipse(startX, startY, 1, 1);
                break;
            case "Triangle":
                currentShape = new Triangle(startX, startX, startX, startY, startY, startY);
                break;
            case "RightTriangle":
                currentShape = new RightTriangle(startX, startX, startX, startY, startY, startY);
                break;
            case "Star":
                break;
            case "Hexagon":
                break;
            case "Curve":
                timesAdjusted = 0;
                currentShape = new CubicCurve(startX, startY, startX, startY, startX + 1, startY + 1, startX + 1, startY + 1);
                break;
        }

        if (currentShape != null) {
            currentShape.setStroke(this.paintStateModel.getCurrentPaintColor()); // This controls the outline color
            currentShape.setStrokeWidth(this.paintStateModel.getCurrentShapeLineStrokeWidth());
            currentShape.setFill(null); // Set this to null to get 'outline' of shapes
            currentShape.setMouseTransparent(false);
            currentShape.setStrokeType(StrokeType.OUTSIDE);
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
                GraphicsContext gc = mainCanvas.getGraphicsContext2D(); // TODO move graphicsContext, setStroke, setLineWidth -> PaintStateModel
                gc.setStroke(this.paintStateModel.getCurrentPaintColor());
                gc.setLineWidth(this.paintStateModel.getCurrentLineWidth());
                gc.setLineCap(this.paintStateModel.getCurrentStrokeLineCap()); // Sets the line cap so that the brush appears 'circular' instead of 'square'
                gc.beginPath();
                gc.stroke();
                break;
        }
    }

    @FXML
    private void handleMouseDragged(MouseEvent mouseEvent) {
        // Verify mode
        if (this.paintStateModel.isTransformable()) {
            // If in transform mode, ignore drag events
            return;
        }

        // Update mouse POS lbl
        this.infoCanvasModel.setMousePosLbl(mouseEvent);
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
        if (this.paintStateModel.isTransformable()) {
            return;
        }

        if (currentShape == null) {
            // Error for if the currentShape is null (Ideally there should always be a tool selected)
            Alert noToolSelectedAlert = new Alert(Alert.AlertType.ERROR, "NO TOOL SELECTED. Please select a tool in the tool bar above.");
            noToolSelectedAlert.setTitle("No Tool Selected: DRAGGED");
            noToolSelectedAlert.setHeaderText("");
            noToolSelectedAlert.showAndWait();
        }


        // Check if shape is off the canvas
        if (curX >= mainCanvas.getWidth() || curY >= mainCanvas.getHeight() || curX < 0 || curY < 0) {
            return;
        }

        if (currentShape instanceof Line line) {
            line.setEndX(curX);
            line.setEndY(curY);
            return;
        }

        if (currentShape instanceof CubicCurve curve) {
            // Drag starting line -> wait for user click 1 or 2 times for control location
            curve.setEndX(curX);
            curve.setEndY(curY);
        }

        if (currentShape instanceof Triangle triangle) {
            // X1 stays same | Y1 changes | X2 changes | Y2 stays same | X3 & Y3 are the cursor
            double topVertex = ((curX - startX) / 2) + startX;
            triangle.setVertices(startX, curY, topVertex, startY, curX, curY);
        }

        if (currentShape instanceof RightTriangle rightTriangle) {
            rightTriangle.setVertices(startX, curY, curX, startY, curX, curY);
        }

        if (currentShape instanceof Rectangle rect) {
            // Check if obj is a Square
            if (Objects.equals(this.paintStateModel.getCurrentTool(), "Square")) {
                // Ensure that W x H stay the same
                double l = ((curX - startX) + (curY - startY)) / 2;
                rect.setWidth(l);
                rect.setHeight(l);
                return;
            }

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

        if (currentShape instanceof Circle circle) {

            circle.setCenterX(startX);
            circle.setCenterY(startY);
            circle.setRadius(Math.abs(curX - startX));

        }

        if (currentShape instanceof Ellipse ellipse) {
            ellipse.setCenterX(startX);
            ellipse.setCenterY(startY);
            ellipse.setRadiusX(Math.abs(curX - startX));
            ellipse.setRadiusY(Math.abs(curY - startY));
        }

        if (currentShape instanceof Polygon polygon) {

        }


    }

    private void handleToolBrushOnDragged(double curX, double curY) {
        GraphicsContext gc = mainCanvas.getGraphicsContext2D();
        gc.lineTo(curX+.5, curY+.5); // +.5 to account for antialiasing. Makes line more natural
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

    private int timesAdjusted = 0;
    private void handleToolShapeReleased(Shape currentShape) {
        // Disable StackPane Mouse Event Handlers
        setCanvasDrawingStackPaneHandlerState(false);

        // Check if currentShape is a curve
        if (currentShape instanceof CubicCurve curve) {
            // Enable mouse click handler for control XY location
            this.canvasGroup.setOnMouseClicked(event -> {
                if (timesAdjusted >= 2) {
                    curve.setControlX2(event.getX());
                    curve.setControlY2(event.getY());
                    currentShape.setPickOnBounds(true);
                    // Enable transformations
                    this.paintStateModel.setTransformable(true, drawingPane);
                    this.canvasGroup.setOnMouseClicked(null);
                } else {
                    curve.setControlX1(event.getX());
                    curve.setControlY1(event.getY());
                    timesAdjusted++;
                }
            });
        } else {
            // Allow shape to be selected via mouse select
            currentShape.setPickOnBounds(true);
            // Enable transformations
            this.paintStateModel.setTransformable(true, drawingPane);
        }
    }

    public void setCanvasDrawingStackPaneHandlerState(boolean bool) {
        if (bool) {
            this.canvasDrawingStackPane.setOnMousePressed(this::handleMousePressed);
            this.canvasDrawingStackPane.setOnMouseDragged(this::handleMouseDragged);
            this.canvasDrawingStackPane.setOnMouseReleased(this::handleMouseReleased);
            this.canvasDrawingStackPane.setOnMouseMoved(this::onMouseOverCanvas);
        } else {
            this.canvasDrawingStackPane.setOnMousePressed(null);
            this.canvasDrawingStackPane.setOnMouseDragged(null);
            this.canvasDrawingStackPane.setOnMouseReleased(null);
            this.canvasDrawingStackPane.setOnMouseMoved(null);
        }

    }
    // DRAWING SECTION END

    // TRANSLATION SECTION START
    // TRANSLATION SECTION END

    @FXML
    private void initialize() {
        // Initialize canvas sizing
        mainCanvas.widthProperty().addListener((obs, oldVal, newVal) -> updateCanvasSize());
        mainCanvas.heightProperty().addListener((obs, oldVal, newVal) -> updateCanvasSize());

        // Initialize graphics context to enable drawing
        graphicsContext = mainCanvas.getGraphicsContext2D();

        // Set default background color -> white
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
    }

    public void setCanvas(Image image) {
        double x = image.getWidth();
        double y = image.getHeight();

        // Set canvas dimensions to match image dimensions
        mainCanvas.setWidth(x);
        mainCanvas.setHeight(y);

        // Update resolution label
        this.infoCanvasModel.setResolutionLblText(x, y);

        // Get pixelReader to convert Image to a WritableImage to set the main canvas
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage(pixelReader, (int) (image.getWidth()), (int)(image.getHeight()));

        // Set canvas to the writableImage
        mainCanvas.getGraphicsContext2D().drawImage(writableImage, 0, 0);
    }

    // Takes a snapshot of the canvas & saves it to the designated file
    public void saveImageFromCanvas(File file, String fileExtension) { // TODO bind the canvas + scrollPane + drawingPane sizes since saved images currently get zoom effect perma
        WritableImage writableImage = new WritableImage((int)(mainCanvas.getWidth()), (int) (mainCanvas.getHeight()));
        // Take a snapshot of the current canvas and save it to the writableImage
        //this.mainCanvas.snapshot(null, writableImage);
        this.canvasDrawingStackPane.snapshot(null, writableImage);

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

    @FXML
    private void scaleCanvasOnScroll(ScrollEvent scrollEvent) { // TODO add CTRL + SCROLL zoom effect to infobar
        double zoomFactor = 1.1; // Handles zoom factor

        // Check if user is scrolling in or out
        if (scrollEvent.getDeltaY() > 0) {
            scaleFactor *= zoomFactor;
        } else {
            scaleFactor /= zoomFactor;
        }

        canvasGroup.setScaleX(scaleFactor);
        canvasGroup.setScaleY(scaleFactor);
    }

    // Mouse POS Handler
    @FXML
    private void onMouseOverCanvas(MouseEvent mouseEvent) {
        // Access the global method used to update Mouse POS
        this.infoCanvasModel.setMousePosLbl(mouseEvent);
    }
}
