package sample.controller.chess;

public class Bishop extends chessPiece {

    public Bishop(int pos, boolean white) {
        super(pos, white);
        dirs = new ChessCoordinate[] {new ChessCoordinate(-1, -1), new ChessCoordinate(-1, 1),
            new ChessCoordinate(1, -1), new ChessCoordinate(1, 1)};
    }
}
