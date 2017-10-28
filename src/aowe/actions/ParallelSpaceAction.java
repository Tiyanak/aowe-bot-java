package aowe.actions;

import aowe.game.ParallelSpace;
import aowe.helper.GlobalKeyListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.LogManager;

/**
 * Created by Igor Farszky on 23.10.2017..
 */
public class ParallelSpaceAction extends AbstractAction {

    public ParallelSpaceAction(String name) {
        super(name);
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

        ParallelSpace parallelSpace = new ParallelSpace();
        gkl.addGame(parallelSpace);
        GlobalScreen.addNativeKeyListener(gkl);
        parallelSpace.play();
    }

}
