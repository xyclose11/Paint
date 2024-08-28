module com.paint {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.desktop;
	requires javafx.swing;

	opens com.paint to javafx.fxml;
    exports com.paint;
	exports com.paint.controller;
	opens com.paint.controller to javafx.fxml;
}