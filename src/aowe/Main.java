package aowe;

import aowe.actions.*;
import aowe.listeners.GlobalKeyListener;
import aowe.utils.SwingUtil;
import aowe.utils.Util;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.opencv.core.Core;

import javax.swing.*;
import java.awt.*;
import java.util.logging.LogManager;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class Main extends JFrame {

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {

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

        GlobalScreen.addNativeKeyListener(gkl);

        SwingUtilities.invokeLater(() -> {
            Main ex = new Main();
        });

    }

    private void initUI() {

        // main jcomponents always being the same
        JPanel mainPanel = SwingUtil.createMainPanel();
        JPanel gamePanel = SwingUtil.createGamePanel();

        JTextField resetsTf = new JTextField("5");
        resetsTf.setFont(new Font("serif", Font.BOLD, 20));

        JTextArea emptyComponent = new JTextArea();
        emptyComponent.setBackground(Color.WHITE);

        JButton infoBtn = new JButton("Info;");
        AbstractAction infoAction = new InfoAction();
        infoBtn.addActionListener(infoAction);

        gamePanel.add(infoBtn);
        gamePanel.add(resetsTf);
        gamePanel.add(emptyComponent);

        // game buttons
        AbstractAction hydraAction = new HydraAction(resetsTf);
        AbstractAction firstSightAction = new FirstSightAction();
        AbstractAction mysticTowerAction = new MysticTerrainAction();
        AbstractAction gemSearchAction = new GemSearchAction();
        AbstractAction parallelSpaceAction = new ParallelSpaceAction();
        AbstractAction magicTowerAction = new MagicTowerAction();


        gamePanel.add(SwingUtil.createImageButton(Util.MAGIC_TOWER_BACKGROUND, magicTowerAction));
        gamePanel.add(SwingUtil.createImageButton(Util.MYSTIC_TERRAIN_BACKGROUND, mysticTowerAction));
        gamePanel.add(SwingUtil.createImageButton(Util.PARALLEL_SPACE_BACKGROUND, parallelSpaceAction));

        gamePanel.add(SwingUtil.createImageButton(Util.HYDRA_BACKGROUND, hydraAction));
        gamePanel.add(SwingUtil.createImageButton(Util.FIRST_SIGHT_BACKGROUND, firstSightAction));
        gamePanel.add(SwingUtil.createImageButton(Util.GEM_SEARCH_BACKGROUND, gemSearchAction));

        mainPanel.add(gamePanel);

        pack();
        add(mainPanel);

        setTitle("AOWE Bot by MoonMoon");
        setSize(400, 320);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }


}
