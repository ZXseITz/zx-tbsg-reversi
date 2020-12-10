package ch.zxseitz.tbsg.games.reversi;

import ch.zxseitz.tbsg.games.TbsgGame;
import ch.zxseitz.tbsg.games.TbsgWebHook;

@TbsgGame("reversi")
public class Reversi {
    @TbsgWebHook(path = "index", method = TbsgWebHook.Method.GET)
    public String test() {
        return "test";
    }
}
