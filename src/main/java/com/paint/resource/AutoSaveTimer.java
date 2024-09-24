package com.paint.resource;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public final class AutoSaveTimer extends AutoSave {
	private Timeline timer;
	private int seconds = 0;
	private int prevTimerLen = -1;

	public AutoSaveTimer(Label timerLabel) {
		super();

		seconds = (int) super.getTimerLenInMinutes().toSeconds() * 60;
		timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			seconds--;
			timerLabel.setText("" + seconds);
		}));

		timer.setCycleCount((int) super.getTimerLenInMinutes().toSeconds() * 60); // seconds -> minutes
		timer.play();
	}

	public void timerVisibility(boolean visibility) {
		if (visibility) { // Show timer

		} else { // Hide timer

		}
	}



}
