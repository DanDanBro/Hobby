package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Controller implements Initializable {

    @FXML
    private transient AnchorPane anchorPane;
    @FXML
    private transient CheckBox darkmode;

    public transient String dark = getClass().getResource("/themes/dark.css").toExternalForm();
    public transient String light = getClass().getResource("/themes/light.css").toExternalForm();

    public transient Preferences pref = new Pref().getPrefs();

    public MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (pref.getBoolean("darkmode", true)) {
            anchorPane.getStylesheets().add(dark);
        } else {
            anchorPane.getStylesheets().add(light);
        }
    }

    /*
    General method to change scenes
     */
    public void changeScene(Parent root1, Controller controller) {
        controller.mediaPlayer = this.mediaPlayer;
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(new Scene(root1));
    }

    /*
    Go to the main mainMenu screen
     */
    public void gotoMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/mainMenu/mainMenu.fxml"));
            Parent root1 = fxmlLoader.load();
            changeScene(root1, fxmlLoader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Go to the settings screen
     */
    public void gotoSettings(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/settings/settings.fxml"));
            Parent root1 = fxmlLoader.load();
            changeScene(root1, fxmlLoader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Go to the game screen
     */
    public void gotoGame(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/game/game.fxml"));
            Parent root1 = fxmlLoader.load();
            changeScene(root1, fxmlLoader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotChess(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/game/chess/chessBoard.fxml"));
            Parent root1 = fxmlLoader.load();
            changeScene(root1, fxmlLoader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotoDamn(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/game/damn/damnBoard.fxml"));
            Parent root1 = fxmlLoader.load();
            changeScene(root1, fxmlLoader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Go to the calculator screen
     */
    public void gotoCalc(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/calculator/calculator.fxml"));
            Parent root1 = fxmlLoader.load();
            changeScene(root1, fxmlLoader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Logout and return to the login screen
     */
    public void logOut(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/login/start.fxml"));
            Parent root1 = fxmlLoader.load();
            mediaPlayer.stop();
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.setScene(new Scene(root1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Checkbox darkmode handler, changes the assigned css2
     */
    public void themeSwitch(MouseEvent mouseEvent) {
        if (darkmode.isSelected()) {
            anchorPane.getStylesheets().remove(light);
            anchorPane.getStylesheets().add(dark);
        } else {
            anchorPane.getStylesheets().remove(dark);
            anchorPane.getStylesheets().add(light);
        }
        pref.putBoolean("darkmode", darkmode.isSelected());
    }
}
