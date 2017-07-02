package aowe;

import aowe.actions.FirstSightAction;
import aowe.actions.HydraAction;
import aowe.game.FirstSight;
import aowe.game.Hydra;
import aowe.helper.GlobalKeyListener;
import jdk.nashorn.internal.objects.Global;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.opencv.core.Core;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.LogManager;


/**
 * Created by Igor Farszky on 1.7.2017..
 */
public class Main extends JFrame{

//    private static GlobalKeyListener gkl;

    public Main(){
        initUI();
    }

    public static void main(String[] args) throws IOException {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        SwingUtilities.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });

        Scanner sc = new Scanner(System.in);

        System.out.println("AOWE AUTOPLAY BOT BY TIYANAK");

        String command = "";

//        try {
//            GlobalScreen.registerNativeHook();
//        }
//        catch (NativeHookException ex) {
//            System.err.println("There was a problem registering the native hook.");
//            System.err.println(ex.getMessage());
//
//            System.exit(1);
//        }
//
//        LogManager.getLogManager().reset();
//        LogManager.getLogManager().getLogger(GlobalScreen.class.getPackage().getName());

//        while(true){
//            System.out.println("INFO: 1 - continue playing, 2 - pause playing, 3 - quit game");
//            System.out.print("Input (h=hydra, h number=hydra with defined levels, s=firstSight) : ");
//            command = sc.nextLine();
//
//            gkl = new GlobalKeyListener();
//
//            if (command.equalsIgnoreCase("h")) {
//                startHydra(false);
//                break;
//            } else if (command.matches("^h [0-9]+$")) {
//                String numbers = command.trim().replaceAll("[^0-9]+", "");
//                startHydra(Integer.valueOf(numbers), false);
//                break;
//            }else if(command.contains("--ud")){
//                command = command.replaceAll("--ud", "");
//                if (command.matches("^h[ ]*$")) {
//                    startHydra(true);
//                    break;
//                } else if (command.matches("^h[ ]*[0-9]+[^0-9]*$")) {
//                    String numbers = command.trim().replaceAll("[^0-9]+", "");
//                    startHydra(Integer.valueOf(numbers), true);
//                    break;
//                }
//            } else if(command.equalsIgnoreCase("s")) {
//                startFirstSight();
//                break;
//            }else if (command.equalsIgnoreCase("q")) {
//                quit();
//            }else{
//                continue;
//            }
//
//        }

    }

    private void initUI() {


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel gamePanel = new JPanel();
        gamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        gamePanel.setLayout(new GridLayout(2, 3, 10, 20));
        gamePanel.setSize(new Dimension(500, 150));
        gamePanel.setMaximumSize(new Dimension(500, 150));

        JButton hydraBtn = new JButton("Hydra");
        JButton firstSightBtn = new JButton("First sight");
        JTextField levelsTf = new JTextField("999");
        JCheckBox untilDeadCb = new JCheckBox("Until dead");
        untilDeadCb.setSelected(false);

        JTextArea logArea = new JTextArea("");
        logArea.setMaximumSize(new Dimension(500, 450));
        logArea.setSize(500, 450);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setEnabled(true);
        logArea.setEditable(true);

        HydraAction hydraAction = new HydraAction("Hydra", levelsTf, untilDeadCb, logArea);
        FirstSightAction firstSight = new FirstSightAction();
        hydraBtn.addActionListener(hydraAction);
        firstSightBtn.addActionListener(firstSight);
        JTextArea info1 = new JTextArea("Number of levels to pass\nClick 1 to resume bot\nClick 2 to pause bot");
        JTextArea info2 = new JTextArea("Should play until death? If not, playing until 1 life left\nClick 3 to quit");
        info1.setLineWrap(true);
        info1.setWrapStyleWord(true);
        info1.setEnabled(false);
        info1.setDisabledTextColor(Color.black);
        info2.setLineWrap(true);
        info2.setWrapStyleWord(true);
        info2.setEnabled(false);
        info2.setDisabledTextColor(Color.black);

        JScrollPane logPanel = new JScrollPane(logArea);
        logPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        logPanel.setSize(new Dimension(500, 450));
        logPanel.setMaximumSize(new Dimension(500, 450));

        gamePanel.add(hydraBtn);
        gamePanel.add(levelsTf);
        gamePanel.add(untilDeadCb);
        gamePanel.add(firstSightBtn);
        gamePanel.add(info1);
        gamePanel.add(info2);

        mainPanel.add(gamePanel);
        mainPanel.add(logPanel);

        add(mainPanel);
        pack();

        setTitle("AOWE Bot by Tiyanak");
        setSize(500, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

//    private static void startHydra(int levels, boolean untilDead){
//        Hydra hydra = new Hydra(levels);
//        hydra.setUntilDead(untilDead);
//        gkl.addGame(hydra);
//        GlobalScreen.addNativeKeyListener(gkl);
//        hydra.play();
//    }
//
//    private static void startHydra(boolean untilDead){
//        Hydra hydra = new Hydra();
//        hydra.setUntilDead(untilDead);
//        gkl.addGame(hydra);
//        GlobalScreen.addNativeKeyListener(gkl);
//        hydra.play();
//    }

//    private static void startFirstSight(){
//        FirstSight firstSight = new FirstSight();
//        gkl.addGame(firstSight);
//        GlobalScreen.addNativeKeyListener(gkl);
//        firstSight.play();
//    }

//    private static void quit(){
//        System.out.println("EXITING, GOODBYE LORD");
//        System.exit(0);
//    }

}