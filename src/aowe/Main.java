package aowe;

import aowe.actions.*;
import aowe.helper.Constants;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


/**
 * Created by Igor Farszky on 1.7.2017..
 */
public class Main extends JFrame{

    public Main(){
        initUI();
    }

    public static void main(String[] args) throws IOException {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        SwingUtilities.invokeLater(() -> {
            Main ex = new Main();
        });

    }

    private void initUI() {

        Image hydra_back = null;
        Image first_sight_back = null;
        Image mystic_tower_back = null;
        Image gem_search_back = null;
        Image para_space_back = null;
        Image tower_back = null;
        try {
            hydra_back = ImageIO.read(new File(Constants.HYDRA_BACKGROUND)).getScaledInstance(110, 80,  java.awt.Image.SCALE_SMOOTH );
            first_sight_back = ImageIO.read(new File(Constants.FIRST_SIGHT_BACKGROUND)).getScaledInstance(110, 80,  java.awt.Image.SCALE_SMOOTH );
            mystic_tower_back = ImageIO.read(new File(Constants.MYSTIC_TOWER_BACKGROUND)).getScaledInstance(110, 80,  java.awt.Image.SCALE_SMOOTH );
            gem_search_back = ImageIO.read(new File(Constants.GEM_SEARCH_BACKGROUND)).getScaledInstance(110, 80,  java.awt.Image.SCALE_SMOOTH );
            para_space_back = ImageIO.read(new File(Constants.PARALLEL_SPACE_BACKGROUND)).getScaledInstance(110, 80,  java.awt.Image.SCALE_SMOOTH );
            tower_back = ImageIO.read(new File(Constants.TOWER_BACKGROUND)).getScaledInstance(110, 80,  java.awt.Image.SCALE_SMOOTH );
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(new Color(0,0,0,0));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        gamePanel.setLayout(new GridLayout(3, 3, 20, 10));
        gamePanel.setSize(new Dimension(400, 200));

        JButton infoBtn = new JButton("Info");
        Image finalHydra_back = hydra_back;
        JButton hydraBtn = new JButton("Hydra"){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalHydra_back, 0, 0, null);
                setOpaque(true);
            }
        };
        hydraBtn.setMinimumSize(new Dimension(110, 80));

        Image finalFirst_sight_back = first_sight_back;
        JButton firstSightBtn = new JButton("First sight"){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalFirst_sight_back, 0, 0, null);
            }
        };
        firstSightBtn.setMinimumSize(new Dimension(110, 80));

        Image finalMystic_back = mystic_tower_back;
        JButton mysticTowerBtn = new JButton("Mystic tower"){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalMystic_back, 0, 0, null);
            }
        };
        mysticTowerBtn.setMinimumSize(new Dimension(110, 80));

        Image finalGemSearch_back = gem_search_back;
        JButton gemSearchBtn = new JButton("Gem Search"){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalGemSearch_back, 0, 0, null);
            }
        };
        gemSearchBtn.setMinimumSize(new Dimension(110, 80));

        Image para_space_back_img = para_space_back;
        JButton paraSpaceBtn = new JButton("Parallel space"){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(para_space_back_img, 0, 0, null);
            }
        };
        paraSpaceBtn.setMinimumSize(new Dimension(110, 80));

        Image tower_img = tower_back;
        JButton towerBtn = new JButton("Tower"){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(tower_img, 0, 0, null);
            }
        };
        towerBtn.setMinimumSize(new Dimension(110, 80));

        JTextField levelsTf = new JTextField("5");
        levelsTf.setFont(new Font("serif", Font.BOLD, 20));
        JCheckBox untilDeadCb = new JCheckBox("Until dead");
        untilDeadCb.setBackground(Color.WHITE);
        untilDeadCb.setSize(30, 30);
        untilDeadCb.setFont(new Font("serif", Font.BOLD, 16));
        untilDeadCb.setMinimumSize(new Dimension(30, 30));
        untilDeadCb.setSelected(false);

        JTextArea info_empty = new JTextArea("");

        InfoAction infoAction = new InfoAction();
        HydraAction hydraAction = new HydraAction("Hydra", levelsTf, untilDeadCb);
        FirstSightAction firstSightAction = new FirstSightAction("First sight");
        MysticTowerAction mysticTowerAction = new MysticTowerAction("Mystic Terrain");
        GemSearchAction gemSearchAction = new GemSearchAction("Gem Search");
        ParallelSpaceAction parallelSpaceAction = new ParallelSpaceAction("Parallel space");
        TowerAction towerAction = new TowerAction("Tower");

        infoBtn.addActionListener(infoAction);
        hydraBtn.addActionListener(hydraAction);
        firstSightBtn.addActionListener(firstSightAction);
        mysticTowerBtn.addActionListener(mysticTowerAction);
        gemSearchBtn.addActionListener(gemSearchAction);
        paraSpaceBtn.addActionListener(parallelSpaceAction);
        towerBtn.addActionListener(towerAction);

        gamePanel.add(infoBtn);
        gamePanel.add(levelsTf);
        gamePanel.add(untilDeadCb);

        gamePanel.add(towerBtn);
        gamePanel.add(mysticTowerBtn);
        gamePanel.add(paraSpaceBtn);

        gamePanel.add(hydraBtn);
        gamePanel.add(gemSearchBtn);
        gamePanel.add(firstSightBtn);

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