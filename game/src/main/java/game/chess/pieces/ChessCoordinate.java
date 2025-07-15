package game.chess.pieces;

import game.util.Coordinate;

public class ChessCoordinate extends Coordinate {

    public ChessCoordinate(int x, int y) {
        super(x, y);
        this.maxLength = 8;
        this.maxHeight = 8;
    }

    public ChessCoordinate copy() {
        return new ChessCoordinate(this.getX(), this.getY());
    }

}
