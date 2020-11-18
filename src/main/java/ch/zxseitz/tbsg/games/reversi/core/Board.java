package ch.zxseitz.tbsg.games.reversi.core;


public class Board {
    // field states
    public static final int FIELD_UNDEFINED = -1;
    public static final int FIELD_EMPTY = 0;
    public static final int FIELD_BLACK = 1;
    public static final int FIELD_WHITE = 2;

    private static final int[] initialFields = new int[] {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 1, 2, 0, 0, 0,
            0, 0, 0, 2, 1, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    /**
     * Calculates the field index from a field coordinate
     *
     * @param point - field coordinate
     * @return field index
     */
    public static int getIndex(Vector2 point) {
        return point.y * 8 + point.x;
    }

    private final int[] fields;

    public Board() {
        fields = new int[64];
        reset();
    }

    /**
     * Resets the fields of this board to their initial state
     */
    public void reset() {
        System.arraycopy(initialFields, 0, fields, 0, 64);
    }

    /**
     * Checks if a field coordinate is on this board
     *
     * @param point - field coordinate
     * @return <code>true</code> if the field coordinate is on this board,
     *     or <code>false</code> otherwise
     */
    public boolean covers(Vector2 point) {
        return point.x >= 0 && point.x < 8 && point.y >= 0 && point.y < 8;
    }

    /**
     * Returns the state of a field
     *
     * @param point - field coordinate
     * @return field state
     */
    public int get(Vector2 point) {
        if (covers(point)) {
            return fields[getIndex(point)];
        }
        return FIELD_UNDEFINED;
    }

    /**
     * Sets the state of a field
     * The state value should be validated before this method is called
     *
     * @param point - field coordinate
     * @param value - field state
     * @return <code>true</code> if the state was set successfully,
     *     or <code>false</code> otherwise
     */
    public boolean set(Vector2 point, int value) {
        if (covers(point)) {
            fields[getIndex(point)] = value;
            return true;
        }
        return false;
    }
}
