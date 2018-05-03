package aowe.actions;

import aowe.game.FirstSightGame;
import aowe.game.Game;
import aowe.game.GameManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class FirstSightAction extends AbstractAction {

    public FirstSightAction() {
        super("First Sight");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Game firstSight = new FirstSightGame();
        GameManager.setGame(firstSight);
        GameManager.play();

    }

}
