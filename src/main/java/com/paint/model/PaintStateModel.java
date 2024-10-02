package com.paint.model;

import com.paint.handler.WorkspaceHandler;
import com.paint.resource.TransformableNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This Model holds information about the current state of the selected brush, image, color, line-width, shape, {@code transformableNode} etc.
 *
 * The model is then used in situations where the 'state' of the users actions is required to be modified.
 *
 * @since 1.1
 */
public class PaintStateModel {
	private static final Logger LOGGER = LogManager.getLogger();

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
	private TransformableNode currentShape;
	private double currentLineWidth;
	private StrokeLineCap currentStrokeLineCap;
	private double currentShapeLineStrokeWidth;
	private boolean isTransformable;
	private Group shapeTransformationGroup;
	private Rectangle selectionRectangle;
	private boolean isDashed;
	//    private CanvasController canvasController;
	private WorkspaceHandler workspaceHandler;
	private InfoCanvasModel infoCanvasModel;
	private ImageView imageView;

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
		LOGGER.info("Is Dashed: {}", b);
		this.isDashed = b;
	}

	public InfoCanvasModel getInfoCanvasModel() {
		return infoCanvasModel;
	}

	public void setInfoCanvasModel(InfoCanvasModel infoCanvasModel) {
		this.infoCanvasModel = infoCanvasModel;
	}

    public void setCurrentSelection(ImageView imageView) {
        this.imageView = imageView;
		LOGGER.info("New Selection Set");
    }

	public ImageView getImageView() {
		return imageView;
	}

	public WorkspaceHandler getCurrentWorkspaceModel() { return  this.workspaceHandler; }

	public void setCurrentWorkspaceModel(WorkspaceHandler workspaceHandler) {
		this.workspaceHandler = workspaceHandler;
	}

	public double getCurrentShapeLineStrokeWidth() {
		return currentShapeLineStrokeWidth;
	}

	public void setCurrentShapeLineStrokeWidth(double currentShapeLineStrokeWidth) {
		// Set infobar lbl
		this.infoCanvasModel.setCurrentLineWidthLbl(currentShapeLineStrokeWidth);
		this.currentShapeLineStrokeWidth = currentShapeLineStrokeWidth;
		LOGGER.info("Line Stroke Width set to: {}px", currentShapeLineStrokeWidth);
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
		LOGGER.info("Line Width set to: {}px", currentLineWidth);
	}

	public TransformableNode getCurrentShape() {
		return this.currentShape;
	}

	public void setCurrentShape(TransformableNode currentShape) {
		this.currentShape = currentShape;
	}

	public String getCurrentTool() {
		return this.currentTool;
	}

	public void setCurrentTool(String currentTool) {
		LOGGER.info("{} Tool Selected", currentTool);
		this.currentTool = currentTool;
	}

	public String getCurrentBrush() {
		return currentBrush.getBrushType();
	}

	public void setCurrentBrush(String currentBrush) {
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
		LOGGER.info("{} Type Selected", currentToolType);
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
