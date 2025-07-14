package controller;

//@TODO full restructure to coordinate mapping
public class Coordinate {
    protected int x; //row
    protected int y; //column
    protected int maxLength = 1;
    protected int maxHeight = 1;

    public Coordinate(int pos) {
        this.x = pos / maxLength;
        this.y = pos % maxLength;
    }

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
        return this.x*maxLength + this.y;
    }

    public Coordinate addMove(int move) {
        this.x += (move / maxLength);
        this.y += (move % maxLength);
        return this;
    }

    public Coordinate addMove(Coordinate add) {
        this.x += add.x;
        this.y += add.y;
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
        if(obj == this) {
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
        int result = 17;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        return result;
    }
}
