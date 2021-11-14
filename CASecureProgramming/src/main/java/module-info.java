module com.example.casecureprogramming {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.casecureprogramming to javafx.fxml;
    exports com.example.casecureprogramming;
}