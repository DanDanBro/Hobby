package main.java.javafx.controller.chess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Knight extends chessPiece {

    public ChessCoordinate[] dirs = {new ChessCoordinate(-2, -1), new ChessCoordinate(-2, 1),
        new ChessCoordinate(-1, -2), new ChessCoordinate(-1, 2),
        new ChessCoordinate(1, -2), new ChessCoordinate(1, 2),
        new ChessCoordinate(2, -1), new ChessCoordinate(2, 1)};

    public Knight(int i, boolean b) {
        super(i, b);
    }

    @Override
    public Set<ChessCoordinate> checkMoves(HashMap<ChessCoordinate, chessPiece> board) {
        Set<ChessCoordinate> res = new HashSet<>();
        for(ChessCoordinate i : dirs) {
            ChessCoordinate check = this.pos.copy();
            check.addMove(i);
            if(!check.checkValidCoord()) {
                continue;
            }
            else {
                if(board.get(check) == null || board.get(check).isWhite() ^ this.isWhite()) {
                    res.add(check.copy());
                }
            }
        }
        moves = res;
        return moves;
    }
}
