package com.paint.controller;

import com.paint.model.SettingStateModel;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class handles the UI state for the Auto Save Timer
 *
 * @since 1.5
 * */
public class AutoSaveController {

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The Auto save settings apply btn.
	 */
	@FXML
	public ButtonType autoSaveSettingsApplyBtn;

	/**
	 * The Auto save timer visible cb.
	 */
	@FXML
	public CheckBox autoSaveTimerVisibleCB;

	/**
	 * The Auto save enabled cb.
	 */
	@FXML
	public CheckBox autoSaveEnabledCB;

	/**
	 * The Auto save interval slider.
	 */
	@FXML
	public Slider autoSaveIntervalSlider;

	private SettingStateModel settingStateModel;

	/**
	 * Gets setting state model.
	 *
	 * @return the setting state model
	 */
	public SettingStateModel getSettingStateModel() {
		return settingStateModel;
	}

	/**
	 * Sets setting state model.
	 *
	 * @param settingStateModel the setting state model
	 */
	public void setSettingStateModel(SettingStateModel settingStateModel) {
		this.settingStateModel = settingStateModel;
		applyCurrentAutoSaveSettings();
	}

	private void applyCurrentAutoSaveSettings() {
		autoSaveEnabledCB.setSelected(this.settingStateModel.isAutosaveEnabled());
		autoSaveIntervalSlider.setValue(this.settingStateModel.getAutoSaveInterval());
		autoSaveTimerVisibleCB.setSelected(this.settingStateModel.isTimerVisible());
		LOGGER.info("New AutoSave Settings Applied");
		LOGGER.info("Is Auto Save Enabled: {}", this.settingStateModel.isAutosaveEnabled());
		LOGGER.info("Auto Save Interval: {}", this.settingStateModel.getAutoSaveInterval());
		LOGGER.info("Is Auto Save Timer Visible: {}", this.settingStateModel.isTimerVisible());
	}

	/**
	 * Gets auto save interval slider.
	 *
	 * @return the auto save interval slider
	 */
	public Slider getAutoSaveIntervalSlider() {
		return autoSaveIntervalSlider;
	}

	/**
	 * Sets auto save interval slider.
	 *
	 * @param autoSaveIntervalSlider the auto save interval slider
	 */
	public void setAutoSaveIntervalSlider(Slider autoSaveIntervalSlider) {
		this.autoSaveIntervalSlider = autoSaveIntervalSlider;
	}

	/**
	 * Gets auto save timer visible cb.
	 *
	 * @return the auto save timer visible cb
	 */
	public CheckBox getAutoSaveTimerVisibleCB() {
		return autoSaveTimerVisibleCB;
	}

	/**
	 * Sets auto save timer visible cb.
	 *
	 * @param autoSaveTimerVisibleCB the auto save timer visible cb
	 */
	public void setAutoSaveTimerVisibleCB(CheckBox autoSaveTimerVisibleCB) {
		this.autoSaveTimerVisibleCB = autoSaveTimerVisibleCB;
	}

	/**
	 * Gets auto save enabled cb.
	 *
	 * @return the auto save enabled cb
	 */
	public CheckBox getAutoSaveEnabledCB() {
		return autoSaveEnabledCB;
	}

	/**
	 * Sets auto save enabled cb.
	 *
	 * @param autoSaveEnabledCB the auto save enabled cb
	 */
	public void setAutoSaveEnabledCB(CheckBox autoSaveEnabledCB) {
		this.autoSaveEnabledCB = autoSaveEnabledCB;
	}
}
