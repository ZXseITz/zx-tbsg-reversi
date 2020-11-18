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
     * @param field - field coordinate
     * @return <code>true</code> if the field coordinate is on this board,
     *     or <code>false</code> otherwise
     */
    public boolean covers(Vector2 field) {
        return field.x >= 0 && field.x < 8 && field.y >= 0 && field.y < 8;
    }

    /**
     * Returns the state of a field
     *
     * @param field - field coordinate
     * @return field state
     */
    public int get(Vector2 field) {
        if (covers(field)) {
            return fields[getIndex(field)];
        }
        return FIELD_UNDEFINED;
    }

    /**
     * Sets the state of a field
     * The state value should be validated before this method is called
     *
     * @param field - field coordinate
     * @param state - field state
     * @return <code>true</code> if the state was set successfully,
     *     or <code>false</code> otherwise
     */
    public boolean set(Vector2 field, int state) {
        if (covers(field)) {
            fields[getIndex(field)] = state;
            return true;
        }
        return false;
    }
}
