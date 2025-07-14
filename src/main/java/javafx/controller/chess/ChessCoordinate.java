package main.java.javafx.controller.chess;

import main.java.javafx.controller.Coordinate;

public class ChessCoordinate extends Coordinate {

    public ChessCoordinate(int pos) {
        super(pos);
        maxLength = 8;
        maxHeight = 8;
        this.x = pos / maxLength;
        this.y = pos % maxLength;
    }

    public ChessCoordinate(int x, int y) {
        super(x, y);
        maxLength = 8;
        maxHeight = 8;
        this.x = x;
        this.y = y;
    }

    public ChessCoordinate copy() {
        return new ChessCoordinate(this.x, this.y);
    }

}
