module com.test {
	requires org.junit.jupiter.api;
    requires org.testfx.junit5;
	requires org.testfx;
	requires com.paint;
	requires javafx.graphics;

	opens com.test to org.junit.platform.commons;
	exports com.test;
}