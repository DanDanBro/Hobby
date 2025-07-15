package game.chess.pieces;

import game.util.GamePieceColor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Pawn extends ChessPiece {

    public Pawn(int x, int y, GamePieceColor b) {
        super(x, y, b);
        if (b == GamePieceColor.WHITE) {
            setDirs(new ChessCoordinate[]{new ChessCoordinate(-1, -1), new ChessCoordinate(0, -1),
                    new ChessCoordinate(1, -1)});
        } else {
            setDirs(new ChessCoordinate[]{new ChessCoordinate(-1, 1), new ChessCoordinate(0, 1),
                    new ChessCoordinate(1, 1)});
        }
    }

    @Override
    public Set<ChessCoordinate> checkMoves(HashMap<ChessCoordinate, ChessPiece> board) {
        Set<ChessCoordinate> res = new HashSet<>();
        for (ChessCoordinate i : this.getDirs()) {
            ChessCoordinate check = this.getPosition().copy();
            check.addMove(i);
            if (check.checkValidCoord()) {
                if (Math.abs(i.toIntPos()) == 8 && board.get(check) == null) {
                    res.add(check.copy());
                    boolean startRow = (this.getPosition().getY() == 1) || (this.getPosition().getY() == 6);
                    check.addMove(i);
                    if (startRow && board.get(check) == null) {
                        res.add(check.copy());
                    }
                }
                if (Math.abs(i.getX()) == 1 && board.get(check) != null && board.get(check).getPieceColor() != this.getPieceColor()) {
                    res.add(check.copy());
                }
            }
        }
        setMoves(res);
        return getMoves();
    }
}
