package com.paint.model;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;

public class SceneStateModel {
    private Scene currentScene;

    private HBox canvasView;

    public HBox getCanvasView() {
        return canvasView;
    }

    public void setCanvasView(HBox canvasView) {
        this.canvasView = canvasView;
    }

    public SceneStateModel(Scene primaryScene) {
        this.currentScene = primaryScene;
    }

    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
    }

    public Scene getCurrentScene() {
        return this.currentScene;
    }

}
