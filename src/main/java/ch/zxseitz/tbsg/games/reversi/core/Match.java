package ch.zxseitz.tbsg.games.reversi.core;

import java.util.ArrayList;
import java.util.List;

public class Match<T, P> {
    private T id;
    private P playerBlack;
    private P playerWhite;
    private Board board;
    private Object state;
    private List<Audit<P>> history;

    public Match(T id, P playerBlack, P playerWhite) {
        this.id = id;
        this.playerBlack = playerBlack;
        this.playerWhite = playerWhite;
        this.board = new Board();
        this.history = new ArrayList<>(60);
    }

    public T getId() {
        return id;
    }

    public P getPlayerBlack() {
        return playerBlack;
    }

    public P getPlayerWhite() {
        return playerWhite;
    }


}
