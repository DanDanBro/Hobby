package main.java.javafx.controller.chess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Rook extends chessPiece {

    Boolean moved;

    public Rook(int pos, boolean white) {
        super(pos, white);
        dirs = new ChessCoordinate[] {new ChessCoordinate(-1, 0), new ChessCoordinate(0, -1),
            new ChessCoordinate(0, 1), new ChessCoordinate(1, 0)};
        moved = false;
    }
}
