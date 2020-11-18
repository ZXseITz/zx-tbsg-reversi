package ch.zxseitz.tbsg.games.reversi.core;

import java.util.List;

public class Match<T, P> {
    private T id;
    private P playerBlack;
    private P playerWhite;
    private Board board;
    private Object state;
    private List<Audit> history;

    public Match(T id, P playerBlack, P playerWhite) {
        this.id = id;
        this.playerBlack = playerBlack;
        this.playerWhite = playerBlack;
    }
}
