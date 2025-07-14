package controller;

import controller.chess.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class chessController extends controller {

    @FXML
    private transient AnchorPane anchorPane;
    @FXML
    private transient GridPane gridPane;

    boolean whiteMove; //True: white to move, False: black to move
    boolean gameRunning; // True: while game is playing
    int inCheck = 0; //Amount of times the current king is in check (should never be more than 2 except if starting position is weird)
    public static HashMap<ChessCoordinate, chessPiece> board; //Board, key is position
    public static Set<ChessCoordinate> highlighted; //Highlighted tiles
    ChessCoordinate selectedPos; //Selected piece coordinate
    King KingW; //Tracking the white king
    King KingB; //Tracking the black king
    Set<chessPiece> Checking; //Set of pieces that have the king in check (should never be able to contain more than 2 pieces in a normal game.
    Set<ChessCoordinate> blockableTiles; //Set of tiles that are selectable for pieces to block a check

    public static ChessCoordinate[] dirs = new ChessCoordinate[]{new ChessCoordinate(-1, -1), new ChessCoordinate(-1, 0),
            new ChessCoordinate(-1, 1), new ChessCoordinate(0, -1),
            new ChessCoordinate(0, 1), new ChessCoordinate(1, -1),
            new ChessCoordinate(1, 0), new ChessCoordinate(1, 1)};
    public static ChessCoordinate[] knightDirs = {new ChessCoordinate(-2, -1), new ChessCoordinate(-2, 1),
            new ChessCoordinate(-1, -2), new ChessCoordinate(-1, 2),
            new ChessCoordinate(1, -2), new ChessCoordinate(1, 2),
            new ChessCoordinate(2, -1), new ChessCoordinate(2, 1)};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Load css onto anchorpane
        if (pref.getBoolean("darkmode", true)) {
            anchorPane.getStylesheets().add(dark);
        } else {
            anchorPane.getStylesheets().add(light);
        }
        board = new HashMap<>();
        highlighted = new HashSet<>();

        //Initialize the board logics
        try {
            Scanner sc = getBeginPositionScanner("src/javafx/controller/chess/positions/normalSPositions");
            fillBeginPositionsWithScanner(sc);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        //Initialize board GUI
        for (int i = 0; i < 8; i++) {
            int k = i * 8;
            for (int j = 0; j < 8; j++) {
                int pos = k + j;
                StackPane spane = (StackPane) gridPane.getChildren().get(pos);
                if (((i % 2) + j) % 2 == 0) {
                    spane.setStyle("-fx-background-color: linear-gradient(#dc9556, #e8b548); -fx-border-color: black;");
                } else {
                    spane.setStyle("-fx-background-color: linear-gradient(#2c2c47, #33334d); -fx-border-color: black;");
                }
                spane.setOnMouseClicked(this::selectTile);
                addHoverListeners(spane);
            }
        }

        //Initializing moveset of all pieces on the board
        for (Map.Entry<ChessCoordinate, chessPiece> p : board.entrySet()) {
            p.getValue().checkMoves(board);
        }

        Checking = new HashSet<>();
        blockableTiles = new HashSet<>();
        whiteMove = true;
        gameRunning = true;
    }

    /*
    Retrieve file containing the start position.
     */
    public Scanner getBeginPositionScanner(String path) throws IOException, URISyntaxException {
        File file = new File(path);
        return new Scanner(file);
    }

    /*
    Put pieces into board from file
     */
    public void fillBeginPositionsWithScanner(Scanner sc) {
        int i = 0;
        while (sc.hasNextInt() && i < 64) {
            int k = sc.nextInt();
            boolean isWhite = true;
            isWhite = k >= 10;
            switch (k % 10) {
                case 1:
                    if (isWhite) {
                        if (KingW == null) {
                            KingW = new King(i, isWhite);
                        } else {
                            System.out.println("Invalid board, multiple Kings detected");
                        }
                        board.put(new ChessCoordinate(i), KingW);
                    } else {
                        if (KingB == null) {
                            KingB = new King(i, isWhite);
                        } else {
                            System.out.println("Invalid board, multiple Kings detected");
                        }
                        board.put(new ChessCoordinate(i), KingB);
                    }
                    break;
                case 2:
                    board.put(new ChessCoordinate(i), new Queen(i, isWhite));
                    break;
                case 3:
                    board.put(new ChessCoordinate(i), new Rook(i, isWhite));
                    break;
                case 4:
                    board.put(new ChessCoordinate(i), new Bishop(i, isWhite));
                    break;
                case 5:
                    board.put(new ChessCoordinate(i), new Knight(i, isWhite));
                    break;
                case 6:
                    board.put(new ChessCoordinate(i), new Pawn(i, isWhite));
                    break;
                default:
                    break;
            }
            updateGuiPiece(i);
            i++;
        }
    }

    /*
    Method calling all other methods to move a piece
    Exclude is passed to exclude that piece update
    @RETURN chessPiece that has been taken if there is one.
     */
    public static chessPiece movePiece(ChessCoordinate from, ChessCoordinate to, HashMap<ChessCoordinate, chessPiece> boardChecked) {
        chessPiece removed = boardChecked.remove(to);
        boardChecked.put(to, boardChecked.get(from));
        boardChecked.remove(from);
        boardChecked.get(to).setPosition(to);
        updateBoard(from, boardChecked);
        updateBoard(to, boardChecked);
        return removed;
    }

    /*
    Method to update movesets of all effected pieces by a change on the base coordinate
     */
    public static void updateBoard(ChessCoordinate base, HashMap<ChessCoordinate, chessPiece> updateBoard) {
        for (ChessCoordinate i : dirs) {
            ChessCoordinate check = base.copy();
            check.addMove(i);
            while (check.checkValidCoord()) {
                if (updateBoard.get(check) != null) {
                    updateBoard.get(check).checkMoves(updateBoard);
                    break;
                } else {
                    check.addMove(i);
                }
            }
        }

        for (ChessCoordinate i : knightDirs) {
            ChessCoordinate check = base.copy();
            check.addMove(i);
            if (check.checkValidCoord()) {
                if (updateBoard.get(check) != null && (updateBoard.get(check) instanceof Knight)) {
                    updateBoard.get(check).checkMoves(updateBoard);
                }
            }
        }
    }

    /*
    Method to check if the King check in the position from boardChecked is in check.
    @Return set of all pieces checking the check King
     */
    public static HashSet<chessPiece> positionInCheck(King check, HashMap<ChessCoordinate, chessPiece> boardChecked) {
        HashSet<chessPiece> pieceChecking = new HashSet<>();
        for (Map.Entry<ChessCoordinate, chessPiece> entry : boardChecked.entrySet()) {
            if (entry.getValue().isWhite() ^ check.isWhite()) {
                if (entry.getValue().getMoves().contains(check.getPosition())) {
                    pieceChecking.add(entry.getValue());
                }
            }
        }
        return pieceChecking;
    }

    /*
    Method called after a move to check the game state in terms of checks and mates.
     */
    public void checkChecks() {
        if (whiteMove) {
            Checking = positionInCheck(KingW, board);
            KingW.checkMoves(board);
            inCheck(KingW);
            isMated(KingW);
        } else {
            Checking = positionInCheck(KingB, board);
            KingB.checkMoves(board);
            inCheck(KingB);
            isMated(KingB);
        }
    }

    /*
    Method to set the blockableTiles set if the king is in check by 1 piece.
     */
    public void inCheck(King king) {
        blockableTiles = new HashSet<>();
        if (Checking.size() == 1) {
            chessPiece c = Checking.iterator().next();
            Knight knight = new Knight(100, true);
            blockableTiles.add(c.getPosition());
            if (!c.getClass().isInstance(knight)) {
                ChessCoordinate movement = (ChessCoordinate) c.getPosition().copy().removeMove(king.getPosition());
                movement.x = (int) ((movement.x >= 0) ? Math.ceil(movement.x / 8.0) : Math.floor(movement.x / 8.0));
                movement.y = (int) ((movement.y >= 0) ? Math.ceil(movement.y / 8.0) : Math.floor(movement.y / 8.0));
                ChessCoordinate check = (ChessCoordinate) king.getPosition().copy().addMove(movement);

                while (!check.equals(c.getPosition())) {
                    blockableTiles.add(check.copy());
                    check.addMove(movement);
                }
            }
        }
    }

    /*
    Method to check if there is a checkmate or stalemate
     */
    public boolean isMated(King king) {
        //Passing new hashmap/copy of board to avoid concurrentmodification exception (deleting entry from hashmap while iterating over it)
        for (Map.Entry<ChessCoordinate, chessPiece> entry : board.entrySet()) {
            if (entry.getValue().isWhite() == king.isWhite()) {
                if (entry.getValue().equals(king)) {
                    HashSet<ChessCoordinate> moves = new HashSet<>(king.getMoves());
                    moves.removeIf((ChessCoordinate cc) -> entry.getValue().checkIfMoveIntoCheck(cc, new HashMap<>(board)));
                    if (!moves.isEmpty()) {
                        return false;
                    }
                } else {
                    HashSet<ChessCoordinate> checks;
                    if (Checking.isEmpty()) {
                        checks = new HashSet<>(entry.getValue().getMoves());
                    } else {
                        checks = new HashSet<>(blockableTiles);
                        checks.retainAll(entry.getValue().getMoves());
                    }
                    checks.removeIf((ChessCoordinate cc) -> entry.getValue().checkIfMoveIntoCheck(cc, new HashMap<>(board)));
                    if (!checks.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        gameRunning = false;
        System.out.println("Game over");
        if (Checking.isEmpty()) {
            System.out.println("Stalemate");
        } else {
            if (whiteMove) {
                System.out.println("White checkmated, Black won");
            } else {
                System.out.println("Black checkmated, White won");
            }
        }
        return true;
    }
    /*
    @TODO pawnpromotion and en passent and castling
    @TODO 3fold repetition and 50 move draw rule, add history of moves
     */

    /*
    -------------------- GUI --------------------
     */

    private final String resources = "/chess";
    //@TODO add into preferences
    public String skinSet = "/chess/classic/";

    /*
    Method handling the selecting of a tile on the board.
     */
    public void selectTile(MouseEvent mouseEvent) {
        if (!gameRunning) {
            return;
        }
        //Getting the stackpane
        ChessCoordinate pos = null;
        if (mouseEvent.getSource() instanceof StackPane) {
            StackPane spane = (StackPane) mouseEvent.getSource();
            pos = new ChessCoordinate(gridPane.getRowIndex(spane), gridPane.getColumnIndex(spane));
        }

        //Check if a piece is being selected
        if (board.get(pos) != null) {

            //Check for opponent colour piece
            if (whiteMove ^ board.get(pos).isWhite()) {
                //Check if valid take
                if (highlighted.contains(pos) && selectedPos != null) {
                    movePiece(selectedPos, pos, board);
                    updateGuiPiece(selectedPos.toIntPos());
                    updateGuiPiece(pos.toIntPos());
                    dehighlight();
                    whiteMove = !whiteMove;
                    checkChecks();
                }
                //Deselecting
                else {
                    selectedPos = null;
                    dehighlight();
                }
            } else {
                //Check if deselecting selected piece
                if (pos.equals(selectedPos)) {
                    selectedPos = null;
                    dehighlight();
                }
                //Selecting a piece and highlighting moves
                else {
                    selectedPos = pos;
                    dehighlight();
                    highlighted = board.get(selectedPos).getMoves();
                    highlighted.removeIf((ChessCoordinate cc) -> board.get(selectedPos).checkIfMoveIntoCheck(cc, board));
                    if (!Checking.isEmpty() && !(board.get(selectedPos) instanceof King)) {
                        highlighted.retainAll(blockableTiles);
                    }
                    highlight();
                }
            }
        } else {
            //Check valid move into empty space
            if (selectedPos != null && highlighted.contains(pos)) {
                movePiece(selectedPos, pos, board);
                updateGuiPiece(selectedPos.toIntPos());
                updateGuiPiece(pos.toIntPos());
                dehighlight();
                whiteMove = !whiteMove;
                checkChecks();
            }
            //Deselecting
            else {
                selectedPos = null;
                dehighlight();
            }
        }
    }

    public void addHoverListeners(StackPane stackPane) {
        stackPane.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        StackPane spane = (StackPane) e.getSource();
                        if (highlighted.contains(new ChessCoordinate(gridPane.getRowIndex(spane), gridPane.getColumnIndex(spane)))) {
                            spane.setStyle("-fx-background-color: pink; -fx-border-color: black;");
                        }
                    }
                });

        stackPane.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        StackPane spane = (StackPane) e.getSource();
                        ChessCoordinate coord = new ChessCoordinate(gridPane.getRowIndex(spane), gridPane.getColumnIndex(spane));
                        if (highlighted.contains(coord)) {
                            if ((coord.getX() + coord.getY()) % 2 == 0) {
                                spane.setStyle("-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 70% , pink, #dc9556, #e8b548, pink);");
                            } else {
                                spane.setStyle("-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 70% , pink,  #2c2c47, #33334d, pink);");
                            }
                        }
                    }
                });
    }

    public void dehighlight() {
        for (ChessCoordinate i : highlighted) {
            StackPane spane = (StackPane) gridPane.getChildren().get(i.toIntPos());

            if ((i.getX() + i.getY()) % 2 == 0) {
                spane.setStyle("-fx-background-color: linear-gradient(#dc9556, #e8b548); -fx-border-color: black;");
            } else {
                spane.setStyle("-fx-background-color: linear-gradient(#2c2c47, #33334d); -fx-border-color: black;");
            }
        }
        highlighted = new HashSet<>();
    }

    public void highlight() {
        for (ChessCoordinate i : highlighted) {
            StackPane spane = (StackPane) gridPane.getChildren().get(i.toIntPos());

            if ((i.getX() + i.getY()) % 2 == 0) {
                spane.setStyle("-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 70% , pink, #dc9556, #e8b548, pink);");
                //spane.setStyle("-fx-background-color: linear-gradient(pink, #dc9556, #e8b548, pink); -fx-border-color: black;");

            } else {
                spane.setStyle("-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 70% , pink,  #2c2c47, #33334d, pink);");
                //spane.setStyle("-fx-background-color: linear-gradient(pink, #2c2c47, #2c2c47, #33334d, #33334d, pink); -fx-border-color: black;");
            }
        }
    }

    //@TODO add skinsets
    public String getSkinSet() {
        return skinSet;
    }

    public void setSkinSet(String skinSet) {
        this.skinSet = skinSet;
    }

    /*
    Method to update the GUI on position of pos
     */
    public void updateGuiPiece(int pos) {
        chessPiece piece = board.get(new ChessCoordinate(pos));
        StackPane spane = (StackPane) gridPane.getChildren().get(pos);

        if (piece == null) {
            try {
                spane.getChildren().remove(0);
            } catch (Exception e) {
                //Nothing on this tile
            }
            return;
        }
        String[] classSplit = piece.getClass().toString().split("\\.");
        String imagePath = resources + skinSet + classSplit[classSplit.length - 1];
        if (piece.isWhite()) {
            imagePath = imagePath + "W.png";
        } else {
            imagePath = imagePath + "B.png";
        }

        Image pieceImage = new Image(getClass().getResource(imagePath).toString());
        ImageView imgView = new ImageView(pieceImage);
        try {
            spane.getChildren().remove(0);
        } catch (Exception e) {
            //No piece was taken
        }
        spane.getChildren().add(imgView);
    }
}