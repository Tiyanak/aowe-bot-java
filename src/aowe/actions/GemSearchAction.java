package aowe.actions;

import aowe.game.Game;
import aowe.game.GameManager;
import aowe.game.GemSearchGame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class GemSearchAction extends AbstractAction {

    public GemSearchAction() {
        super("Gem Search");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Game game = new GemSearchGame();
        GameManager.setGame(game);
        GameManager.play();

    }

}
