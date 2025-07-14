package main.java.javafx.controller.chess;

import main.java.javafx.controller.chessController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class chessPiece {

    boolean white;
    ChessCoordinate pos;
    ChessCoordinate[] dirs;
    Set<ChessCoordinate> moves;


    public chessPiece(int pos, boolean white) {
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

    /*
    Setter for the colour of the piece, should never really be called
     */
    public void setWhite(boolean white) {
        this.white = white;
    }

    public Set<ChessCoordinate> getMoves() {
        return this.moves;
    }

    /*
    Method to set and return all moves a piece can make, this does not check if the move is actually legal in terms of checks
     */
    public Set<ChessCoordinate> checkMoves(HashMap<ChessCoordinate, chessPiece> board) {
        Set<ChessCoordinate> res = new HashSet<>();
        for(ChessCoordinate i : dirs) {
            ChessCoordinate check = this.pos.copy();
            check.addMove(i);
            while(check.checkValidCoord()) {
                if(board.get(check) == null) {
                    res.add(check.copy());
                    check.addMove(i);
                }
                else if (board.get(check).isWhite() ^ this.isWhite()) {
                    res.add(check.copy());
                    break;
                }
                else {
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
    public King getKing(HashMap<ChessCoordinate, chessPiece> board) {
        for (Map.Entry<ChessCoordinate, chessPiece> entry : board.entrySet()) {
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
    public boolean checkIfMoveIntoCheck(ChessCoordinate check, HashMap<ChessCoordinate, chessPiece> board) {
        boolean res = true;
        ChessCoordinate temp = this.pos.copy();
        chessPiece removed = chessController.movePiece(temp, check, board);
        if (chessController.positionInCheck(this.getKing(board), board).isEmpty()) {
            res = false;
        }
        chessController.movePiece(this.pos, temp, board);
        if(removed != null) {
            board.put(check, removed);
            board.get(check).checkMoves(board);
        }
        chessController.updateBoard(check, board);

        return res;
    }
}
