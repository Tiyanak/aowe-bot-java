package aowe.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class SwingUtil {

    public static JButton createImageButton(String imageName, AbstractAction action)  {

        Image image = null;
        try {
            image = ImageIO.read(new File(imageName)).getScaledInstance(110, 80,  Image.SCALE_SMOOTH );
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image finalImage = image;
        JButton jBtn = new JButton(imageName){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalImage, 0, 0, null);
            }
        };

        jBtn.setMinimumSize(new Dimension(110, 80));

        jBtn.addActionListener(action);

        return jBtn;

    }

    public static JPanel createMainPanel() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        return mainPanel;
    }

    public static JPanel createGamePanel() {

        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(new Color(0,0,0,0));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        gamePanel.setLayout(new GridLayout(3, 3, 20, 10));
        gamePanel.setSize(new Dimension(400, 200));

        return gamePanel;
    }

}
