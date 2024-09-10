module com.paint {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.desktop;
	requires javafx.swing;
	requires org.kordamp.ikonli.core;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.ikonli.fluentui;

	opens com.paint to javafx.fxml;
    exports com.paint;
	exports com.paint.controller;
	exports com.paint.resource;
	opens com.paint.controller to javafx.fxml;
}