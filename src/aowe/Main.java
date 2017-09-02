package aowe;

import aowe.actions.FirstSightAction;
import aowe.actions.HydraAction;
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
        try {
            hydra_back = ImageIO.read(new File(Constants.HYDRA_BACKGROUND)).getScaledInstance(110, 50,  java.awt.Image.SCALE_SMOOTH );
            first_sight_back = ImageIO.read(new File(Constants.FIRST_SIGHT_BACKGROUND)).getScaledInstance(110, 50,  java.awt.Image.SCALE_SMOOTH );
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
        gamePanel.setMaximumSize(new Dimension(400, 200));

        Image finalHydra_back = hydra_back;
        JButton hydraBtn = new JButton("Hydra"){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalHydra_back, 0, 0, null);
                setOpaque(true);
            }
        };
        Image finalFirst_sight_back = first_sight_back;
        JButton firstSightBtn = new JButton("First sight"){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalFirst_sight_back, 0, 0, null);
            }
        };

        JTextField levelsTf = new JTextField("999");
        levelsTf.setFont(new Font("serif", Font.BOLD, 20));
        JCheckBox untilDeadCb = new JCheckBox("Until dead");
        untilDeadCb.setBackground(Color.WHITE);
        untilDeadCb.setSize(30, 30);
        untilDeadCb.setFont(new Font("serif", Font.BOLD, 16));
        untilDeadCb.setMinimumSize(new Dimension(30, 30));
        untilDeadCb.setSelected(false);

        JTextArea logArea = new JTextArea("");
        logArea.setMaximumSize(new Dimension(400, 350));
        logArea.setSize(400, 350);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setEnabled(true);
        logArea.setEditable(true);
        logArea.setFont(new Font("serif", Font.PLAIN, 16));

        JTextArea info1 = new JTextArea("Number of levels to pass");
        JTextArea info2 = new JTextArea("False: Stoping game on 1 life left; True: Playing until all lifes lost");
        JTextArea info3 = new JTextArea("Press 1 to RESUME game");
        JTextArea info4 = new JTextArea("Press 2 to PAUSE game");
        JTextArea info5 = new JTextArea("Press 3 to QUIT");
        info1.setLineWrap(true);
        info1.setWrapStyleWord(true);
        info1.setEnabled(false);
        info1.setDisabledTextColor(Color.GREEN);
        info1.setFont(new Font("serif", Font.BOLD, 10));
        info2.setLineWrap(true);
        info2.setWrapStyleWord(true);
        info2.setEnabled(false);
        info2.setDisabledTextColor(Color.GREEN);
        info2.setFont(new Font("serif", Font.BOLD, 10));
        info3.setLineWrap(true);
        info3.setWrapStyleWord(true);
        info3.setEnabled(false);
        info3.setDisabledTextColor(Color.GREEN);
        info3.setFont(new Font("serif", Font.BOLD, 12));
        info4.setLineWrap(true);
        info4.setWrapStyleWord(true);
        info4.setEnabled(false);
        info4.setDisabledTextColor(Color.GREEN);
        info4.setFont(new Font("serif", Font.BOLD, 12));
        info5.setLineWrap(true);
        info5.setWrapStyleWord(true);
        info5.setEnabled(false);
        info5.setDisabledTextColor(Color.GREEN);
        info5.setFont(new Font("serif", Font.BOLD, 12));

        JScrollPane logPanel = new JScrollPane(logArea);
        logPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        logPanel.setSize(new Dimension(400, 350));
        logPanel.setMaximumSize(new Dimension(400, 350));
        logPanel.setEnabled(true);
        logPanel.setVisible(true);

        HydraAction hydraAction = new HydraAction("Hydra", levelsTf, untilDeadCb, logArea);
        FirstSightAction firstSight = new FirstSightAction();
        hydraBtn.addActionListener(hydraAction);
        firstSightBtn.addActionListener(firstSight);

        gamePanel.add(hydraBtn);
        gamePanel.add(levelsTf);
        gamePanel.add(untilDeadCb);
        gamePanel.add(firstSightBtn);
        gamePanel.add(info1);
        gamePanel.add(info2);
        gamePanel.add(info3);
        gamePanel.add(info4);
        gamePanel.add(info5);

        mainPanel.add(gamePanel);
        mainPanel.add(logPanel);

        pack();
        add(mainPanel);

        setTitle("AOWE Bot by Tiyanak");
        setSize(400, 550);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

}