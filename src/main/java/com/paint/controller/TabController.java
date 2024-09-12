package com.paint.controller;

import com.paint.model.CanvasModel;
import com.paint.model.TabModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;

public class TabController {
	private TabModel tabModel;
	private CanvasModel canvasModel;

	@FXML
	public TabPane fileTabPane;

	@FXML
	public Tab addNewFileTab;

	@FXML
	public Tab currentFileTab;

	@FXML
	public Button addNewFileTabBtn;



	// TAB Handler SECTION START
	@FXML
	private void onMouseClickedNewFileTab(Event event) {
		Button button = (Button) event.getSource();

		TabPane tabPane = this.tabModel.getTabPane();
		Tab newTab = new Tab("FILE");

		// START TEMP
		// END TEMP

		HBox t = this.canvasModel.getCanvasView();
		newTab.setContent(t);

		int addNewFileTabIndex= tabPane.getTabs().indexOf(addNewFileTab);

		tabPane.getTabs().remove(addNewFileTabIndex);
		tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
		tabPane.getTabs().add(newTab);
		tabPane.getTabs().add(addNewFileTab);

	}




	// TAB Handler SECTION END

	public void setTabModel(TabModel tabModel) {
		this.tabModel = tabModel;
	}

	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
	}

	public CanvasModel getCanvasModel() {
		return this.canvasModel;
	}

	public TabModel getTabModel() {
		return this.tabModel;
	}



}
