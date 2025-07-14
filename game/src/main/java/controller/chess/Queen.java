package controller.chess;

public class Queen extends chessPiece {


    public Queen(int pos, boolean white) {
        super(pos, white);
        dirs = new ChessCoordinate[] {new ChessCoordinate(-1, -1), new ChessCoordinate(-1, 0),
            new ChessCoordinate(-1, 1), new ChessCoordinate(0, -1),
            new ChessCoordinate(0, 1), new ChessCoordinate(1, -1),
            new ChessCoordinate(1, 0), new ChessCoordinate(1, 1)};
    }
}
