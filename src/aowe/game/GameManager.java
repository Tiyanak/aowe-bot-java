package aowe.game;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class GameManager {

    private static Game game;
    private static boolean doPlay = false;

    public static void play() {
        while (doPlay) {
            game.play();
        }
    }

    public void start() {
        doPlay = true;
        play();
    }

    public void stop() {
        doPlay = false;
    }

    public static void togglePlaying() {
        doPlay = !doPlay;
        if (!doPlay) play();
    }

    public static void setGame(Game game) {
        GameManager.game = game;
    }
}
