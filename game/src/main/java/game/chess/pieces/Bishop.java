package game.chess.pieces;

public class Bishop extends ChessPiece {

    public Bishop(int pos, boolean white) {
        super(pos, white);
        setDirs(new ChessCoordinate[]{new ChessCoordinate(-1, -1), new ChessCoordinate(-1, 1),
                new ChessCoordinate(1, -1), new ChessCoordinate(1, 1)});
    }
}
