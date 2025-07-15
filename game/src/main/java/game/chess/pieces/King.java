package game.chess.pieces;

import game.util.GamePieceColor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class King extends ChessPiece {

    Boolean moved;

    ChessCoordinate[] dirs = {new ChessCoordinate(-1, -1), new ChessCoordinate(-1, 0),
            new ChessCoordinate(-1, 1), new ChessCoordinate(0, -1),
            new ChessCoordinate(0, 1), new ChessCoordinate(1, -1),
            new ChessCoordinate(1, 0), new ChessCoordinate(1, 1)};

    public King(int x, int y, GamePieceColor white) {
        super(x, y, white);
        moved = false;
    }

    @Override
    public Set<ChessCoordinate> checkMoves(HashMap<ChessCoordinate, ChessPiece> board) {
        Set<ChessCoordinate> res = new HashSet<>();
        for (ChessCoordinate i : dirs) {
            ChessCoordinate check = this.getPosition().copy();
            check.addMove(i);
            if (check.checkValidCoord()) {
                if (board.get(check) == null || (board.get(check).getPieceColor() != this.getPieceColor())) {
                    res.add(check);
                }
            }
        }
        setMoves(res);
        return getMoves();
    }
}
