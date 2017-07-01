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

    public void click(int x, int y) {
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

}
