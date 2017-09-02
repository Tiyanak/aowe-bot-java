package aowe.actions;

import aowe.game.Hydra;
import aowe.helper.GlobalKeyListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.LogManager;

/**
 * Created by Igor Farszky on 2.7.2017..
 */
public class HydraAction extends AbstractAction {

    private JTextField levelsTf;
    private JCheckBox untilDeadCb;

    public HydraAction(String name, JTextField levelsTf, JCheckBox untilDeadCb) {
        super(name);
        this.levelsTf = levelsTf;
        this.untilDeadCb = untilDeadCb;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        LogManager.getLogManager().reset();
        LogManager.getLogManager().getLogger(GlobalScreen.class.getPackage().getName());

        GlobalKeyListener gkl = new GlobalKeyListener();

        Hydra hydra = new Hydra(Integer.valueOf(levelsTf.getText()));
        hydra.setUntilDead(untilDeadCb.isSelected());
        gkl.addGame(hydra);
        GlobalScreen.addNativeKeyListener(gkl);
        hydra.play();
    }
}
