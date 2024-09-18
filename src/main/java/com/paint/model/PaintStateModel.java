package com.paint.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

import java.util.Objects;

// Hold info about currently selected brush, image, color, shape, etc. settings.
public class PaintStateModel {
	// Constant default values START
	final String INITIAL_TOOL = "StLine";
	final String INITIAL_TOOLTYPE = "shape";
	final double INITIAL_LINE_WIDTH = 1.0;
	final double INITIAL_SHAPE_LINE_WIDTH = 1.0;
	final StrokeLineCap INITIAL_LINE_CAP = StrokeLineCap.ROUND;
	final Color INITIAL_PAINT_COLOR = Color.BLACK;
	private final BrushObj currentBrush;
	// Constant default values END
	// Converted currentPaintColor to a property for binding purposes
	private final ObjectProperty<Color> currentPaintColor = new SimpleObjectProperty<>(Color.BLACK);
	private String currentTool; // Holds the currentTool that the user has selected.
	private String currentToolType; // Differentiates the differing tool types. i.e. (Select, brush, shape, etc.)
	private Shape currentShape;
	private double currentLineWidth;
	private StrokeLineCap currentStrokeLineCap;
	private double currentShapeLineStrokeWidth;
	private boolean isTransformable;
	private Group shapeTransformationGroup;
	private Rectangle selectionRectangle;
	private boolean isDashed;
	//    private CanvasController canvasController;
	private CurrentWorkspaceModel currentWorkspaceModel;
	private InfoCanvasModel infoCanvasModel;

    private double startX;
    private double startY;

	public PaintStateModel() {
		this.currentBrush = new BrushObj();
		this.currentTool = INITIAL_TOOL;
		this.currentToolType = INITIAL_TOOLTYPE; // Tool Types include: shape, brush, image, selection, & general
		this.currentShape = null;
		this.currentLineWidth = INITIAL_LINE_WIDTH;
		this.currentStrokeLineCap = INITIAL_LINE_CAP; // Default cap for lines
		this.currentShapeLineStrokeWidth = INITIAL_SHAPE_LINE_WIDTH;
		this.isTransformable = false;
		this.shapeTransformationGroup = new Group();
		this.selectionRectangle = null;
		this.currentPaintColor.setValue(INITIAL_PAINT_COLOR); // Default color
	}

	public boolean getDashed() {
		return this.isDashed;
	}

	public void setDashed(boolean b) {
		this.isDashed = b;
	}

	public InfoCanvasModel getInfoCanvasModel() {
		return infoCanvasModel;
	}

	public void setInfoCanvasModel(InfoCanvasModel infoCanvasModel) {
		this.infoCanvasModel = infoCanvasModel;
	}

	public CurrentWorkspaceModel getCurrentWorkspaceModel() {
		return this.currentWorkspaceModel;
	}
    public void setCurrentSelection(ImageView imageView) {
        this.imageView = imageView;
    }

    public CurrentWorkspaceModel getCurrentWorkspaceModel() { return  this.currentWorkspaceModel; }

	public void setCurrentWorkspaceModel(CurrentWorkspaceModel currentWorkspaceModel) {
		this.currentWorkspaceModel = currentWorkspaceModel;
	}

	public Rectangle getSelectionRectangle() {
		return selectionRectangle;
	}

	public void setSelectionRectangle(Rectangle selectionRectangle) {
		this.selectionRectangle = selectionRectangle;
	}

	public Group getShapeTransformationGroup() {
		return shapeTransformationGroup;
	}

	public void setShapeTransformationGroup(Group shapeTransformationGroup) {
		this.shapeTransformationGroup = shapeTransformationGroup;
	}

	public boolean isTransformable() {
		return isTransformable;
	}

	public void setTransformable(boolean transformable, Pane draw) {
		isTransformable = transformable;

		if (transformable && currentShape != null) {
			// Add dashed outline
			createSelectionBox(this.currentShape, draw);

			Parent parent = this.currentShape.getParent();
			if (parent == null) {
				System.out.println("PARENT ERROR");
				return;
			}

			Scene parentScene = parent.getScene();
			// Set keybinding for ESC to exit transform mode
			parentScene.setOnKeyPressed(keyEvent -> {
				// Check key type
				if (keyEvent.getCode() == KeyCode.ESCAPE) {
					exitTransformMode(parent);
				}
			});

			// Translation handler (XY Movement) SECTION START

			parent.getParent().setOnMousePressed(mousePressed -> handleMousePressed(mousePressed, parent));

			this.shapeTransformationGroup.setOnMouseDragged(dragEvent -> {
                // Update shape position
                this.shapeTransformationGroup.setTranslateX( + (dragEvent.getSceneX() - startX));
                this.shapeTransformationGroup.setTranslateY( + (dragEvent.getSceneY() - startY));
			});
            this.shapeTransformationGroup.setOnMouseDragged(dragEvent -> {
                double[] initialValues = (double[]) this.shapeTransformationGroup.getUserData();
                if (initialValues != null) {
                    double startX = initialValues[0];
                    double startY = initialValues[1];

                    // Update shape position
                    this.shapeTransformationGroup.setTranslateX(this.shapeTransformationGroup.getTranslateX() + (dragEvent.getX() - startX));
                    this.shapeTransformationGroup.setTranslateY(this.shapeTransformationGroup.getTranslateY() + (dragEvent.getY() - startY));
                    // Update UserData with the new mouse position
                    this.shapeTransformationGroup.setUserData(new double[]{dragEvent.getX(), dragEvent.getY()});
                }
            });

			// Translation handler (XY Movement) SECTION END

			// Resize handler SECTION START
			// Resize handler SECTION END

		} else {
			// Handle error
			if (this.shapeTransformationGroup == null) {
				return;
			}

			// Remove selectionRectangle
			if (!this.shapeTransformationGroup.getChildren().isEmpty()) {
				this.shapeTransformationGroup.getChildren().get(0).setVisible(false);
			}
		}
	}

	private void handleMousePressed(MouseEvent mouseEvent, Parent parent) {
		if (!this.shapeTransformationGroup.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
			exitTransformMode(parent);
		} else {
            startX = mouseEvent.getSceneX();
		    startY = mouseEvent.getSceneY();
    private void handleMousePressed(MouseEvent mouseEvent, Parent parent) {
        if (!this.shapeTransformationGroup.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
            exitTransformMode(parent);
        }else {
            if (Objects.equals(this.currentToolType, "selection")) {
                this.imageView.translateXProperty().bind(this.shapeTransformationGroup.translateXProperty());
                this.imageView.translateYProperty().bind(this.shapeTransformationGroup.translateYProperty());
            }

            this.shapeTransformationGroup.setUserData(new double[]{mouseEvent.getX(), mouseEvent.getY()});
        }

	}

	public void exitTransformMode(Parent parent) {
		if (parent != null) {
			Scene parentScene = parent.getScene();
			if (parentScene != null) {
				parentScene.setOnKeyPressed(null);
			}

			if (parent.getParent() != null) {
				parent.getParent().setOnMousePressed(null);
			}
		}
		this.setTransformable(false, null);
		this.shapeTransformationGroup.setOnMouseDragged(null);
		this.shapeTransformationGroup.setOnMousePressed(null);
		this.shapeTransformationGroup.setOnMouseEntered(null);

		// Convert shape -> canvas
		this.currentWorkspaceModel.getCurrentWorkspace().getCanvasController().applyPaneShapeToCanvas(this.currentShape);
        // Convert shape -> canvas

        // Check if current object is a shape
        if (Objects.equals(this.currentToolType, "shape")) { // TODO convert this into a switch/case statement
            this.currentWorkspaceModel.getCurrentWorkspace().getCanvasController().applyPaneShapeToCanvas(this.currentShape);
        } else if (Objects.equals(this.currentToolType, "selection")) {
            this.currentWorkspaceModel.getCurrentWorkspace().getCanvasController().applySelectionToCanvas(this.imageView);
        }


		// Enable CanvasController handlers
		this.currentWorkspaceModel.getCurrentWorkspace().getCanvasController().setCanvasDrawingStackPaneHandlerState(true);

		// Reset shapeGroup
		setShapeTransformationGroup(null);


	}

	private void createSelectionBox(Shape currentShape, Pane drawing) {
		// Create a rect that will surround the currentShape
		Rectangle selectionRect = new Rectangle();
		selectionRect.setId("selectionBox");

		// Bind the rectangle's position to the current shape's bounds in parent
		selectionRect.xProperty().bind(
				Bindings.createDoubleBinding(() -> currentShape.getBoundsInParent().getMinX(),
						currentShape.boundsInParentProperty())
		);
		selectionRect.yProperty().bind(
				Bindings.createDoubleBinding(() -> currentShape.getBoundsInParent().getMinY(),
						currentShape.boundsInParentProperty())
		);

		selectionRect.widthProperty().bind(
				Bindings.createDoubleBinding(() -> currentShape.getBoundsInParent().getWidth(),
						currentShape.boundsInParentProperty())
		);
		selectionRect.heightProperty().bind(
				Bindings.createDoubleBinding(() -> currentShape.getBoundsInParent().getHeight(),
						currentShape.boundsInParentProperty())
		);

		selectionRect.getStrokeDashArray().addAll(9.5);
		selectionRect.setFill(Color.TRANSPARENT);
		selectionRect.setStroke(Color.GRAY);
		selectionRect.setStrokeDashOffset(40);
		selectionRect.setStrokeType(StrokeType.OUTSIDE);
		selectionRect.toFront();

		// TEST for the impl of other transformations (scale/resize, rotate, etc) | need to work out the event handlers
//        Rectangle middleBox = new Rectangle();
//        middleBox.xProperty().bind(selectionRect.xProperty().add(selectionRect.widthProperty().subtract(middleBox.widthProperty()).divide(2)));
//        middleBox.yProperty().bind(selectionRect.yProperty());
//        middleBox.setWidth(10);
//        middleBox.setHeight(10);
//
//        middleBox.xProperty().addListener(((observable, oldValue, newValue) -> updateSelection(selectionRect, middleBox)));
//        middleBox.yProperty().addListener(((observable, oldValue, newValue) -> updateSelection(selectionRect, middleBox)));
//        middleBox.heightProperty().addListener(((observable, oldValue, newValue) -> updateSelection(selectionRect, middleBox)));
//
//        middleBox.setFill(Color.ORANGE);
//        middleBox.setStroke(Color.ORANGE);
//
//        middleBox.setPickOnBounds(true);
		// END TEST

		// Setup mouse event handlers
//        selectionRect.setOnMouseEntered(mouseEvent -> {
//            selectionRect.setCursor(Cursor.H_RESIZE);
//        });

		Group group = new Group(selectionRect, currentShape);
		setShapeTransformationGroup(group);

		shapeTransformationGroup.setOnMouseEntered(mouseE -> {
			shapeTransformationGroup.setCursor(Cursor.MOVE);
		});

		// TODO remove selectionbox when saving since it willcapture the selectionbox in the file

		drawing.getChildren().add(shapeTransformationGroup);
	}

	public double getCurrentShapeLineStrokeWidth() {
		return currentShapeLineStrokeWidth;
	}

	public void setCurrentShapeLineStrokeWidth(double currentShapeLineStrokeWidth) {
		// Set infobar lbl
		this.infoCanvasModel.setCurrentLineWidthLbl(currentShapeLineStrokeWidth);
		this.currentShapeLineStrokeWidth = currentShapeLineStrokeWidth;
	}

	public StrokeLineCap getCurrentStrokeLineCap() {
		return currentStrokeLineCap;
	}

	public void setCurrentStrokeLineCap(StrokeLineCap currentStrokeLineCap) {
		this.currentStrokeLineCap = currentStrokeLineCap;
	}

	public double getCurrentLineWidth() {
		return currentLineWidth;
	}

	public void setCurrentLineWidth(double currentLineWidth) {
		this.infoCanvasModel.setCurrentLineWidthLbl(currentLineWidth);
		this.currentLineWidth = currentLineWidth;
	}

	public Shape getCurrentShape() {
		return this.currentShape;
	}

	public void setCurrentShape(Shape currentShape) {
		this.currentShape = currentShape;
	}

	public String getCurrentTool() {
		return this.currentTool;
	}

	public void setCurrentTool(String currentTool) {
		this.currentTool = currentTool;
	}

	public String getCurrentBrush() {
		return currentBrush.getBrushType();
	}

	public void setCurrentBrush(String currentBrush) {
		// TODO add validation for the brush type
		this.currentBrush.setBrushType(currentBrush);
	}

	public ObjectProperty<Color> getCurrentPaintColorProperty() {
		return currentPaintColor;
	}

	public Color getCurrentPaintColor() {
		return this.currentPaintColor.getValue();
	}

	public void setCurrentPaintColor(Color color) {
		this.currentPaintColor.setValue(color);
	}

	public String getCurrentToolType() {
		return currentToolType;
	}

	public void setCurrentToolType(String currentToolType) {
		this.currentToolType = currentToolType;
	}

	// Create a nested class to allow for default values in obj creation
	private static class BrushObj {
		private String brushType;

		public BrushObj() {
			this.brushType = "regular";
		}

		public BrushObj(String brushType) {
			this.brushType = brushType;
		}

		public String getBrushType() {
			return brushType;
		}

		public void setBrushType(String brushType) {
			this.brushType = brushType;
		}

	}

}
