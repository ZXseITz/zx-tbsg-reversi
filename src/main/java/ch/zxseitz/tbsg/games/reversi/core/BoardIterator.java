package ch.zxseitz.tbsg.games.reversi.core;

import java.util.Map;

public class BoardIterator {
    private Board board;
    private Vector2 direction;
    private Vector2 field;

    public BoardIterator(Board board, Vector2 direction, Vector2 field) {
        this.board = board;
        this.direction = direction;
        this.field = field;
    }

    public void set(Board board, Vector2 direction, Vector2 field) {
        this.board = board;
        this.direction = direction;
        this.field = field;
    }

    public Map.Entry<Integer, Vector2> next() {
        this.field = Vector2.add(field, direction);
        return Map.entry(board.get(field), field);
    }
}
