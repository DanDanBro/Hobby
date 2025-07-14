package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController extends controller {

    @FXML
    private transient AnchorPane anchorPane;
    @FXML
    private transient CheckBox remember;
    @FXML
    private transient CheckBox darkmode;
    @FXML
    private transient TextField usernameField;
    @FXML
    private transient PasswordField passwordField;
    @FXML
    private transient Label warning;

    private int tries;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        tries = 0;
        if (pref.getBoolean("remember", false)) {
            remember.setSelected(true);
            usernameField.setText(pref.get("username", ""));
            passwordField.setText(pref.get("password", ""));
        }
        if (pref.getBoolean("darkmode", true)) {
            anchorPane.getStylesheets().add(dark);
        } else {
            anchorPane.getStylesheets().add(light);
        }
        darkmode.setSelected(pref.getBoolean("darkmode", true));
        startMusic();
    }

    public void startMusic() {
        try {
            File musicFile = new File(getClass().getResource("/music").getPath());
            File[] musicList = musicFile.listFiles();

            assert musicList != null;
            int number = (int) Math.round(Math.random() * (musicList.length - 1));
            String musicPath = "/music/" + musicList[number].getName();
            String musicGet = getClass().getResource(musicPath).toExternalForm();

            Media music = new Media(musicGet);
            mediaPlayer = new MediaPlayer(music);
            if (pref.getBoolean("music", true)) {
                mediaPlayer.play();
            }
            mediaPlayer.setVolume(pref.getDouble("volume", 100) / 100);
            mediaPlayer.setOnEndOfMedia(this::startMusic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logIn(final ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (tries < 3 && username.equals("Dan Dan") && password.equals("SecretSauce")) {
            saveLoginPrefs();
            gotoMenu(actionEvent);
        } else {
            tries++;
            if (tries >= 3) {
                warning.setText("Failed too many times,\nplease restart");
            }
            warning.setVisible(true);
        }
    }

    /**
     * Method to go to the main screen.
     */
    public void saveLoginPrefs() {
        String username = "";
        String password = "";
        if (remember.isSelected()) {
            username = usernameField.getText();
            password = passwordField.getText();
        }
        pref.putBoolean("remember", remember.isSelected());
        pref.put("username", username);
        pref.put("password", password);
        pref.putBoolean("darkmode", darkmode.isSelected());
    }

    public void gotoRegister(ActionEvent actionEvent) {
    }
}
