module com.paint {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.desktop;
	requires javafx.swing;
	requires org.testfx.junit5;
	requires org.testfx;
	requires org.kordamp.ikonli.core;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.ikonli.fluentui;
	requires jdk.httpserver;
    requires org.controlsfx.controls;

    opens com.paint to javafx.fxml;
    exports com.paint;
	exports com.paint.controller;
	exports com.paint.resource;
	exports com.paint.model;
	opens com.paint.controller to javafx.fxml;
	exports com.paint.handler;
	opens com.paint.handler to javafx.fxml;
}