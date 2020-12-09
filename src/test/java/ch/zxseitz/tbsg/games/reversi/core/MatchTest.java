package ch.zxseitz.tbsg.games.reversi.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;

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
    public void testPlace() {
        //todo
    }
}
