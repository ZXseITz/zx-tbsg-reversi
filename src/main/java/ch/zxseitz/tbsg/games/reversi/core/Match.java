package ch.zxseitz.tbsg.games.reversi.core;

import ch.zxseitz.tbsg.games.reversi.exceptions.InvalidFieldException;
import ch.zxseitz.tbsg.games.reversi.exceptions.InvalidPlaceException;
import ch.zxseitz.tbsg.games.reversi.exceptions.InvalidPlayerException;
import ch.zxseitz.tbsg.games.reversi.exceptions.ReversiException;

import java.util.*;
import java.util.List;

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
    private final Map<Integer, Set<Integer>> actions;

    public Match(T id, P playerBlack, P playerWhite) {
        this.id = id;
        this.playerBlack = playerBlack;
        this.playerWhite = playerWhite;
        this.history = new ArrayList<>(60);
        this.board = new Board();
        this.state = STATE_NEXT_BLACK;
        this.actions = new TreeMap<>();

        // first black actions
        this.actions.put(20, Set.of(28));
        this.actions.put(29, Set.of(28));
        this.actions.put(34, Set.of(35));
        this.actions.put(43, Set.of(35));
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

    /**
     * @param player
     * @return
     */
    public int getColor(P player) {
        return player == playerBlack ? Board.FIELD_BLACK : player == playerWhite
                ? Board.FIELD_WHITE : Board.FIELD_UNDEFINED;
    }

    /**
     * Places a new token of a player on a field.
     *
     * @param player source
     * @param x      x-coordinate
     * @param y      y-coordinate
     * @return new game state
     * @throws ReversiException
     */
    public void place(P player, int x, int y) throws ReversiException {
        var playerColor = getColor(player);
        var opponentColor = 3 - playerColor;

        // check current state
        if (playerColor == Board.FIELD_UNDEFINED) {
            throw new InvalidPlayerException(String.format("Player [%s] is not a member of  match [%s]", player, id));
        }
        if (state >= STATE_TIE) {
            throw new InvalidPlaceException(String.format("Match %s is already finished", id));
        }
        if (playerColor == Board.FIELD_BLACK && state == STATE_NEXT_WHITE
                || playerColor == Board.FIELD_WHITE && state == STATE_NEXT_BLACK) {
            throw new InvalidPlaceException(String.format("Not player's [%s] turn in match [%s]", player, id));
        }
        var index = board.covers(x, y) ? Board.getIndex(x, y) : -1;
        if (!actions.containsKey(index)) {
            throw new InvalidFieldException(String.format("Invalid place action of player [%s] on field (%d, %d)" +
                    "in match [%s]", player, x, y, id));
        }

        // apply new token
        history.add(new Audit<>(player, index));
        board.set(index, playerColor);
        for (var i : actions.get(index)) {
            board.set(i, playerColor);
        }

        // update state and actions
        actions.clear();
        var emptyFields = new TreeSet<Integer>();
        var blackCount = 0;
        var whiteCount = 0;
        for (var i = 0; i < 64; i++) {
            switch (board.get(i)) {
                case Board.FIELD_BLACK:
                    blackCount++;
                    break;
                case Board.FIELD_WHITE:
                    whiteCount++;
                    break;
                default:
                    emptyFields.add(i);
            }
        }

        // check next opponent turn
        addActions(emptyFields, opponentColor);
        if (!actions.isEmpty()) {
            this.state = opponentColor;
            return;
        }

        // check next own turn, if opponent has no legal moves
        addActions(emptyFields, playerColor);
        if (!actions.isEmpty()) {
            this.state = playerColor;
            return;
        }

        // check end state, if no one has legal moves
        if (blackCount > whiteCount) {
            this.state = STATE_WON_BLACK;
        } else if (blackCount < whiteCount) {
            this.state = STATE_WON_WHITE;
        } else {
            this.state = STATE_TIE;
        }
    }

    public int getState() {
        return state;
    }

    public List<Audit<P>> getHistory() {
        return history;
    }

    private void addActions(Set<Integer> emptyFields, int color) {
        var it = new BoardIterator(board);
        for (var ai : emptyFields) {
            var ax = ai % 8;
            var ay = ai / 8;
            var set = new TreeSet<Integer>();
            set.addAll(iterateStraight(it, ax, ay, 1, 0, color));
            set.addAll(iterateStraight(it, ax, ay, 1, 1, color));
            set.addAll(iterateStraight(it, ax, ay, 0, 1, color));
            set.addAll(iterateStraight(it, ax, ay, -1, 1, color));
            set.addAll(iterateStraight(it, ax, ay, -1, 0, color));
            set.addAll(iterateStraight(it, ax, ay, -1, -1, color));
            set.addAll(iterateStraight(it, ax, ay, 0, -1, color));
            set.addAll(iterateStraight(it, ax, ay, 1, -1, color));
            actions.put(ai, set);
        }
    }

    private Set<Integer> iterateStraight(BoardIterator iterator, int x, int y, int dx, int dy, int color) {
        var fields = new TreeSet<Integer>();
        var opponentColor = 3 - color;
        iterator.set(x, y, dx, dy);
        var state = iterator.next();
        while (state.getValue() == opponentColor) {
            fields.add(state.getKey());
            state = iterator.next();
        }
        if (state.getKey() <= 0) {
            fields.clear();
        }
        return fields;
    }
}
