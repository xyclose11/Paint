package com.paint.controller;

import com.paint.model.CanvasModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CanvasController {
    @FXML
    Canvas myCanvas;

    private CanvasModel canvasModel;

    public void setCanvasModel(CanvasModel canvasModel) {
        this.canvasModel = canvasModel;
        updateCanvasSize();
    }

    private void updateCanvasSize() {
        if (canvasModel != null) {
            canvasModel.setCanvasWidth(myCanvas.getWidth());
            canvasModel.setCanvasHeight(myCanvas.getHeight());
        }
    }

    @FXML
    private void initialize() {
        myCanvas.widthProperty().addListener((obs, oldVal, newVal) -> updateCanvasSize());
        myCanvas.heightProperty().addListener((obs, oldVal, newVal) -> updateCanvasSize());
    }

    public void setCanvas(Image image) {
        // Set canvas dimensions to match image dimensions
        myCanvas.setWidth(image.getWidth());
        myCanvas.setHeight(image.getHeight());

        // Update

        // Get pixelReader to convert Image to a WritableImage to set the main canvas
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage(pixelReader, (int) (image.getWidth()), (int)(image.getHeight()));

        // Set canvas to the writableImage
        myCanvas.getGraphicsContext2D().drawImage(writableImage, 0, 0);
    }

    // Takes a snapshot of the canvas & saves it to the designated file
    public void saveImageFromCanvas(File file, String fileExtension) {
        System.out.println(fileExtension);
//        if (fileExtension == "bmp" || fileExtension == "dib") {
//            saveBMPFormat(file, fileExtension);
//        }


        WritableImage writableImage = new WritableImage((int)(myCanvas.getWidth()), (int) (myCanvas.getHeight()));
        // Take a snapshot of the current canvas and save it to the writableImage
        this.myCanvas.snapshot(null, writableImage);

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
