package aowe.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Igor Farszky on 27.10.2017..
 */
public class InfoAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame jFrame = new JFrame();
        JPanel jPanel = new JPanel();

        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JTextArea infoArea = new JTextArea("1 - Resume the game\n2 - Pause the game\n3 - quit the game\n\n" +
                "Hydra - choose number of lvls you want to bot is playing, and choose the checkbox, if checkbox is chosen, " +
                "hydra will play until you lost all 5 lifes, else, it will play until only 1 life\n\n" +
                "First sight - game must be in full screen of nox, full screen on right tool panel, not window full screen, " +
                "and then need to write s to start every iteration of the game, or q if you want to quit the game in the " +
                "System input console\n\n" +
                "Magic Tower - playing all the way until loose all stamina, finding only closest battle to yourself, tower dont know best ways to boss way, " +
                "its playing until it founds it randomly\n\n" +
                "Mystic terrain - playing until all battles and chests are not open before the boss, then going to another city until all cities " +
                "are dead\n\n" +
                "Parallel space - found all cities first, then iterate over all citiyes every city fighintg 5 times, it has possibility to click " +
                "on home city right bottom if internet breaks a little");
        infoArea.setSize(400, 500);
        infoArea.setFont(new Font("Serif", Font.ITALIC, 16));
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);

        jPanel.add(infoArea);

        jFrame.add(jPanel);
        jFrame.pack();

        jFrame.setTitle("AOWE Bot by MoonMoon");
        jFrame.setSize(400, 500);

        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

    }
}
