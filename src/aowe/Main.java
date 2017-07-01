package aowe;

import aowe.game.FirstSight;
import aowe.game.Hydra;
import aowe.helper.GlobalKeyListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.opencv.core.Core;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.LogManager;


/**
 * Created by Igor Farszky on 1.7.2017..
 */
public class Main {

    private static GlobalKeyListener gkl;

    public static void main(String[] args) throws IOException {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Scanner sc = new Scanner(System.in);

        System.out.println("AOWE AUTOPLAY BOT BY TIYANAK");

        String command = "";

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

        while(true){
            System.out.println("INFO: 1 - continue playing, 2 - pause playing, 3 - quit game");
            System.out.print("Input (h=hydra, s=firstSight) : ");
            command = sc.next();

            gkl = new GlobalKeyListener();

            switch (command) {
                case "h" : startHydra(); break;
                case "s" : startFirstSight(); break;
                case "q" : quit(); break;
                default : continue;
            }

        }

    }

    private static void startHydra(){
        Hydra hydra = new Hydra();
        gkl.addGame(hydra);
        GlobalScreen.addNativeKeyListener(gkl);
        hydra.play();
    }

    private static void startFirstSight(){
        FirstSight firstSight = new FirstSight();
        gkl.addGame(firstSight);
        GlobalScreen.addNativeKeyListener(gkl);
        firstSight.play();
    }

    private static void quit(){
        System.out.println("EXITING, GOODBYE LORD");
        System.exit(0);
    }

}