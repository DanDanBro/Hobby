package game.chess.pieces;

import game.chess.controller.ChessController;
import game.util.GamePieceColor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChessPiece {

    private final GamePieceColor pieceColor;
    private ChessCoordinate pos;
    private ChessCoordinate[] dirs;
    private Set<ChessCoordinate> moves;

    public ChessPiece(int x, int y, GamePieceColor pieceColor) {
        this.pos = new ChessCoordinate(x, y);
        this.pieceColor = pieceColor;
    }

    public ChessCoordinate getPosition() {
        return this.pos;
    }

    public void setPosition(ChessCoordinate p) {
        this.pos = p;
    }

    public GamePieceColor getPieceColor() {
        return pieceColor;
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

    /**
     * Method to set and return all moves a piece can make, this does not check if the move is actually legal in terms of checks
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
                } else if (board.get(check).getPieceColor() != this.getPieceColor()) {
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

    /**
     * Method to find same coloured king on the board, may be moved into the chesscontroller in the future
     */
    public King getKing(HashMap<ChessCoordinate, ChessPiece> board) {
        for (Map.Entry<ChessCoordinate, ChessPiece> entry : board.entrySet()) {
            if (entry.getValue() instanceof King) {
                if (entry.getValue().getPieceColor() == this.getPieceColor()) {
                    return (King) entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Check the legality of a move in regard to putting your own king in check.
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
