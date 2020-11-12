package ch.zxseitz.tbsg.games.reversi.lib;

public class Vector2 {
    public final int x;
    public final int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this(v.x, v.y);
    }
}
