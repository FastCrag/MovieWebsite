module MovieWebsite {
	requires javafx.controls;
	requires javafx.base;
	requires javafx.graphics;
	requires javafx.media;
	
	opens application to javafx.graphics, javafx.fxml;
}
