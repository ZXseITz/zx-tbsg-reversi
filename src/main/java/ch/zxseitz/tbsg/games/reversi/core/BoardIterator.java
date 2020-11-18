package ch.zxseitz.tbsg.games.reversi.core;

import java.util.Map;

public class BoardIterator {
    private Board board;
    private Vector2 direction;
    private Vector2 point;

    public BoardIterator(Board board, Vector2 direction, Vector2 point) {
        this.board = board;
        this.direction = direction;
        this.point = point;
    }

    public void set(Board board, Vector2 direction, Vector2 point) {
        this.board = board;
        this.direction = direction;
        this.point = point;
    }

    public Map.Entry<Integer, Vector2> next() {
        this.point = Vector2.add(point, direction);
        return Map.entry(board.get(point), point);
    }
}
