package com.paint.controller;

import com.paint.model.PaintStateModel;
import com.paint.resource.Star;
import com.paint.resource.TransformableNode;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.stage.Modality;

/**
 * This class controls general tools, -> Eraser, Color Grabber, and the Text Tool
 *
 * NOTE: Currently working on integrating more functionality for remaining tools into ToolController
 * @since 1.3
 * */
public class ToolController {
    private PaintStateModel paintStateModel;
    private GraphicsContext graphicsContext;
    private CanvasController canvasController;

    /**
     * Gets canvas controller.
     *
     * @return the canvas controller
     */
    public CanvasController getCanvasController() {
        return canvasController;
    }

    /**
     * Sets canvas controller.
     *
     * @param canvasController the canvas controller
     */
    public void setCanvasController(CanvasController canvasController) {
        this.canvasController = canvasController;
    }

    private double startX = 0;
    private double startY = 0;

    /**
     * Handle tool general on press.
     *
     * @param currentShape the current shape
     * @param currentTool  the current tool
     * @param mouseEvent   the mouse event
     * @param drawingPane  the drawing pane
     */
    public void handleToolGeneralOnPress(TransformableNode currentShape, String currentTool, MouseEvent mouseEvent, Pane drawingPane) {
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
                textArea.setPrefColumnCount(9);
                textArea.setPrefRowCount(2);
                textArea.setWrapText(true);
                textArea.setBackground(null);
                textArea.setLayoutX(mouseEvent.getX()); // Places the text area
                textArea.setLayoutY(mouseEvent.getY() - 15);
                currentShape = new TransformableNode(textArea, this.paintStateModel.getCurrentWorkspaceModel());
                currentShape.setTransformable(true);
                currentShape.enableTransformations();
                this.paintStateModel.setCurrentNode(currentShape);
                textArea.positionCaret(0);



                drawingPane.getChildren().add(currentShape);

                textArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.ESCAPE) {
                            // Apply text to canvas
                            applyTextToCanvas(textArea.getText());

                            // Remove text area from pane
                            drawingPane.getChildren().clear();
                        }
                    }
                });

                break;

        }

        if (currentShape != null) {
//            loadDefaultShapeAttributes((Shape) currentShape.getOriginalNode());
        }
    }

    private void loadDefaultShapeAttributes(Shape currentShape) {
        currentShape.setStroke(this.paintStateModel.getCurrentPaintColor()); // This controls the outline color
        currentShape.setStrokeWidth(this.paintStateModel.getCurrentShapeLineStrokeWidth());
        currentShape.setFill(null); // Set this to null to get 'outline' of shapes
        currentShape.setMouseTransparent(false);
        currentShape.setStrokeType(StrokeType.OUTSIDE);

        // Set current shape in model
    }

    /**
     * Apply text to canvas.
     *
     * @param text the text
     */
    public void applyTextToCanvas(String text) {
        TransformableNode transformableNode = this.paintStateModel.getCurrentNode();
        graphicsContext.strokeText(text, transformableNode.getTranslatedX(), transformableNode.getTranslatedY());
    }

    /**
     * Show star input dialog star.
     *
     * @param drawingPane the drawing pane
     * @return the star
     */
    public Star showStarInputDialog(Pane drawingPane) {
        // Create a dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Star Input");
        dialog.setHeaderText("Enter the Star parameters");

        // Grid pane for the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create input fields
        TextField sidesField = new TextField();
        sidesField.setPromptText("Number of points");
        TextField radiusField = new TextField();
        radiusField.setPromptText("Radius");

        TextField centerXField = new TextField();
        centerXField.setPromptText("Center X");
        TextField centerYField = new TextField();
        centerYField.setPromptText("Center Y");

        grid.add(new Label("Number of points:"), 0, 0);
        grid.add(sidesField, 1, 0);

        grid.add(new Label("Inner Radius:"), 0, 1);
        grid.add(radiusField, 1, 1);


        grid.add(new Label("Center X:"), 0, 3);
        grid.add(centerXField, 1, 3);

        grid.add(new Label("Center Y:"), 0, 4);
        grid.add(centerYField, 1, 4);

        // Add buttons to the dialog
        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(grid);

        // Convert the result to a polygon and draw it
        final Star[] star = {null};
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                try {
                    int numberOfSides = Integer.parseInt(sidesField.getText());
                    double radius = Double.parseDouble(radiusField.getText());
                    double centerX = Double.parseDouble(centerXField.getText());
                    double centerY = Double.parseDouble(centerYField.getText());
                    star[0] = (new Star(numberOfSides, centerX, centerY, radius));
//                    handleStar(star[0]);
                } catch (NumberFormatException e) {
                    showAlert("Invalid input", "Please enter valid numbers.");
                }
            }
            return null;
        });

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
        return star[0];
    }

    /**
     * Show input dialog polygon.
     *
     * @param drawingPane the drawing pane
     * @return the polygon
     */
    public Polygon showInputDialog(Pane drawingPane) {
        // Create a dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Polygon Input");
        dialog.setHeaderText("Enter the polygon parameters");

        // Grid pane for the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create input fields
        TextField sidesField = new TextField();
        sidesField.setPromptText("Number of sides");
        TextField radiusField = new TextField();
        radiusField.setPromptText("Radius");
        TextField centerXField = new TextField();
        centerXField.setPromptText("Center X");
        TextField centerYField = new TextField();
        centerYField.setPromptText("Center Y");

        grid.add(new Label("Number of sides:"), 0, 0);
        grid.add(sidesField, 1, 0);
        grid.add(new Label("Radius:"), 0, 1);
        grid.add(radiusField, 1, 1);
        grid.add(new Label("Center X:"), 0, 2);
        grid.add(centerXField, 1, 2);
        grid.add(new Label("Center Y:"), 0, 3);
        grid.add(centerYField, 1, 3);

        // Add buttons to the dialog
        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(grid);

        // Convert the result to a polygon and draw it
        final Polygon[] regularPolygon = {null};
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                try {
                    int numberOfSides = Integer.parseInt(sidesField.getText());
                    double radius = Double.parseDouble(radiusField.getText());
                    double centerX = Double.parseDouble(centerXField.getText());
                    double centerY = Double.parseDouble(centerYField.getText());
                    regularPolygon[0] = createPolygon(numberOfSides, radius, centerX, centerY, drawingPane);
                } catch (NumberFormatException e) {
                    showAlert("Invalid input", "Please enter valid numbers.");
                }
            }
            return null;
        });

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();

        return regularPolygon[0];
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Create polygon polygon.
     *
     * @param numberOfSides the number of sides
     * @param radius        the radius
     * @param centerX       the center x
     * @param centerY       the center y
     * @param drawingPane   the drawing pane
     * @return the polygon
     */
    public Polygon createPolygon(int numberOfSides, double radius, double centerX, double centerY, Pane drawingPane) {
        Polygon polygon = new Polygon();
        double angleStep = 360.0 / numberOfSides;
        loadDefaultShapeAttributes(polygon);
        drawingPane.getChildren().add(polygon);

        for (int i = 0; i < numberOfSides; i++) {
            double angle = Math.toRadians(i * angleStep - 90); // Start from the top (90 degrees)
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            polygon.getPoints().addAll(x, y);
        }

        return polygon;
    }

    /**
     * Gets graphics context.
     *
     * @return the graphics context
     */
    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    /**
     * Sets graphics context.
     *
     * @param graphicsContext the graphics context
     */
    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    /**
     * Gets paint state model.
     *
     * @return the paint state model
     */
    public PaintStateModel getPaintStateModel() {
        return paintStateModel;
    }

    /**
     * Sets paint state model.
     *
     * @param paintStateModel the paint state model
     */
    public void setPaintStateModel(PaintStateModel paintStateModel) {
        this.paintStateModel = paintStateModel;
    }
}
