module Fakehalla {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;

    opens Fakehalla to javafx.fxml;
    exports Fakehalla;
}