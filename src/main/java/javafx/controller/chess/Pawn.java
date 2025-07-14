package main.java.javafx.controller.chess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Pawn extends chessPiece {

    public Pawn(int i, boolean b) {
        super(i, b);
        if(b) {
            dirs = new ChessCoordinate[] {new ChessCoordinate(-1, -1), new ChessCoordinate(-1, 0),
                new ChessCoordinate(-1, 1)};
        }
        else{
            dirs = new ChessCoordinate[] {new ChessCoordinate(1, -1), new ChessCoordinate(1, 0),
                new ChessCoordinate(1, 1)};
        }
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
                if(Math.abs(i.toIntPos()) == 8  && board.get(check) == null) {
                    res.add(check.copy());
                    boolean startRow = (this.getPosition().getX() == 1) || (this.getPosition().getX() == 6);
                    check.addMove(i);
                    if (startRow && board.get(check) == null) {
                        res.add(check.copy());
                    }
                }
                if (Math.abs(i.getY()) == 1 && board.get(check) != null && board.get(check).isWhite() ^ this.isWhite()) {
                    res.add(check.copy());
                }
            }
        }
        moves = res;
        return moves;
    }
}
