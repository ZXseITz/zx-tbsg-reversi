package ch.zxseitz.tbsg.games.reversi.lib;

public class Field {
    public static int getIndex(Vector2 point) {
        return point.y * 8 + point.x;
    }

    private final int[] data;

    public Field() {
        this.data = new int[64];
    }

    public int get(Vector2 point) {
        return data[getIndex(point)];
    }

    public void set(Vector2 point, int value) {
        data[getIndex(point)] = value;
    }
}
