package game.chess.pieces;

import game.chess.controller.ChessController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChessPiece {

    private final boolean white;
    private ChessCoordinate pos;
    private ChessCoordinate[] dirs;
    private Set<ChessCoordinate> moves;

    public ChessPiece(int pos, boolean white) {
        this.pos = new ChessCoordinate(pos);
        this.white = white;
    }

    public ChessCoordinate getPosition() {
        return this.pos;
    }

    public void setPosition(ChessCoordinate p) {
        this.pos = p;
    }

    public boolean isWhite() {
        return white;
    }

    public ChessCoordinate[] getDirs() {
        return this.dirs;
    }

    public void setDirs(ChessCoordinate[] dirs) {
        this.dirs = dirs;
    }

    public Set<ChessCoordinate> getMoves() {
        return this.moves;
    }

    public void setMoves(Set<ChessCoordinate> moves) {
        this.moves = moves;
    }

    /*
        Method to set and return all moves a piece can make, this does not check if the move is actually legal in terms of checks
         */
    public Set<ChessCoordinate> checkMoves(HashMap<ChessCoordinate, ChessPiece> board) {
        Set<ChessCoordinate> res = new HashSet<>();
        for (ChessCoordinate i : dirs) {
            ChessCoordinate check = this.pos.copy();
            check.addMove(i);
            while (check.checkValidCoord()) {
                if (board.get(check) == null) {
                    res.add(check.copy());
                    check.addMove(i);
                } else if (board.get(check).isWhite() ^ this.isWhite()) {
                    res.add(check.copy());
                    break;
                } else {
                    break;
                }
            }
        }
        moves = res;
        return moves;
    }

    /*
    Method to find same coloured king on the board, may be moved into the chesscontroller in the future
     */
    public King getKing(HashMap<ChessCoordinate, ChessPiece> board) {
        for (Map.Entry<ChessCoordinate, ChessPiece> entry : board.entrySet()) {
            if (entry.getValue() instanceof King) {
                if (entry.getValue().isWhite() == this.isWhite()) {
                    return (King) entry.getValue();
                }
            }
        }
        return null;
    }

    /*
    Check the legality of a move in regard to putting your own king in check.
     */
    public boolean checkIfMoveIntoCheck(ChessCoordinate check, HashMap<ChessCoordinate, ChessPiece> board) {
        boolean res = true;
        ChessCoordinate temp = this.pos.copy();
        ChessPiece removed = ChessController.movePiece(temp, check, board);
        if (ChessController.positionInCheck(this.getKing(board), board).isEmpty()) {
            res = false;
        }
        ChessController.movePiece(this.pos, temp, board);
        if (removed != null) {
            board.put(check, removed);
            board.get(check).checkMoves(board);
        }
        ChessController.updateBoard(check, board);

        return res;
    }
}
