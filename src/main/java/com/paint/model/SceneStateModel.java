package com.paint.model;

import javafx.scene.Scene;

public class SceneStateModel {
    private Scene currentScene;

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
