package aowe.helper;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Created by Igor Farszky on 1.7.2017..
 */
public class KeyPresser {

    Robot bot;

    public KeyPresser() {
        try {
            this.bot = new Robot();
        } catch (AWTException e) {
            System.out.println("FAILED TO INITIALIZE KEY PRESSER");
        }
    }

    public void moveAndclick(int x, int y) {
        bot.mouseMove(0, 0);
        int xT = (int)MouseInfo.getPointerInfo().getLocation().getX();
        int yT = (int)MouseInfo.getPointerInfo().getLocation().getY();
        System.out.println("My location: " + xT + ":" + yT);
        x = (int) (0.8 * x);
        y = (int) (0.8 * y);
        System.out.println("Click: " + x + ":" + y);
        bot.mouseMove(x, y);
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
        sleep(100);
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void keyPress(int key){
        bot.keyPress(key);
        bot.keyRelease(key);
    }

    public void move(int x, int y) {
        bot.mouseMove(x, y);
    }

    public void click() {
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
        sleep(100);
    }

}
