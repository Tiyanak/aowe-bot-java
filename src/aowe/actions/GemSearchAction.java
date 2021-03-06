package aowe.actions;

import aowe.game.GemSearch;
import aowe.game.Hydra;
import aowe.helper.GlobalKeyListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.LogManager;

/**
 * Created by Igor Farszky on 15.10.2017..
 */
public class GemSearchAction extends AbstractAction {

    public GemSearchAction(String name) {
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

        GemSearch gemSearch = new GemSearch();
        gkl.addGame(gemSearch);
        GlobalScreen.addNativeKeyListener(gkl);
        gemSearch.play();

    }
}
