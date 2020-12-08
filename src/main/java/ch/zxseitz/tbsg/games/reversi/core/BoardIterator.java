package ch.zxseitz.tbsg.games.reversi.core;

import java.util.Map;

public class BoardIterator {
    private Board board;
    private int xDirection, yDirection;
    private int x, y;

    public BoardIterator(Board board) {
        this.board = board;
    }

    public void set(int x, int y, int xDirection, int yDirection) {
        this.x = x;
        this.y = y;
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }

    public Map.Entry<Integer, Integer> next() {
        this.x += xDirection;
        this.y += yDirection;
        var index = Board.getIndex(x, y);
        return Map.entry(index, board.covers(x, y) ? board.get(index) : Board.FIELD_UNDEFINED);
    }
}
