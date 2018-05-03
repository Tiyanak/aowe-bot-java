package aowe.actions;

import aowe.game.Game;
import aowe.game.GameManager;
import aowe.game.MagicTowerGame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class MagicTowerAction extends AbstractAction {

    public MagicTowerAction() {
        super("Magic Tower");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Game game = new MagicTowerGame();
        GameManager.setGame(game);
        GameManager.play();
    }
}
