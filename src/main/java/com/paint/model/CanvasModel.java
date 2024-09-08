package com.paint.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Model to hold canvas information
public class CanvasModel {
    private final DoubleProperty canvasWidth = new SimpleDoubleProperty();
    private final DoubleProperty canvasHeight = new SimpleDoubleProperty();
    private Group canvasGroup;
    private String fileOpenMD5;
    private boolean isFileBlank = true; // On open file is blank
    private File currentFile;

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public boolean isFileBlank() {
        return isFileBlank;
    }

    public void setFileBlank(boolean fileBlank) {
        isFileBlank = fileBlank;
    }

    public String getFileOpenMD5() {
        return fileOpenMD5;
    }

    public void setFileOpenMD5(String fileOpenMD5) {
        this.fileOpenMD5 = fileOpenMD5;
    }

    public String getCurrentFileMD5(File file) throws IOException, NoSuchAlgorithmException {
        Path filePath = Path.of(file.getAbsolutePath());

        byte[] data = Files.readAllBytes(Paths.get(filePath.toUri()));
        byte[] hash = MessageDigest.getInstance("MD5").digest(data);
        String checksum = new BigInteger(1, hash).toString(16);
        return checksum;
    }

    public Group getCanvasGroup() {
        return canvasGroup;
    }

    public void setCanvasGroup(Group canvasGroup) {
        this.canvasGroup = canvasGroup;
    }

    public DoubleProperty canvasWidthProperty() {
        return canvasWidth;
    }

    public DoubleProperty canvasHeightProperty() {
        return canvasHeight;
    }

    public double getCanvasWidth() {
        return canvasWidth.get();
    }

    public void setCanvasWidth(double width) {
        canvasWidth.set(width);
    }

    public double getCanvasHeight() {
        return canvasHeight.get();
    }

    public void setCanvasHeight(double height) {
        canvasHeight.set(height);
    }

}
