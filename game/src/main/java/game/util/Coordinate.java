package game.util;

import java.util.Objects;

public class Coordinate {
    private int x; //row
    private int y; //column
    protected int maxLength = Integer.MAX_VALUE;
    protected int maxHeight = Integer.MAX_VALUE;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int toIntPos() {
        return this.x + this.y * maxLength;
    }

    public Coordinate fromIntPos(int i) {
        return new Coordinate(i % this.maxLength, i / this.maxLength);
    }

    public Coordinate addMove(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Coordinate addMove(Coordinate add) {
        this.x += add.x;
        this.y += add.y;
        return this;
    }

    public Coordinate removeMove(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Coordinate removeMove(Coordinate remove) {
        this.x -= remove.x;
        this.y -= remove.y;
        return this;
    }

    public boolean checkValidCoord() {
        return this.y >= 0 && this.y < maxLength && this.x >= 0 && this.x < maxHeight;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Coordinate)) {
            return false;
        }

        Coordinate c = (Coordinate) obj;
        return this.x == c.x && this.y == c.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
