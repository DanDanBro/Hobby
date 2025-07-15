package game.chess.pieces;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class King extends ChessPiece {

    Boolean moved;

    ChessCoordinate[] dirs = {new ChessCoordinate(-1, -1), new ChessCoordinate(-1, 0),
            new ChessCoordinate(-1, 1), new ChessCoordinate(0, -1),
            new ChessCoordinate(0, 1), new ChessCoordinate(1, -1),
            new ChessCoordinate(1, 0), new ChessCoordinate(1, 1)};

    public King(int pos, boolean white) {
        super(pos, white);
        moved = false;
    }

    @Override
    public Set<ChessCoordinate> checkMoves(HashMap<ChessCoordinate, ChessPiece> board) {
        Set<ChessCoordinate> res = new HashSet<>();
        for (ChessCoordinate i : dirs) {
            ChessCoordinate check = this.getPosition().copy();
            check.addMove(i);
            if (!check.checkValidCoord()) {
                continue;
            } else {
                if (board.get(check) == null || (board.get(check).isWhite() ^ this.isWhite())) {
                    res.add(check);
                    continue;
                }
            }
        }
        setMoves(res);
        return getMoves();
    }
}
