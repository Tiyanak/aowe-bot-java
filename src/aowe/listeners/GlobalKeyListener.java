package aowe.listeners;

import aowe.game.Game;
import aowe.game.GameManager;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class GlobalKeyListener implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_1) {
            GameManager.togglePlaying();
        }

        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_2){
            System.exit(0);
        }

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {}

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {}
}
