package game.chess.pieces;

public class Queen extends ChessPiece {


    public Queen(int pos, boolean white) {
        super(pos, white);
        setDirs(new ChessCoordinate[]{new ChessCoordinate(-1, -1), new ChessCoordinate(-1, 0),
                new ChessCoordinate(-1, 1), new ChessCoordinate(0, -1),
                new ChessCoordinate(0, 1), new ChessCoordinate(1, -1),
                new ChessCoordinate(1, 0), new ChessCoordinate(1, 1)});
    }
}
