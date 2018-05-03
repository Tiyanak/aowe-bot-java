package aowe.helper;

import aowe.game.Game;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * Created by Igor Farszky on 1.7.2017..
 */
public class GlobalKeyListener implements NativeKeyListener {

    Game game;

    public GlobalKeyListener() {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (game != null) {
            if (e.getKeyCode() == NativeKeyEvent.VC_1) {
                if (!game.isPlaying()){
                    System.out.println("STARTING GAME AGAIN");
                    game.start();
                    game.setIsPlaying(true);
                }
            }else{
                if (e.getKeyCode() == NativeKeyEvent.VC_2){
                    if (game.isPlaying()){
                        System.out.println("STOPPING THE GAME");
                        game.stop();
                        game.setIsPlaying(false);
                    }
                }
            }
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_LEFT) {
            game.fromLeft();
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_DOWN) {
            game.fromBottom();
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
            game.fromRight();
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_UP) {
            game.fromUp();
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_3){
            System.out.println("EXITING, GOODBYE LORD");
            System.exit(0);
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    public void addGame(Game game){
        this.game = game;
    }

}
