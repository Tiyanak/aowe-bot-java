package aowe.actions;

import aowe.game.Tower;
import aowe.helper.GlobalKeyListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.LogManager;

/**
 * Created by Igor Farszky on 26.10.2017..
 */
public class TowerAction extends AbstractAction  {

    public TowerAction(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        LogManager.getLogManager().reset();
        LogManager.getLogManager().getLogger(GlobalScreen.class.getPackage().getName());

        GlobalKeyListener gkl = new GlobalKeyListener();

        Tower tower = new Tower();
        gkl.addGame(tower);
        GlobalScreen.addNativeKeyListener(gkl);
        tower.play();

    }

}
