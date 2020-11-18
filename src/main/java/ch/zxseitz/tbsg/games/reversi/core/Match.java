package ch.zxseitz.tbsg.games.reversi.core;

import ch.zxseitz.tbsg.games.reversi.exceptions.InvalidFieldException;
import ch.zxseitz.tbsg.games.reversi.exceptions.InvalidPlaceException;
import ch.zxseitz.tbsg.games.reversi.exceptions.InvalidPlayerException;
import ch.zxseitz.tbsg.games.reversi.exceptions.ReversiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Match<T, P> {
    public static final int STATE_UNDEFINED = -1;
    public static final int STATE_NEXT_BLACK = 1;
    public static final int STATE_NEXT_WHITE = 2;
    public static final int STATE_TIE = 10;
    public static final int STATE_WON_BLACK = 11;
    public static final int STATE_WON_WHITE = 12;

    private final T id;
    private final P playerBlack;
    private final P playerWhite;
    private final List<Audit<P>> history;
    private final Board board;
    private int state;
    private final Map<Vector2, List<Integer>> actions;

    public Match(T id, P playerBlack, P playerWhite) {
        this.id = id;
        this.playerBlack = playerBlack;
        this.playerWhite = playerWhite;
        this.history = new ArrayList<>(60);
        this.board = new Board();
        this.state = STATE_NEXT_BLACK;
        this.actions = new TreeMap<>();  //todo
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

    public int getColor(P player) {
        return player == playerBlack ? Board.FIELD_BLACK : player == playerWhite
                ? Board.FIELD_WHITE : Board.FIELD_UNDEFINED;
    }

    public int place(P player, Vector2 field) throws ReversiException {
        var color = getColor(player);
        if (color == Board.FIELD_UNDEFINED) {
            throw new InvalidPlayerException(String.format("Player [%s] is not a member of  match [%s]", player, id));
        }
        if (state >= STATE_TIE) {
            throw new InvalidPlaceException(String.format("Match %s is already finished", id));
        }
        if (color == Board.FIELD_BLACK && state == STATE_NEXT_WHITE
                || color == Board.FIELD_WHITE && state == STATE_NEXT_BLACK) {
            throw new InvalidPlaceException(String.format("Not player's [%s] turn in match [%s]", player, id));
        }
        if (!actions.containsKey(field)) {
            throw new InvalidFieldException(String.format("Invalid place action of player [%s] on field [%s]" +
                    "in match [%s]", player, field, id));
        }

        // todo

        return 0;
    }
}
