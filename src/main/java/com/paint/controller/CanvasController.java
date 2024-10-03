package com.paint.controller;

import com.paint.handler.SelectionHandler;
import com.paint.handler.WorkspaceHandler;
import com.paint.model.*;
import com.paint.resource.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * This class is responsible for handling the UI state of a canvas
 *
 * NOTE: This is where objects/Nodes are applied to the canvas for further use
 * @since 1.0
 * */
public class CanvasController {
    private static final Logger LOGGER = LogManager.getLogger();

    @FXML
    public HBox canvasContainer;

    @FXML
    private ResizeableCanvas mainCanvas;

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
    private ToolController toolController;
    private SelectionHandler selectionHandler;
    private TabModel tabModel;
    private WorkspaceHandler workspaceHandler;

    
    private void updateCanvasSize() {
        if (canvasModel != null) {
            canvasModel.setCanvasWidth(mainCanvas.getWidth());
            canvasModel.setCanvasHeight(mainCanvas.getHeight());
            LOGGER.info("Canvas Size Updated: New Height: {} | New Width: {}", mainCanvas.getHeight(), mainCanvas.getWidth());
        }
    }

    // DRAWING EVENT HANDLERS SECTION START
    // stores the mouse starting POS
    double startX = 0;
    double startY = 0;

    @FXML
    private void handleMousePressed(MouseEvent mouseEvent) {
        LOGGER.info("Mouse Pressed on Canvas @ POS: X:{},Y:{}", mouseEvent.getX(), mouseEvent.getY());

        startX = mouseEvent.getX();
        startY = mouseEvent.getY();

        String currentTool = this.paintStateModel.getCurrentTool();
        String currentToolType = this.paintStateModel.getCurrentToolType();

        // Empty redo stack
        this.workspaceHandler.getCurrentWorkspace().getRedoStack().clear();

        boolean isNotTransformable = true;
        if (this.paintStateModel.getCurrentNode() != null) {
            if (this.paintStateModel.getCurrentNode().isTransformable()) {
                isNotTransformable = false;
            }
        }

        if (isNotTransformable) {
            switch (currentToolType) {
                case "shape":
                    handleToolShapeOnPress(null, currentTool);
                    break;
                case "brush":
                    handleToolBrushOnPress();
                    break;
                case "general":
                    toolController.handleToolGeneralOnPress(null, currentTool, mouseEvent, drawingPane);
                    break;
                case "selection":
                    selectionHandler.handleSelectionPressed(startX, startY);
                    break;
            }
        }

    }

    private void handleToolShapeOnPress(TransformableNode currentNode, String currentTool) {
        switch (currentTool) {
            case "StLine":
                Line line = new Line(startX, startY, startX + 1, startY + 1);
                currentNode = new TransformableNode(line, this.workspaceHandler);
                break;
            case "Rectangle":
                // x, y, width, height, Paint fill
                Rectangle rectangle = new Rectangle(startX, startY, 1, 2);
                currentNode = new TransformableNode(rectangle, this.workspaceHandler);
                break;
            case "Circle":
                Circle circle = new Circle(startX, startY, 1);
                currentNode = new TransformableNode(circle, this.workspaceHandler);
                break;
            case "Square":
                Rectangle square = new Rectangle(startX, startY, 1, 1);
                currentNode = new TransformableNode(square, this.workspaceHandler);
                break;
            case "Ellipse":
                // Center X Center Y | Radius X Radius Y
                Ellipse ellipse = new Ellipse(startX, startY, 1, 1);
                currentNode = new TransformableNode(ellipse, this.workspaceHandler);
                break;
            case "Triangle":
                Triangle triangle = new Triangle(startX, startX, startX, startY, startY, startY);
                currentNode = new TransformableNode(triangle, this.workspaceHandler);
                break;
            case "RightTriangle":
                RightTriangle rightTriangle = new RightTriangle(startX, startX, startX, startY, startY, startY);
                currentNode = new TransformableNode(rightTriangle, this.workspaceHandler);
                break;
            case "Star":
                Star star = toolController.showStarInputDialog(drawingPane);
                if (star != null) {
                    currentNode = new TransformableNode(star, this.workspaceHandler);
                    currentNode.enableTransformations();
                    this.paintStateModel.setCurrentNode(currentNode);
                    handleToolShapeReleased(currentNode);
                }
                break;
            case "Curve":
                timesAdjusted = 0;
                CubicCurve curve = new CubicCurve(startX, startY, startX, startY, startX + 1, startY + 1, startX + 1, startY + 1);
                currentNode = new TransformableNode(curve, this.workspaceHandler);
                break;
            case "regularPolygon":
                Polygon regularPoly = toolController.showInputDialog(this.drawingPane);
                if (regularPoly != null) {
                    currentNode = new TransformableNode(regularPoly, this.workspaceHandler);
                    currentNode.enableTransformations();
                    this.paintStateModel.setCurrentNode(currentNode);
                    handleToolShapeReleased(currentNode);
                }
                break;
        }

        if (currentNode != null) {
            loadDefaultShapeAttributes(currentNode);
            this.drawingPane.getChildren().add(currentNode);

            // Set current shape in model
            this.paintStateModel.setCurrentNode(currentNode);
        } else {
            // Error for if the currentNode is null (Ideally there should always be a tool selected)
            Alert noToolSelectedAlert = new Alert(Alert.AlertType.ERROR, "NO TOOL SELECTED. Please select a tool in the tool bar above. FROM SHAPE HANDLER");
            noToolSelectedAlert.setTitle("No Tool Selected");
            noToolSelectedAlert.setHeaderText("");
            noToolSelectedAlert.showAndWait();
        }
    }

    private void loadDefaultShapeAttributes(TransformableNode currentNode) {
        Shape shape = (Shape) currentNode.getOriginalNode();

        shape.setStroke(this.paintStateModel.getCurrentPaintColor()); // This controls the outline color
        shape.setStrokeWidth(this.paintStateModel.getCurrentShapeLineStrokeWidth());
        shape.setFill(null); // Set this to null to get 'outline' of shapes
        shape.setMouseTransparent(false);
        shape.setStrokeType(StrokeType.CENTERED);

        if (this.paintStateModel.getDashed()) {
            // Setup dashed lines for shapes
            shape.getStrokeDashArray().addAll(9.5);
        }
        LOGGER.info("Loaded Default Shape Attributes for Node: {}", currentNode);
    }

    private void handleToolBrushOnPress() {
        switch (this.paintStateModel.getCurrentBrush()) {
            case "regular":
                LOGGER.info("Brush Pressed");
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
        // Update mouse POS lbl
        this.infoCanvasModel.setMousePosLbl(mouseEvent);
        TransformableNode currentNode = this.paintStateModel.getCurrentNode();
        String currentToolType = this.paintStateModel.getCurrentToolType();

        // Verify mode
        if (this.paintStateModel.getCurrentNode() != null) {
            if (this.paintStateModel.getCurrentNode().isTransformable()) {
                // If in transform mode, ignore drag events
                return;
            }
        }


        double curX = mouseEvent.getX();
        double curY = mouseEvent.getY();

        switch (currentToolType) {
            case "shape":
                handleToolShapeOnDragged(currentNode, curX, curY);
                break;
            case "brush":
                handleToolBrushOnDragged(curX, curY);
                break;
            case "general":
                handleToolGeneralOnDragged(curX, curY);
                break;
            case "selection":
                selectionHandler.handleSelectionDragged(curX, curY);
                break;
        }

        // Set currentNode in model
        this.paintStateModel.setCurrentNode(currentNode);
    }

    private void handleToolGeneralOnDragged(double curX, double curY) {
        if (paintStateModel.getCurrentTool() == null) {
            // Error for if the currentNode is null (Ideally there should always be a tool selected)
            Alert noToolSelectedAlert = new Alert(Alert.AlertType.ERROR, "NO TOOL SELECTED. Please select a tool in the tool bar above.");
            noToolSelectedAlert.setTitle("No Tool Selected: GENERAL DRAGGED");
            noToolSelectedAlert.setHeaderText("");
            noToolSelectedAlert.showAndWait();
        }

        switch (paintStateModel.getCurrentTool()){
            case "TextTool":
                break;
        }

        double eraserStrokeWidth = this.paintStateModel.getCurrentLineWidth();
        double eraserX = curX - (eraserStrokeWidth / 2);
        double eraserY = curY - (eraserStrokeWidth / 2);

        graphicsContext.clearRect(eraserX, eraserY, eraserStrokeWidth, eraserStrokeWidth);
    }

    private void handleToolShapeOnDragged(TransformableNode currentNode, double curX, double curY) {
        if (this.paintStateModel.getCurrentNode().isTransformable()) {
            return;
        }

        if (currentNode == null) {
            // Error for if the currentNode is null (Ideally there should always be a tool selected)
            Alert noToolSelectedAlert = new Alert(Alert.AlertType.ERROR, "NO TOOL SELECTED. Please select a tool in the tool bar above.");
            noToolSelectedAlert.setTitle("No Tool Selected: DRAGGED");
            noToolSelectedAlert.setHeaderText("");
            noToolSelectedAlert.showAndWait();
            return;
        }

        Shape shape = null;
        if (currentNode.isShape()) {
            shape = (Shape) currentNode.getOriginalNode();
        }


        // Check if shape is off the canvas
        if (curX >= mainCanvas.getWidth() || curY >= mainCanvas.getHeight() || curX < 0 || curY < 0) {
            return;
        }

        if (shape instanceof Line line) {
            line.setEndX(curX);
            line.setEndY(curY);
            return;
        }

        if (shape instanceof CubicCurve curve) {
            // Drag starting line -> wait for user click 1 or 2 times for control location
            curve.setEndX(curX);
            curve.setEndY(curY);
        }

        if (shape instanceof Triangle triangle) {
            // X1 stays same | Y1 changes | X2 changes | Y2 stays same | X3 & Y3 are the cursor
            double topVertex = ((curX - startX) / 2) + startX;
            triangle.setVertices(startX, curY, topVertex, startY, curX, curY);
        }

        if (shape instanceof RightTriangle rightTriangle) {
            rightTriangle.setVertices(startX, curY, curX, startY, curX, curY); // TODO Update ToolMenu UI Icon with right triangle icon
        }

        if (shape instanceof Rectangle rect) {
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

        if (shape instanceof Circle circle) {

            circle.setCenterX(startX);
            circle.setCenterY(startY);
            circle.setRadius(Math.abs(curX - startX));

        }

        if (shape instanceof Ellipse ellipse) {
            ellipse.setCenterX(startX);
            ellipse.setCenterY(startY);
            ellipse.setRadiusX(Math.abs(curX - startX));
            ellipse.setRadiusY(Math.abs(curY - startY));
        }


    }

    private void handleToolBrushOnDragged(double curX, double curY) {
        GraphicsContext gc = mainCanvas.getGraphicsContext2D();
        gc.lineTo(curX+.5, curY+.5); // +.5 to account for antialiasing. Makes line more natural
        gc.stroke();
    }

    @FXML
    private void handleMouseReleased(MouseEvent mouseEvent) {
        // Add previous canvas snapshot to undo stack
        String currentToolType = this.paintStateModel.getCurrentToolType();
        LOGGER.info("Mouse Released at X:{},Y:{}", mouseEvent.getX(), mouseEvent.getY());

        switch (currentToolType) {
            case ("shape"):
                // Disable StackPane Mouse Event Handlers
                setCanvasDrawingStackPaneHandlerState(false);
                handleToolShapeReleased(this.paintStateModel.getCurrentNode());
                break;
            case ("brush"), ("general"):
                this.workspaceHandler.getCurrentWorkspace().getUndoStack().push(getCurrentCanvasSnapshot());
                break;
            case ("selection"):
                // Disable StackPane Mouse Event Handlers
                this.workspaceHandler.getCurrentWorkspace().getUndoStack().push(getCurrentCanvasSnapshot());
                setCanvasDrawingStackPaneHandlerState(false);
                this.selectionHandler.handleSelectionReleased(this.workspaceHandler);
                break;
        }

        this.canvasModel.setChangesMade(true);

        // Canvas has been altered adjust state of canvasModel
        this.canvasModel.setFileBlank(false);
    }

    private int timesAdjusted = 0; // Cubic curve
    public void handleToolShapeReleased(TransformableNode currentNode) {
        System.out.println(currentNode);
        if (currentNode == null) {
            return;
        }

        Shape shape = null;
        if (currentNode.isShape()) {
            shape = (Shape) currentNode.getOriginalNode();
        }

        // Check if currentNode is a curve
        if (shape instanceof CubicCurve curve) {
            // Enable mouse click handler for control XY location
            this.canvasGroup.setOnMouseClicked(event -> {
                if (timesAdjusted >= 2) {
                    curve.setControlX2(event.getX());
                    curve.setControlY2(event.getY());

                    // Enable transformations
                    currentNode.setPickOnBounds(true);
                    this.paintStateModel.getCurrentNode().setTransformable(true);
                    currentNode.enableTransformations();
                    this.paintStateModel.setCurrentNode(currentNode);
                    this.canvasGroup.setOnMouseClicked(null);
                } else {
                    curve.setControlX1(event.getX());
                    curve.setControlY1(event.getY());
                    timesAdjusted++;
                }
            });
        } else {
            // Allow shape to be selected via mouse select
            currentNode.setPickOnBounds(true);
            // Enable transformations
            this.paintStateModel.getCurrentNode().setTransformable(true);
            currentNode.enableTransformations();
            this.paintStateModel.setCurrentNode(currentNode);
        }
    }

    public void applyPaneShapeToCanvas(Shape currentNode) {
        graphicsContext.setStroke(this.paintStateModel.getCurrentPaintColor()); // Responsible for the color of shapes
        TransformableNode transformableNode = this.paintStateModel.getCurrentNode();

	    double minX;
        double minY;
        double maxX;
        double maxY;
        double w = currentNode.getBoundsInParent().getWidth();
        double h = currentNode.getBoundsInParent().getHeight();

        // Check for dashed lines
        if (this.paintStateModel.getDashed()) {
            graphicsContext.setLineDashes(9.5);
        } else {
            graphicsContext.setLineDashes(0);
        }


        graphicsContext.setLineWidth(this.paintStateModel.getCurrentShapeLineStrokeWidth());
        graphicsContext.setFill(null);

        // Translation state
        double xT, yT;

        if (checkForTranslation(transformableNode)) {
            xT = transformableNode.getTranslateX();
            yT = transformableNode.getTranslateY();

            minX = currentNode.getBoundsInParent().getMinX() + xT;
            maxX = currentNode.getBoundsInParent().getMaxX() + xT;

            minY = currentNode.getBoundsInParent().getMinY() + yT;
            maxY = currentNode.getBoundsInParent().getMaxY() + yT;
        } else {
            xT = 0;
            yT = 0;

            minX = currentNode.getBoundsInParent().getMinX();
            maxX = currentNode.getBoundsInParent().getMaxX();

            minY = currentNode.getBoundsInParent().getMinY();
            maxY = currentNode.getBoundsInParent().getMaxY();
        }

        double rotation = transformableNode.getRotate();
        double centerX = (minX + maxX) / 2;
        double centerY = (minY + maxY) / 2;

        graphicsContext.save(); // Save the current state
        graphicsContext.translate(centerX, centerY); // Move to the rotation center
        graphicsContext.rotate(rotation); // Rotate around the center
        graphicsContext.translate(-centerX, -centerY); // Move back to original position

        String currentTool = this.paintStateModel.getCurrentTool();

        switch (currentTool) {
            case "StLine":
                // You don't need to use the bounded XY since that will only indicate the bounding box & translations
                Line line = (Line) currentNode;
                double lX = line.getStartX() + xT;
                double lY = line.getStartY() + yT;
                double eX = line.getEndX() + xT;
                double eY = line.getEndY() + yT;

                graphicsContext.strokeLine(lX, lY, eX, eY);
                break;
            case "Curve":
                // control point XY 2x -> end XY
                CubicCurve curve = (CubicCurve) currentNode;

                double px1 = curve.getControlX1() + xT;
                double py1 = curve.getControlY1() + yT;
                double px2 = curve.getControlX2() + xT;
                double py2 = curve.getControlY2() + yT;
                double endX = curve.getEndX() + xT;
                double endY = curve.getEndY() + yT;

                double startX = curve.getStartX() + xT;
                double startY = curve.getStartY() + yT;

                graphicsContext.beginPath();
                graphicsContext.moveTo(startX, startY);
                graphicsContext.bezierCurveTo(px1, py1, px2, py2, endX, endY);
                graphicsContext.stroke();
                graphicsContext.closePath();
                break;
            case ("Rectangle"), ("Square"):
                graphicsContext.strokeRect(minX, minY,w,h);
                break;
            case "Triangle":
                // X1 stays same | Y1 changes | X2 changes | Y2 stays same | X3 & Y3 are the cursor
                Triangle triangle = (Triangle) currentNode;
                handleTriangles(xT, yT, triangle);
                break;
            case "RightTriangle":
                RightTriangle rightTriangle = (RightTriangle) currentNode;
                handleTriangles(xT, yT, rightTriangle);
                break;
            case "Circle", "Ellipse":
                graphicsContext.strokeOval(minX, minY, w, h);
                break;
            case "Star":
                Star star = (Star) transformableNode.getChildren().get(0);
                handleStar(xT, yT, star);
                break;
            case "regularPolygon":
                Polygon polygon = (Polygon) transformableNode.getChildren().get(0);
                double[] xPoints = new double[polygon.getPoints().size() / 2];
                double[] yPoints = new double[polygon.getPoints().size() / 2];

                for (int i = 0; i < polygon.getPoints().size(); i++) {
                    if (i % 2 == 0) {
                        xPoints[i / 2] = polygon.getPoints().get(i) + xT;
                    } else {
                        yPoints[i / 2] = polygon.getPoints().get(i) + yT;
                    }
                }
                graphicsContext.strokePolygon(xPoints, yPoints, xPoints.length);
                break;
        }

        graphicsContext.restore();

        // Add shape creation to the undo stack on applied 2 canvas
        WritableImage writableImage = new WritableImage((int)(mainCanvas.getWidth()), (int) (mainCanvas.getHeight()));
        this.workspaceHandler.getCurrentWorkspace().getUndoStack().push(mainCanvas.snapshot(null, writableImage));


        // Reinitialize drawingPane to remove shape
        drawingPane.getChildren().clear();

        LOGGER.info("Applied Shape: {} to canvas", currentNode);

    }

    public void applySelectionToCanvas(ImageView selection) {
        Image image = selection.getImage();

        double minX = selection.getBoundsInParent().getMinX();
        double maxX = selection.getBoundsInParent().getMaxX();

        double minY = selection.getBoundsInParent().getMinY();
        double maxY = selection.getBoundsInParent().getMaxY();

        double x = selection.getX() + selection.getTranslateX();
        double y = selection.getY() + selection.getTranslateY();

        double rotation = selection.getParent().getRotate();
        double centerX = (minX + maxX) / 2;
        double centerY = (minY + maxY) / 2;

        graphicsContext.save(); // Save the current state
        graphicsContext.translate(centerX, centerY); // Move to the rotation center
        graphicsContext.rotate(rotation); // Rotate around the center
        graphicsContext.translate(-centerX, -centerY); // Move back to original position

        // Reset GC settings
        this.selectionHandler.removeSelectionRectangle();

        // Set canvas to the image
        graphicsContext.drawImage(image, x, y);

        graphicsContext.restore();

        graphicsContext.setLineDashes(0);
        graphicsContext.setStroke(Color.TRANSPARENT);
        graphicsContext.setFill(Color.TRANSPARENT);

        this.canvasModel.setChangesMade(true);

        LOGGER.info("Applied Selection: {} to canvas", selection);
    }

    private void handleStar(double xT, double yT, Star star) {
        star.applyTranslation(xT, yT);
        graphicsContext.strokePolygon(
                star.getPoints().stream().filter(p -> star.getPoints().indexOf(p) % 2 == 0).mapToDouble(p -> p).toArray(),
                star.getPoints().stream().filter(p -> star.getPoints().indexOf(p) % 2 == 1).mapToDouble(p -> p).toArray(),
                star.getPoints().size() / 2
        );
    }

    private void handleTriangles(double xT, double yT, Triangle triangle) {
        double[] xPoints = new double[3];
        double[] yPoints = new double[3];

        xPoints[0] = triangle.getX1() + xT;
        xPoints[1] = triangle.getX2() + xT;
        xPoints[2] = triangle.getX3() + xT;

        yPoints[0] = triangle.getY1() + yT;
        yPoints[1] = triangle.getY2() + yT;
        yPoints[2] = triangle.getY3() + yT;

        graphicsContext.strokePolygon(xPoints, yPoints, 3);
    }

    private boolean checkForTranslation(Group selectionGroup) {
        // Check translation XY movement
        return selectionGroup.getTranslateX() != 0.0 || selectionGroup.getTranslateY() != 0.0;
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
	    this.canvasModel.setChangesMade(true);
        // Change tabName to append '*' to indicate that changes are not saved
        this.tabModel.appendUnsavedTitle();
    }
    // DRAWING SECTION END

    // TRANSLATION SECTION START
    // TRANSLATION SECTION END

    boolean ctrlPressed = false;

    @FXML
    private void initialize() {
        mainCanvas.setWidth(900);
        mainCanvas.setHeight(600);
        // Initialize canvas sizing
        mainCanvas.widthProperty().addListener((obs, oldVal, newVal) -> updateCanvasSize());
        mainCanvas.heightProperty().addListener((obs, oldVal, newVal) -> updateCanvasSize());

        // Initialize graphics context to enable drawing
        graphicsContext = mainCanvas.getGraphicsContext2D();

        // Initialize tool controller to separate concerns
        toolController = new ToolController();
        toolController.setGraphicsContext(graphicsContext);

        // Bind infobar properties to canvasDrawingStackPane since we remove the event listeners from the mainCanvas
        canvasDrawingStackPane.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                infoCanvasModel.setMousePosLbl(event);
            }
        });

        canvasDrawingStackPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                infoCanvasModel.setMousePosLbl(event);
            }
        });

        // Setup CTRL + Scroll handler
        canvasContainer.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                scaleCanvasOnScroll(event);
            }
        });

        LOGGER.info("Canvas Controller Initialized");
    }

    @FXML
    private void handleResizeCanvasExited(MouseEvent mouseEvent) {
        double canvasWidth = this.mainCanvas.getWidth();
        double canvasHeight = this.mainCanvas.getHeight();
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();

        // Boolean values to determine the cardinal directions for resizing NESW
        boolean mouseN = (mouseY < canvasHeight);
        boolean mouseE = (mouseX > canvasWidth);
        boolean mouseS = (mouseY > canvasHeight);
        boolean mouseW = (mouseX < canvasWidth);

        if (mouseN) {
            if (mouseE) {
                setCanvasResizeHandlers("NE");
                this.canvasContainer.setCursor(Cursor.NE_RESIZE);
            }
            setCanvasResizeHandlers("N");
            this.canvasContainer.setCursor(Cursor.V_RESIZE);
        }


        if (mouseS) {
            setCanvasResizeHandlers("S");
            this.canvasContainer.setCursor(Cursor.V_RESIZE);
        }

        if (mouseE) {
            setCanvasResizeHandlers("E");
            this.canvasContainer.setCursor(Cursor.H_RESIZE);
        }

        if (mouseW) {
            setCanvasResizeHandlers("W");
            this.canvasContainer.setCursor(Cursor.H_RESIZE);
        }

    }

    private void setCanvasResizeHandlers(String direction) {

        this.canvasContainer.setOnMouseDragged(mD -> {

            // Ensure user does not go below 1
            if (mD.getX() < 1 || mD.getY() < 1 || mD.getX() > 10000 || mD.getY() > 10000) {
                return;
            }

            double newWidth = mainCanvas.getWidth();
            double newHeight = mainCanvas.getHeight();

            switch (direction) {
                case "W":
                    newWidth = Math.max(mD.getX(), 1);
                    this.mainCanvas.expandW(mD.getX());
                    break;
                case "E":
                    newWidth = Math.max(mD.getX(), 1);
                    this.mainCanvas.expandE(newWidth);
                    break;
                case "N":
                    newHeight = Math.max(mD.getY(), 1);
                    this.mainCanvas.expandN(newHeight);
                    break;
                case "S":
                    newHeight = Math.max(mD.getY(), 1);
                    this.mainCanvas.expandS(newHeight);
                    break;
            }

            if (newWidth > 1) {
                this.infoCanvasModel.setResolutionLblText(newWidth, newHeight);
            }

            if (newHeight > 1) {
                this.infoCanvasModel.setResolutionLblText(newWidth, newHeight);
            }
        });
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

        this.canvasModel.setChangesMade(true);
    }

    // Takes a snapshot of the canvas & saves it to the designated file
    public void saveImageFromCanvas(File file, String fileExtension) {
        WritableImage writableImage = new WritableImage((int)(mainCanvas.getWidth()), (int) (mainCanvas.getHeight()));
        // Take a snapshot of the current canvas and save it to the writableImage
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

            this.canvasModel.setChangesMade(false);

            // Set tab name
            this.tabModel.setTabName(file.getName());
            this.tabModel.handleFileSavedTitle();

            LOGGER.info("File: {} | Saved from canvas", file);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Unable to save the image at this time. Stack Trace: " + e.getMessage() );
            e.printStackTrace();
            LOGGER.error("UNABLE TO SAVE IMAGE FROM CANVAS: File {}", file);
        }

    }

    public boolean isFileSavedRecently() throws IOException {
        return !this.canvasModel.isChangesMade();
    }


    @FXML
    private void scaleCanvasOnScroll(ScrollEvent scrollEvent) {
        if (!scrollEvent.isControlDown()) {
            // Ensure that CTRL is held down
            return;
        }
        CanvasModel cm = this.workspaceHandler.getCurrentWorkspace().getCanvasModel();

        double zoomFactor = 1.25; // Handles zoom factor
        double scaleFactor = cm.getZoomScale();
        double maxScale = cm.getMaxScale();
        double minScale = cm.getMinScale();

        // Check if user is scrolling in or out
        if (scrollEvent.getDeltaY() > 0) {
            scaleFactor *= zoomFactor;
        } else {
            scaleFactor /= zoomFactor;
        }

        if (scaleFactor > maxScale || scaleFactor < minScale) {
            return;
        }

        cm.setZoomScale(scaleFactor);

        cm.getCanvasGroup().setScaleX(scaleFactor);
        cm.getCanvasGroup().setScaleY(scaleFactor);
    }

    // Mouse POS Handler
    @FXML
    private void onMouseOverCanvas(MouseEvent mouseEvent) {
        // Access the global method used to update Mouse POS
        this.infoCanvasModel.setMousePosLbl(mouseEvent);
    }

    public WritableImage getCurrentCanvasSnapshot() {
        try {
            WritableImage image = new WritableImage((int) mainCanvas.getWidth(), (int) mainCanvas.getHeight());
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            mainCanvas.getGraphicsContext2D().setImageSmoothing(false);
            return mainCanvas.snapshot(snapshotParameters, image);
        } catch (Exception e) {
            LOGGER.error("Unable to snapshot current canvas. Exception: {}", e);
        }

        return null;
    }


    public WorkspaceHandler getCurrentWorkspaceModel() {
        return workspaceHandler;
    }

    public void setCurrentWorkspaceModel(WorkspaceHandler workspaceHandler) {
        this.workspaceHandler = workspaceHandler;
    }

    public SelectionHandler getSelectionHandler() {
        return selectionHandler;
    }

    public void setSelectionHandler(SelectionHandler selectionHandler) {
        this.selectionHandler = selectionHandler;
        this.selectionHandler.setCanvasGroup(canvasGroup);
    }

    public void setTabModel(TabModel tabModel) {
        this.tabModel = tabModel;
    }

    public TabModel getTabModel() {
        return tabModel;
    }

    public void setToolController(ToolController toolController) {
        this.toolController = toolController;
    }

    public ToolController getToolController() {
        return toolController;
    }

    public Pane getDrawingPane() { return this.drawingPane; }

    public void setInfoCanvasModel(InfoCanvasModel infoCanvasModel) {
        this.infoCanvasModel = infoCanvasModel;
        // Initialize resolution label
        this.infoCanvasModel.setResolutionLblText(canvasModel.getCanvasWidth(), canvasModel.getCanvasHeight());
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
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
        // Update ToolController
        this.toolController.setPaintStateModel(paintStateModel);
    }

    
}
