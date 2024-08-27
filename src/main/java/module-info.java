module com.example.paint {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.paint to javafx.fxml;
    exports com.paint;
	exports com.paint.controller;
	opens com.paint.controller to javafx.fxml;
	exports com.paint.model;
	opens com.paint.model to javafx.fxml;
}