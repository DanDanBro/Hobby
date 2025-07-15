package game.chess.pieces;

import game.util.GamePieceColor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Knight extends ChessPiece {

    public ChessCoordinate[] dirs = {new ChessCoordinate(-2, -1), new ChessCoordinate(-2, 1),
            new ChessCoordinate(-1, -2), new ChessCoordinate(-1, 2),
            new ChessCoordinate(1, -2), new ChessCoordinate(1, 2),
            new ChessCoordinate(2, -1), new ChessCoordinate(2, 1)};

    public Knight(int x, int y, GamePieceColor b) {
        super(x, y, b);
    }

    @Override
    public Set<ChessCoordinate> checkMoves(HashMap<ChessCoordinate, ChessPiece> board) {
        Set<ChessCoordinate> res = new HashSet<>();
        for (ChessCoordinate i : dirs) {
            ChessCoordinate check = this.getPosition().copy();
            check.addMove(i);
            if (check.checkValidCoord()) {
                if (board.get(check) == null || board.get(check).getPieceColor() != this.getPieceColor()) {
                    res.add(check.copy());
                }
            }
        }
        setMoves(res);
        return getMoves();
    }
}
