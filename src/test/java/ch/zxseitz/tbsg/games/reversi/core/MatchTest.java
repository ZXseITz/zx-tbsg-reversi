package ch.zxseitz.tbsg.games.reversi.core;

import ch.zxseitz.tbsg.games.reversi.exceptions.InvalidFieldException;
import ch.zxseitz.tbsg.games.reversi.exceptions.InvalidPlaceException;
import ch.zxseitz.tbsg.games.reversi.exceptions.InvalidPlayerException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Any;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;

import java.util.Map;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Match.class, Board.class, BoardIterator.class, ActionCollection.class})
public class MatchTest {
    private final Board board;
    private final BoardIterator iterator;
    private final ActionCollection actionCollection;

    public MatchTest() {
        board = mock(Board.class);
        iterator = mock(BoardIterator.class);
        actionCollection = mock(ActionCollection.class);
    }

    @Before
    public void setUp() {
        reset(board, iterator, actionCollection);

        try {
            PowerMockito.whenNew(BoardIterator.class)
                    .withArguments(board).thenReturn(iterator);
            PowerMockito.whenNew(ActionCollection.class)
                    .withNoArguments().thenReturn(actionCollection);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testInit() {
        var match = new Match<>(0, "black", "white", board);
        match.init();

        verify(board, times(1)).set(27, Board.FIELD_WHITE);
        verify(board, times(1)).set(28, Board.FIELD_BLACK);
        verify(board, times(1)).set(35, Board.FIELD_BLACK);
        verify(board, times(1)).set(36, Board.FIELD_WHITE);

        verify(actionCollection, times(1)).add(19, 27);
        verify(actionCollection, times(1)).add(26, 27);
        verify(actionCollection, times(1)).add(37, 36);
        verify(actionCollection, times(1)).add(44, 36);

        Assert.assertEquals(Match.STATE_NEXT_BLACK, match.getState());
    }

    @Test
    public void testGetColorWhite() {
        var match = new Match<>(0, "black", "white", board);
        Assert.assertEquals(1, match.getColor("black"));
        Assert.assertEquals(2, match.getColor("white"));
        Assert.assertEquals(-1, match.getColor("something"));
    }

    @Test
    public void testPlaceInvalidPlayer() {
        try {
            var match = new Match<>(0, "black", "white", board);
            match.place("someone", 2, 3);
            Assert.fail();
        } catch (InvalidPlayerException ipe) {
            Assert.assertEquals("Player [someone] is not a member of  match [0]", ipe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPlaceFinishedGameTie() {
        try {
            var match = new Match<>(0, "black", "white", board);
            match.state = Match.STATE_TIE;
            match.place("black", 2, 3);
            Assert.fail();
        } catch (InvalidPlaceException ipe) {
            Assert.assertEquals("Match [0] is already finished", ipe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPlaceFinishedGameBlackWon() {
        try {
            var match = new Match<>(0, "black", "white", board);
            match.state = Match.STATE_WON_BLACK;
            match.place("black", 2, 3);
            Assert.fail();
        } catch (InvalidPlaceException ipe) {
            Assert.assertEquals("Match [0] is already finished", ipe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPlaceFinishedGameWhiteWon() {
        try {
            var match = new Match<>(0, "black", "white", board);
            match.state = Match.STATE_WON_WHITE;
            match.place("black", 2, 3);
            Assert.fail();
        } catch (InvalidPlaceException ipe) {
            Assert.assertEquals("Match [0] is already finished", ipe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPlaceOpponentsTurn() {
        try {
            var match = new Match<>(0, "black", "white", board);
            match.state = Match.STATE_NEXT_BLACK;
            match.place("white", 2, 3);
            Assert.fail();
        } catch (InvalidPlaceException ipe) {
            Assert.assertEquals("Not player's [white] turn in match [0]", ipe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPlaceInvalidField() {
        doReturn(false).when(board).covers(-1, -1);

        try {
            var match = new Match<>(0, "black", "white", board);
            match.state = Match.STATE_NEXT_BLACK;
            match.place("black", -1, -1);
            Assert.fail();
        } catch (InvalidFieldException ife) {
            Assert.assertEquals("Invalid place action of player [black] on field (-1, -1)" +
            "in match [0]", ife.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPlaceNextBlack() {
        doReturn(true).when(actionCollection).containsIndex(26);
        doReturn(true).when(board).covers(2, 3);
        doAnswer((Answer<Void>) invocation -> {
            board.set(35, 1);
            board.set(44, 1);
            return null;
        }).when(actionCollection).foreach(eq(26), any(Consumer.class));

        var field = new int[] {
                0, 0, 2, 2, 2, 2, 0, 0,
                0, 0, 2, 2, 2, 1, 0, 0,
                1, 0, 1, 1, 2, 2, 2, 0,
                1, 2, 2, 1, 2, 1, 2, 2,
                1, 1, 1, 1, 1, 1, 2, 0,
                1, 2, 1, 1, 2, 2, 0, 0,
                0, 2, 1, 1, 1, 0, 0, 0,
                0, 0, 0, 1, 1, 1, 1, 0
        };
        for (var i = 0; i < field.length; i++) {
            doReturn(field[i]).when(board).get(i);
        }

        doReturn(Map.entry(-1, -1)).when(iterator).next();
        doReturn(true).when(actionCollection).anyIndices();

        try {
            var match = new Match<>(0, "black", "white", board);
            match.state = Match.STATE_NEXT_BLACK;
            match.place("black", 2, 3);

            verify(board, times(1)).set(35, 1);
            verify(board, times(1)).set(44, 1);

            verify(iterator, times(168)).set(anyInt(), anyInt(), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(0), eq(0), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(1), eq(0), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(6), eq(0), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(7), eq(0), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(0), eq(1), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(1), eq(1), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(6), eq(1), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(7), eq(1), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(1), eq(2), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(7), eq(2), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(7), eq(4), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(6), eq(5), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(7), eq(5), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(0), eq(6), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(5), eq(6), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(6), eq(6), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(7), eq(6), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(0), eq(7), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(1), eq(7), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(2), eq(7), anyInt(), anyInt());
            verify(iterator, times(8)).set(eq(7), eq(7), anyInt(), anyInt());

            Assert.assertEquals(Match.STATE_NEXT_WHITE, match.getState());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPlaceNextBlackAgain() {
        doReturn(true).when(actionCollection).containsIndex(26);
        doReturn(true).when(board).covers(2, 3);
        doAnswer((Answer<Void>) invocation -> {
            board.set(35, 1);
            board.set(44, 1);
            return null;
        }).when(actionCollection).foreach(eq(26), any(Consumer.class));

        var field = new int[] {
                0, 0, 2, 2, 2, 2, 0, 0,
                0, 0, 2, 2, 2, 1, 0, 0,
                1, 0, 1, 1, 2, 2, 2, 0,
                1, 2, 2, 1, 2, 1, 2, 2,
                1, 1, 1, 1, 1, 1, 2, 0,
                1, 2, 1, 1, 2, 2, 0, 0,
                0, 2, 1, 1, 1, 0, 0, 0,
                0, 0, 0, 1, 1, 1, 1, 0
        };
        for (var i = 0; i < field.length; i++) {
            doReturn(field[i]).when(board).get(i);
        }

        doReturn(Map.entry(-1, -1)).when(iterator).next();
        doReturn(false).doReturn(true)
                .when(actionCollection).anyIndices();

        try {
            var match = new Match<>(0, "black", "white", board);
            match.state = Match.STATE_NEXT_BLACK;
            match.place("black", 2, 3);

            verify(board, times(1)).set(35, 1);
            verify(board, times(1)).set(44, 1);

            verify(iterator, times(336)).set(anyInt(), anyInt(), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(0), eq(0), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(1), eq(0), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(6), eq(0), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(7), eq(0), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(0), eq(1), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(1), eq(1), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(6), eq(1), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(7), eq(1), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(1), eq(2), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(7), eq(2), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(7), eq(4), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(6), eq(5), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(7), eq(5), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(0), eq(6), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(5), eq(6), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(6), eq(6), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(7), eq(6), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(0), eq(7), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(1), eq(7), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(2), eq(7), anyInt(), anyInt());
            verify(iterator, times(16)).set(eq(7), eq(7), anyInt(), anyInt());

            Assert.assertEquals(Match.STATE_NEXT_BLACK, match.getState());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPlaceTie() {
        doReturn(true).when(actionCollection).containsIndex(26);
        doReturn(true).when(board).covers(2, 3);
        doAnswer((Answer<Void>) invocation -> null).when(actionCollection)
                .foreach(eq(26), any(Consumer.class));

        var field = new int[] {
                0, 0, 2, 2, 2, 2, 0, 0,
                0, 0, 2, 2, 2, 1, 0, 0,
                1, 0, 1, 1, 2, 2, 2, 0,
                1, 2, 2, 1, 2, 2, 2, 2,
                1, 1, 1, 1, 1, 1, 2, 0,
                1, 2, 1, 1, 2, 2, 0, 0,
                0, 2, 1, 1, 1, 0, 0, 0,
                0, 0, 0, 1, 1, 1, 0, 0
        };
        for (var i = 0; i < field.length; i++) {
            doReturn(field[i]).when(board).get(i);
        }

        doReturn(Map.entry(-1, -1)).when(iterator).next();
        doReturn(false).when(actionCollection).anyIndices();

        try {
            var match = new Match<>(0, "black", "white", board);
            match.state = Match.STATE_NEXT_BLACK;
            match.place("black", 2, 3);

            Assert.assertEquals(Match.STATE_TIE, match.getState());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testBlackWon() {
        doReturn(true).when(actionCollection).containsIndex(26);
        doReturn(true).when(board).covers(2, 3);
        doAnswer((Answer<Void>) invocation -> null).when(actionCollection)
                .foreach(eq(26), any(Consumer.class));

        var field = new int[] {
                0, 0, 2, 2, 2, 2, 0, 0,
                0, 0, 2, 2, 2, 1, 0, 0,
                1, 0, 1, 1, 2, 2, 2, 0,
                1, 1, 1, 1, 2, 2, 2, 2,
                1, 1, 1, 1, 1, 1, 2, 0,
                1, 2, 1, 1, 1, 1, 0, 0,
                0, 2, 1, 1, 1, 0, 0, 0,
                0, 0, 0, 1, 1, 1, 1, 0
        };
        for (var i = 0; i < field.length; i++) {
            doReturn(field[i]).when(board).get(i);
        }

        doReturn(Map.entry(-1, -1)).when(iterator).next();
        doReturn(false).when(actionCollection).anyIndices();

        try {
            var match = new Match<>(0, "black", "white", board);
            match.state = Match.STATE_NEXT_BLACK;
            match.place("black", 2, 3);

            Assert.assertEquals(Match.STATE_WON_BLACK, match.getState());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPlaceWhiteWon() {
        doReturn(true).when(actionCollection).containsIndex(26);
        doReturn(true).when(board).covers(2, 3);
        doAnswer((Answer<Void>) invocation -> null).when(actionCollection)
                .foreach(eq(26), any(Consumer.class));

        var field = new int[] {
                0, 0, 2, 2, 2, 2, 0, 0,
                0, 0, 2, 2, 2, 1, 0, 0,
                1, 0, 2, 2, 2, 2, 2, 0,
                1, 2, 2, 1, 2, 2, 2, 2,
                1, 1, 1, 1, 2, 2, 2, 0,
                1, 2, 2, 2, 2, 2, 0, 0,
                0, 2, 1, 1, 1, 0, 0, 0,
                0, 0, 0, 1, 1, 1, 0, 0
        };
        for (var i = 0; i < field.length; i++) {
            doReturn(field[i]).when(board).get(i);
        }

        doReturn(Map.entry(-1, -1)).when(iterator).next();
        doReturn(false).when(actionCollection).anyIndices();

        try {
            var match = new Match<>(0, "black", "white", board);
            match.state = Match.STATE_NEXT_BLACK;
            match.place("black", 2, 3);

            Assert.assertEquals(Match.STATE_WON_WHITE, match.getState());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
