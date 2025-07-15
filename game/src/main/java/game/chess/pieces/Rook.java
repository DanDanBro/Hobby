package game.chess.pieces;

import game.util.GamePieceColor;

public class Rook extends ChessPiece {

    private boolean moved;

    public Rook(int x, int y, GamePieceColor white) {
        super(x, y, white);
        setDirs(new ChessCoordinate[]{new ChessCoordinate(-1, 0), new ChessCoordinate(0, -1),
                new ChessCoordinate(0, 1), new ChessCoordinate(1, 0)});
        moved = false;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}
