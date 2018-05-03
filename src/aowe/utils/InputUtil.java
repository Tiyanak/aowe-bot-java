package aowe.utils;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class InputUtil {

    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void mouseMoveAndClick(int x, int y) {
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public static void mouseClick() {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public static void mouseMove(int x, int y) {
        robot.mouseMove(x, y);
    }

    public static void keyboardPress(KeyEvent keyEvent) {
        robot.keyPress(keyEvent.getExtendedKeyCode());
    }

}
