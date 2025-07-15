package game.damn;


import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DamnController extends Controller {

    @FXML
    private transient AnchorPane anchorPane;
    @FXML
    private transient GridPane gridPane;
    @FXML
    private transient ListView listView;


    ImageView selectedDamn;
    int selectedPos;
    //Images used to add gui elements.
    Image BlackPiece;
    Image WhitePiece;

    boolean whiteMove; //True: white to move, False: black to move
    public static HashMap<Integer, DamnTuple> board; //Board, key is position
    public static HashMap<Integer, DamnTuple> taken; //Memory of last move, key is position
    public static HashMap<Integer, ArrayList<ArrayList<Integer>>> highlighted; //Highlighted tiles, key is position, Arraylist is possible paths

    int lastFrom; //Memory of last move, from position
    int lastTo; //Memory of last move, to position

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (pref.getBoolean("darkmode", true)) {
            anchorPane.getStylesheets().add(dark);
        } else {
            anchorPane.getStylesheets().add(light);
        }
        board = new HashMap<>();
        taken = new HashMap<>();
        highlighted = new HashMap<>();
        BlackPiece = new Image(getClass().getResource("/game/damn/DamnPieceB.png").toString());
        WhitePiece = new Image(getClass().getResource("/game/damn/DamnPieceW.png").toString());

        //Adding in the GUI elements
        for (int i = 0; i < 10; i++) {
            int k = i * 10;
            for (int j = 0; j < 10; j++) {
                StackPane spane = (StackPane) gridPane.getChildren().get(k + j);
                if (((i % 2) + j) % 2 == 0) {
                    spane.setStyle("-fx-background-color: linear-gradient(#dc9556, #e8b548); -fx-border-color: black;");
                } else {
                    spane.setStyle("-fx-background-color: linear-gradient(#2c2c47, #33334d); -fx-border-color: black;");
                    if (i < 4) {
                        ImageView imgView = new ImageView(BlackPiece);
                        imgView.setOnMouseClicked(this::selectDamn);
                        spane.getChildren().add(imgView);
                        board.put(k + j, new DamnTuple(k + j, 0));
                    }
                    if (i >= 4 && i <= 5) {
                        board.put(k + j, null);
                    }
                    if (i > 5) {
                        ImageView imgView = new ImageView(WhitePiece);
                        imgView.setOnMouseClicked(this::selectDamn);
                        spane.getChildren().add(imgView);
                        board.put(k + j, new DamnTuple(k + j, 1));
                    }
                }
            }
        }
        whiteMove = true;
    }

    //Not properly called by checkDamn
    public void checkPath(int pos, ArrayList<Integer> path) {
        if (path.size() < board.get(pos).getPath().size()) {
            return;
        }

        ArrayList<Integer> checkNE = checkDamn(pos, -9, (ArrayList<Integer>) path.clone());
        ArrayList<Integer> checkSE = checkDamn(pos, 11, (ArrayList<Integer>) path.clone());
        ArrayList<Integer> checkSW = checkDamn(pos, 9, (ArrayList<Integer>) path.clone());
        ArrayList<Integer> checkNW = checkDamn(pos, -11, (ArrayList<Integer>) path.clone());

        if (path.size() != 0) {
            if (checkNE.size() + checkNW.size() + checkSE.size() + checkSW.size() - 4 * path.size() == 0) {
                ArrayList<ArrayList<Integer>> pathList = highlighted.get(pos);
                if (pathList != null) {
                    if (path.size() == pathList.get(0).size()) {
                        pathList.add(path);
                    }
                    if (path.size() > pathList.get(0).size()) {
                        pathList.clear();
                        pathList.add(path);
                    }
                } else {
                    ArrayList<ArrayList<Integer>> posPath = new ArrayList<>();
                    posPath.add(path);
                    highlighted.put(pos, posPath);
                }
            }
        }
        return;
    }

    //Not correctly highlighting taken spots and jump spots
    public ArrayList<Integer> checkDamn(int pos, int dir, ArrayList<Integer> path) {
        int check = pos + dir;
        if (!board.keySet().contains(check)) {
            return path;
        } else {
            DamnTuple checkTuple = board.get(check);
            if (checkTuple == null && path.size() == 0) {
                highlighted.put(check, new ArrayList<>());
                return path;
            }
            if (checkTuple == null || (checkTuple.getType() + board.get(selectedPos).getType()) % 2 == 0) {
                return path;
            } else {
                check = check + dir;
                if (!board.keySet().contains(check)) {
                    return path;
                }
                checkTuple = board.get(check);
                if (checkTuple == null) {
                    path.add(check);
                    checkPath(check, path);
                } else {
                    return path;
                }
            }

        }
        return path;
    }

    /*
    Event handler of clicking a damn.
     */
    public void selectDamn(MouseEvent mouseEvent) {
        ImageView damn = (ImageView) mouseEvent.getSource();
        clearOptions();

        if (damn.equals(selectedDamn)) {
            selectedDamn = null;
            return;
        }

        selectedDamn = (ImageView) mouseEvent.getSource();
        StackPane spane = (StackPane) selectedDamn.getParent();
        selectedPos = gridPane.getColumnIndex(spane) + gridPane.getRowIndex(spane) * 10;
        ArrayList<Integer> path = new ArrayList<>();
        checkPath(selectedPos, path);
        highlightOptions();
    }

    //add functionality for chosing path
    /*
    Event handler for clicking a highlighted tile to move damn
     */
    public void selectMove(MouseEvent mouseEvent) {
        StackPane spane = (StackPane) mouseEvent.getSource();
        int pos = gridPane.getColumnIndex(spane) + gridPane.getRowIndex(spane) * 10;
        ArrayList<ArrayList<Integer>> paths = highlighted.get(pos);
        lastTo = pos;
        if (paths == null || paths.size() == 0) {
            moveDamn(null);
        } else if (paths.size() == 1) {
            moveDamn(paths.get(0));
        } else {
            //listView.getItems().add()
        }
    }

    public void selectPath(MouseEvent mouseEvent) {

    }

    /*
    Method to move the damn to new place, remove the damns in it's path.
    Also saves the positions from where damn moved, moved to, removed damns.
     */
    public void moveDamn(ArrayList<Integer> path) {
        if (path != null) {
            for (int i : path) {
                taken.put(i, board.get(i));
                board.remove(i);
            }
        }
        lastFrom = selectedPos;
        board.put(lastTo, board.remove(selectedPos));
        board.put(selectedPos, null);
        StackPane spane = (StackPane) gridPane.getChildren().get(lastFrom);
        StackPane spaneTarget = (StackPane) gridPane.getChildren().get(lastTo);
        System.out.println(lastFrom + " removed");
        ImageView img = (ImageView) spane.getChildren().remove(0);
        spaneTarget.getChildren().add(img);
        clearOptions();
        selectedDamn = null;
    }

    /*
    Method for highlighting the tiles which are eligible to move to.
     */
    public void highlightOptions() {
        for (int i : highlighted.keySet()) {
            StackPane spane = (StackPane) gridPane.getChildren().get(i);
            spane.setStyle("-fx-background-color: radial-gradient(radius 50%, royalblue, dodgerblue); -fx-border-color: black;");
            spane.addEventHandler(MouseEvent.MOUSE_CLICKED, this::selectMove);
        }
    }

    //Remove eventhandler doesn't work properly
    /*
    Method to turn all highlighted tiles to normal tiles.
     */
    public void clearOptions() {
        for (int i : highlighted.keySet()) {
            StackPane spane = (StackPane) gridPane.getChildren().get(i);
            spane.setStyle("-fx-background-color: linear-gradient(#2c2c47, #33334d); -fx-border-color: black;");

            spane.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::selectMove);
        }
        highlighted.clear();
    }
}
