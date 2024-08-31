package com.paint.controller;

import com.paint.model.PaintStateModel;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ToolMenuController {
    private PaintStateModel paintStateModel;

    // TESTING
    @FXML
    private Label ShapeSectionId;


    public void setPaintStateModel(PaintStateModel paintStateModel) {
        this.paintStateModel = paintStateModel;
    }

    @FXML
    public void updatePaintShapeState(MouseEvent mouseEvent) {
        String sourceId = ((Control)mouseEvent.getSource()).getId();

        if (sourceId == null) {
            // Impl error handling here
        }

        // Update paintStateModel
        this.paintStateModel.setCurrentBrush(sourceId);


        // TESTING
        ShapeSectionId.setText(this.paintStateModel.getCurrentBrush());
    }
}
