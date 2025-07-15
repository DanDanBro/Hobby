package game.damn;

import java.util.ArrayList;

public class DamnTuple {

    public static int num;
    public static ArrayList<Integer> path;
    public static int type;

    //-----------Black  | White
    //DamnPiece | 00    | 01
    //DamnDamn  | 10    | 11
    public DamnTuple(int num, int type) {
        this.num = num;
        path = new ArrayList<Integer>();
        this.type = type;
    }

    public static int getNum() {
        return num;
    }

    public static ArrayList<Integer> getPath() {
        return path;
    }

    public static int getType() {
        return type;
    }

    public static void setNum(int num) {
        DamnTuple.num = num;
    }

    public static void setPath(ArrayList<Integer> path) {
        DamnTuple.path = path;
    }

    public static void setType(int type) {
        DamnTuple.type = type;
    }

}
