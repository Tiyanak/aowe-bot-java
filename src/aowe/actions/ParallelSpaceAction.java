package aowe.actions;

import aowe.game.Game;
import aowe.game.GameManager;
import aowe.game.ParallelSpaceGame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class ParallelSpaceAction extends AbstractAction {

    public ParallelSpaceAction() {
        super("Parallel Space");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Game game = new ParallelSpaceGame();
        GameManager.setGame(game);
        GameManager.play();
    }

}
