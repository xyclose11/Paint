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
import javafx.scene.shape.Line;

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




    double startX = 0;
    double startY = 0;
    private Line line;

    @FXML
    private void handleMousePressed(MouseEvent mouseEvent) {
        line = new Line();
        startX = mouseEvent.getX();
        startY = mouseEvent.getY();
        line.setStartX(startX);
        line.setStartY(startY);

        canvasGroup.getChildren().add(line);
        line.setMouseTransparent(true);

    }


    @FXML
    private void handleMouseDragged(MouseEvent mouseEvent) {
        line.setEndX(mouseEvent.getX());
        line.setEndY(mouseEvent.getY());
    }

    @FXML
    private void handleMouseReleased(MouseEvent mouseEvent) {
        line.setMouseTransparent(false);
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
