module Hobby {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    requires java.prefs;

    opens javafx;
    opens javafx.controller to javafx.fxml;
}