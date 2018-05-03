package aowe.actions;

import aowe.game.Game;
import aowe.game.GameManager;
import aowe.game.MysticTerrainGame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class MysticTerrainAction extends AbstractAction {

    public MysticTerrainAction() {
        super("Mystic terrain");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Game game = new MysticTerrainGame();
        GameManager.setGame(game);
        GameManager.play();
    }

}
