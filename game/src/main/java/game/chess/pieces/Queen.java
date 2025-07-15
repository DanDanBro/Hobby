package game.chess.pieces;

import game.util.GamePieceColor;

public class Queen extends ChessPiece {


    public Queen(int x, int y, GamePieceColor white) {
        super(x, y, white);
        setDirs(new ChessCoordinate[]{new ChessCoordinate(-1, -1), new ChessCoordinate(-1, 0),
                new ChessCoordinate(-1, 1), new ChessCoordinate(0, -1),
                new ChessCoordinate(0, 1), new ChessCoordinate(1, -1),
                new ChessCoordinate(1, 0), new ChessCoordinate(1, 1)});
    }
}
