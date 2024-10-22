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

	/**
	 * The Initial tool.
	 */
// Constant default values START
	final String INITIAL_TOOL = "StLine";
	/**
	 * The Initial tooltype.
	 */
	final String INITIAL_TOOLTYPE = "shape";
	/**
	 * The Initial line width.
	 */
	final double INITIAL_LINE_WIDTH = 1.0;
	/**
	 * The Initial shape line width.
	 */
	final double INITIAL_SHAPE_LINE_WIDTH = 1.0;
	/**
	 * The Initial line cap.
	 */
	final StrokeLineCap INITIAL_LINE_CAP = StrokeLineCap.ROUND;
	/**
	 * The Initial paint color.
	 */
	final Color INITIAL_PAINT_COLOR = Color.BLACK;
	private final BrushObj currentBrush;
	// Constant default values END
	// Converted currentPaintColor to a property for binding purposes
	private final ObjectProperty<Color> currentPaintColor = new SimpleObjectProperty<>(Color.BLACK);
	private String currentTool; // Holds the currentTool that the user has selected.
	private String currentToolType; // Differentiates the differing tool types. i.e. (Select, brush, shape, etc.)
	private TransformableNode currentNode;
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

	/**
	 * Instantiates a new Paint state model.
	 */
	public PaintStateModel() {
		this.currentBrush = new BrushObj();
		this.currentTool = INITIAL_TOOL;
		this.currentToolType = INITIAL_TOOLTYPE; // Tool Types include: shape, brush, image, selection, & general
		this.currentNode = null;
		this.currentLineWidth = INITIAL_LINE_WIDTH;
		this.currentStrokeLineCap = INITIAL_LINE_CAP; // Default cap for lines
		this.currentShapeLineStrokeWidth = INITIAL_SHAPE_LINE_WIDTH;
		this.isTransformable = false;
		this.shapeTransformationGroup = new Group();
		this.selectionRectangle = null;
		this.currentPaintColor.setValue(INITIAL_PAINT_COLOR); // Default color
	}

	/**
	 * Gets dashed.
	 *
	 * @return the dashed
	 */
	public boolean getDashed() {
		return this.isDashed;
	}

	/**
	 * Sets dashed.
	 *
	 * @param b the b
	 */
	public void setDashed(boolean b) {
		LOGGER.info("Is Dashed: {}", b);
		this.isDashed = b;
	}

	/**
	 * Gets info canvas model.
	 *
	 * @return the info canvas model
	 */
	public InfoCanvasModel getInfoCanvasModel() {
		return infoCanvasModel;
	}

	/**
	 * Sets info canvas model.
	 *
	 * @param infoCanvasModel the info canvas model
	 */
	public void setInfoCanvasModel(InfoCanvasModel infoCanvasModel) {
		this.infoCanvasModel = infoCanvasModel;
	}

	/**
	 * Sets current selection.
	 *
	 * @param imageView the image view
	 */
	public void setCurrentSelection(ImageView imageView) {
        this.imageView = imageView;
		LOGGER.info("New Selection Set");
    }

	/**
	 * Gets image view.
	 *
	 * @return the image view
	 */
	public ImageView getImageView() {
		return imageView;
	}

	/**
	 * Gets current workspace model.
	 *
	 * @return the current workspace model
	 */
	public WorkspaceHandler getCurrentWorkspaceModel() { return  this.workspaceHandler; }

	/**
	 * Sets current workspace model.
	 *
	 * @param workspaceHandler the workspace handler
	 */
	public void setCurrentWorkspaceModel(WorkspaceHandler workspaceHandler) {
		this.workspaceHandler = workspaceHandler;
	}

	/**
	 * Gets current shape line stroke width.
	 *
	 * @return the current shape line stroke width
	 */
	public double getCurrentShapeLineStrokeWidth() {
		return currentShapeLineStrokeWidth;
	}

	/**
	 * Sets current shape line stroke width.
	 *
	 * @param currentShapeLineStrokeWidth the current shape line stroke width
	 */
	public void setCurrentShapeLineStrokeWidth(double currentShapeLineStrokeWidth) {
		// Set infobar lbl
		this.infoCanvasModel.setCurrentLineWidthLbl(currentShapeLineStrokeWidth);
		this.currentShapeLineStrokeWidth = currentShapeLineStrokeWidth;
		LOGGER.info("Line Stroke Width set to: {}px", currentShapeLineStrokeWidth);
	}

	/**
	 * Gets current stroke line cap.
	 *
	 * @return the current stroke line cap
	 */
	public StrokeLineCap getCurrentStrokeLineCap() {
		return currentStrokeLineCap;
	}

	/**
	 * Sets current stroke line cap.
	 *
	 * @param currentStrokeLineCap the current stroke line cap
	 */
	public void setCurrentStrokeLineCap(StrokeLineCap currentStrokeLineCap) {
		this.currentStrokeLineCap = currentStrokeLineCap;
	}

	/**
	 * Gets current line width.
	 *
	 * @return the current line width
	 */
	public double getCurrentLineWidth() {
		return currentLineWidth;
	}

	/**
	 * Sets current line width.
	 *
	 * @param currentLineWidth the current line width
	 */
	public void setCurrentLineWidth(double currentLineWidth) {
		this.infoCanvasModel.setCurrentLineWidthLbl(currentLineWidth);
		this.currentLineWidth = currentLineWidth;
		LOGGER.info("Line Width set to: {}px", currentLineWidth);
	}

	/**
	 * Gets current node.
	 *
	 * @return the current node
	 */
	public TransformableNode getCurrentNode() {
		return this.currentNode;
	}

	/**
	 * Sets current node.
	 *
	 * @param currentNode the current node
	 */
	public void setCurrentNode(TransformableNode currentNode) {
		this.currentNode = currentNode;
	}

	/**
	 * Gets current tool.
	 *
	 * @return the current tool
	 */
	public String getCurrentTool() {
		return this.currentTool;
	}

	/**
	 * Sets current tool.
	 *
	 * @param currentTool the current tool
	 */
	public void setCurrentTool(String currentTool) {
		LOGGER.info("{} Tool Selected", currentTool);
		this.currentTool = currentTool;
	}

	/**
	 * Gets current brush.
	 *
	 * @return the current brush
	 */
	public String getCurrentBrush() {
		return currentBrush.getBrushType();
	}

	/**
	 * Sets current brush.
	 *
	 * @param currentBrush the current brush
	 */
	public void setCurrentBrush(String currentBrush) {
		this.currentBrush.setBrushType(currentBrush);
	}

	/**
	 * Gets current paint color property.
	 *
	 * @return the current paint color property
	 */
	public ObjectProperty<Color> getCurrentPaintColorProperty() {
		return currentPaintColor;
	}

	/**
	 * Gets current paint color.
	 *
	 * @return the current paint color
	 */
	public Color getCurrentPaintColor() {
		return this.currentPaintColor.getValue();
	}

	/**
	 * Sets current paint color.
	 *
	 * @param color the color
	 */
	public void setCurrentPaintColor(Color color) {
		this.currentPaintColor.setValue(color);
	}

	/**
	 * Gets current tool type.
	 *
	 * @return the current tool type
	 */
	public String getCurrentToolType() {
		return currentToolType;
	}

	/**
	 * Sets current tool type.
	 *
	 * @param currentToolType the current tool type
	 */
	public void setCurrentToolType(String currentToolType) {
		LOGGER.info("{} Type Selected", currentToolType);
		this.currentToolType = currentToolType;
	}

	// Create a nested class to allow for default values in obj creation
	private static class BrushObj {
		private String brushType;

		/**
		 * Instantiates a new Brush obj.
		 */
		public BrushObj() {
			this.brushType = "regular";
		}

		/**
		 * Instantiates a new Brush obj.
		 *
		 * @param brushType the brush type
		 */
		public BrushObj(String brushType) {
			this.brushType = brushType;
		}

		/**
		 * Gets brush type.
		 *
		 * @return the brush type
		 */
		public String getBrushType() {
			return brushType;
		}

		/**
		 * Sets brush type.
		 *
		 * @param brushType the brush type
		 */
		public void setBrushType(String brushType) {
			this.brushType = brushType;
		}

	}

}
