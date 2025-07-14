package main.java.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class settingsController extends controller {

    @FXML
    public transient AnchorPane anchorPane;
    @FXML
    public transient Button volumePlus;
    @FXML
    public transient Button volumeMin;
    @FXML
    public transient CheckBox darkmode;
    @FXML
    public transient CheckBox music;
    @FXML
    public transient CheckBox soundEffects;
    @FXML
    public transient Slider volumeSlider;
    @FXML
    public transient Label volumeLevel;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (pref.getBoolean("darkmode", true)) {
            anchorPane.getStylesheets().add(dark);
        }
        else {
            anchorPane.getStylesheets().add(light);
        }
        darkmode.setSelected(pref.getBoolean("darkmode", true));
        music.setSelected(pref.getBoolean("music", true));
        soundEffects.setSelected(pref.getBoolean("soundEffects", true));
        volumeSlider.setValue(pref.getDouble("volume", 100));
        volumeLevel.setText(df.format(volumeSlider.getValue()));

        volumeSlider.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
                pref.putDouble("volume", volumeSlider.getValue());
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                volumeLevel.setText(df.format(volumeSlider.getValue()));
            });
    }

    public void setVolumePlus(ActionEvent actionEvent) {
        volumeSlider.setValue(volumeSlider.getValue() + 1);
        pref.putDouble("volume", volumeSlider.getValue());
        mediaPlayer.setVolume(volumeSlider.getValue() / 100);
        volumeLevel.setText(df.format(volumeSlider.getValue()));
    }

    public void setVolumeMin(ActionEvent actionEvent) {
        volumeSlider.setValue(volumeSlider.getValue() - 1);
        pref.putDouble("volume", volumeSlider.getValue());
        mediaPlayer.setVolume(volumeSlider.getValue() / 100);
        volumeLevel.setText(df.format(volumeSlider.getValue()));
    }

    public void soundSwitch() {
        pref.putBoolean("soundEffects", soundEffects.isSelected());
    }

    public void musicSwitch() {
        if (music.isSelected()) {
            mediaPlayer.play();
        }
        else  {
            mediaPlayer.pause();
        }
        pref.putBoolean("music", music.isSelected());
    }
}
