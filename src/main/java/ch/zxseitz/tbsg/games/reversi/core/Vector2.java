package ch.zxseitz.tbsg.games.reversi.core;


public class Vector2 implements Comparable<Vector2> {
    public final int x;
    public final int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this(v.x, v.y);
    }

    @Override
    public int compareTo(Vector2 o) {
        var c = Integer.compare(y, o.y);
        if (c == 0) {
            c = Integer.compare(x, o.x);
        }
        return c;
    }

    public static Vector2 add(Vector2 a, Vector2 b) {
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    public static Vector2 subtract(Vector2 a, Vector2 b) {
        return new Vector2(a.x - b.x, a.y - b.y);
    }

    public static Vector2 scale(Vector2 a, int s) {
        return new Vector2(a.x * s, a.y * s);
    }
}
