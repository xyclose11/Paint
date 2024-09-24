package com.paint.controller;

import com.paint.model.SettingStateModel;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

public class AutoSaveController {
	@FXML
	public ButtonType autoSaveSettingsApplyBtn;

	@FXML
	public CheckBox autoSaveTimerVisibleCB;

	@FXML
	public CheckBox autoSaveEnabledCB;

	@FXML
	public Slider autoSaveIntervalSlider;

	private SettingStateModel settingStateModel;

	public SettingStateModel getSettingStateModel() {
		return settingStateModel;
	}

	public void setSettingStateModel(SettingStateModel settingStateModel) {
		this.settingStateModel = settingStateModel;
		applyCurrentAutoSaveSettings();
	}

	private void applyCurrentAutoSaveSettings() {
		autoSaveEnabledCB.setSelected(this.settingStateModel.isAutosaveEnabled());
		autoSaveIntervalSlider.setValue(this.settingStateModel.getAutoSaveInterval());
		autoSaveTimerVisibleCB.setSelected(this.settingStateModel.isTimerVisible());
	}

	public Slider getAutoSaveIntervalSlider() {
		return autoSaveIntervalSlider;
	}

	public void setAutoSaveIntervalSlider(Slider autoSaveIntervalSlider) {
		this.autoSaveIntervalSlider = autoSaveIntervalSlider;
	}

	public CheckBox getAutoSaveTimerVisibleCB() {
		return autoSaveTimerVisibleCB;
	}

	public void setAutoSaveTimerVisibleCB(CheckBox autoSaveTimerVisibleCB) {
		this.autoSaveTimerVisibleCB = autoSaveTimerVisibleCB;
	}

	public CheckBox getAutoSaveEnabledCB() {
		return autoSaveEnabledCB;
	}

	public void setAutoSaveEnabledCB(CheckBox autoSaveEnabledCB) {
		this.autoSaveEnabledCB = autoSaveEnabledCB;
	}
}
