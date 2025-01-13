module com.front.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.front.app to javafx.fxml;
    exports com.front.app;
    exports com.front.app.Controller;
    opens com.front.app.Controller to javafx.fxml;
}