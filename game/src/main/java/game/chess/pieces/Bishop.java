package game.chess.pieces;

import game.util.GamePieceColor;

public class Bishop extends ChessPiece {

    public Bishop(int x, int y, GamePieceColor white) {
        super(x, y, white);
        setDirs(new ChessCoordinate[]{new ChessCoordinate(-1, -1), new ChessCoordinate(-1, 1),
                new ChessCoordinate(1, -1), new ChessCoordinate(1, 1)});
    }
}
