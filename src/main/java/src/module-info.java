module Hobby {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    requires java.prefs;

    opens sample;
    opens sample.controller to javafx.fxml;
}