package aowe.actions;

import aowe.game.Game;
import aowe.game.GameManager;
import aowe.game.HydraGame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class HydraAction extends AbstractAction {

    private JTextField resetsTf;

    public HydraAction(JTextField resetsTf) {
        super("Hydra");
        this.resetsTf = resetsTf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Game hydra = new HydraGame(Integer.valueOf(resetsTf.getText()));
        GameManager.setGame(hydra);
        GameManager.play();

    }
}
