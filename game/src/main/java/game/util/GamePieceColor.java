package game.util;

public enum GamePieceColor {
    BLACK,
    WHITE;

    public GamePieceColor nextColorToMove() {
        return switch (this) {
            case WHITE -> GamePieceColor.BLACK;
            case BLACK -> GamePieceColor.WHITE;
        };
    }
}
