package aowe.game;

import aowe.helper.CV;
import aowe.helper.Constants;
import aowe.helper.KeyPresser;
import aowe.helper.ScreenHelper;
import aowe.model.Battle;
import aowe.model.Sight;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Igor Farszky on 1.7.2017..
 */
public class FirstSight implements Game {

    private Map<String, Mat> templates;
    private boolean isPlaying;
    private boolean shouldPlay;
    Mat screenFrame = null;
    Mat heroes = null;
    KeyPresser keyPresser = null;

    public FirstSight() {
        initTemplates();
        this.isPlaying = false;
        this.shouldPlay = true;
        keyPresser = new KeyPresser();
    }

    private void initTemplates() {
        this.templates = new HashMap<>();
        this.templates.put(Constants.SIGHT_START, Imgcodecs.imread(Constants.AOWE_ASSETS + Constants.SIGHT_START + Constants.PNG_EXT));
        this.templates.put(Constants.SIGHT_HERO_HIGHLIGHT, Imgcodecs.imread(Constants.AOWE_ASSETS + Constants.SIGHT_HERO_HIGHLIGHT + Constants.PNG_EXT));
        this.templates.put(Constants.SIGHT_HERO_GUESS, Imgcodecs.imread(Constants.AOWE_ASSETS + Constants.SIGHT_HERO_GUESS + Constants.PNG_EXT));
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void play() {

        this.isPlaying = true;
        Scanner sc = new Scanner(System.in);

        while (true) {

            if (!shouldPlay) {
                sleep(5000);
                System.out.println("waiting highlight");
                continue;
            }

            System.out.print("SIGHT COMMAND (s=start, q=quit) : ");
            String command = sc.next();

            if (command.equalsIgnoreCase("s")) {
                System.out.println("GAME STARTED");
                sleep(1000);
            } else if (command.equalsIgnoreCase("q")) {
                break;
            } else {
                continue;
            }

            // moveAndclick start aowe.game
            List<Battle> start = CV.matchingHydraTemplates(ScreenHelper.GetCurrentScreenImage(), this.templates.get(Constants.SIGHT_START), true, false, true, Constants.MATCHING_PRECISION);
            keyPresser.moveAndclick(start.get(0).getX(), start.get(0).getY());

            sleep(1000);
            keyPresser.move(100, 100);

            // take init screenshot
            screenFrame = ScreenHelper.GetCurrentScreenImage();
            heroes = screenFrame.submat(326, 920, 386, 1131);

            shouldPlay = false;

            System.out.println("READY FOR HIGHLIGHT!");

            sleep(1000);

        }

    }

    @Override
    public void fromLeft() {
        System.out.println("LEFT IN CLASS");
        screenFrame = ScreenHelper.GetCurrentScreenImage();
        List<Sight> hero_highlight = CV.matchingSightTemplates(screenFrame, templates.get(Constants.SIGHT_HERO_HIGHLIGHT), 1, true);

        Mat resizedImage = new Mat();
        Size sz = new Size(templates.get(Constants.SIGHT_HERO_GUESS).rows(), templates.get(Constants.SIGHT_HERO_GUESS).cols());
        Imgproc.resize(hero_highlight.get(0).getMat(), resizedImage, sz);

        Mat hero_line = resizedImage.submat(6, 131, 5, 7);

        List<Sight> results = CV.matchingSightTemplates(heroes, hero_line, 1, true);
        Sight s = results.get(0);

        System.out.println((s.getX() + 386) + " : " + (s.getY() + 326));
        keyPresser.moveAndclick(s.getX() + 386, s.getY() + 326);

        ScreenHelper.saveImage(resizedImage, "resized_left");
        ScreenHelper.saveImage(hero_line, "line_left");

        this.shouldPlay = true;
    }

    @Override
    public void fromBottom() {
        System.out.println("BOTTOM IN CLASS");
        screenFrame = ScreenHelper.GetCurrentScreenImage();
        List<Sight> hero_highlight = CV.matchingSightTemplates(screenFrame, templates.get(Constants.SIGHT_HERO_HIGHLIGHT), 1, true);

        Mat resizedImage = new Mat();
        Size sz = new Size(templates.get(Constants.SIGHT_HERO_GUESS).rows(), templates.get(Constants.SIGHT_HERO_GUESS).cols());
        Imgproc.resize(hero_highlight.get(0).getMat(), resizedImage, sz);

        Mat hero_line = resizedImage.submat(132, 134, 6, 131);

        List<Sight> results = CV.matchingSightTemplates(heroes, hero_line, 1, true);
        Sight s = results.get(0);

        System.out.println((s.getX() + 386) + " : " + (s.getY() + 326));
        keyPresser.moveAndclick(s.getX() + 386, s.getY() + 326);

        ScreenHelper.saveImage(resizedImage, "resized_down");
        ScreenHelper.saveImage(hero_line, "line_down");

        this.shouldPlay = true;
    }

    @Override
    public void fromUp() {
        System.out.println("UP IN CLASS");
        screenFrame = ScreenHelper.GetCurrentScreenImage();
        List<Sight> hero_highlight = CV.matchingSightTemplates(screenFrame, templates.get(Constants.SIGHT_HERO_HIGHLIGHT), 1, true);

        Mat resizedImage = new Mat();
        Size sz = new Size(templates.get(Constants.SIGHT_HERO_GUESS).rows(), templates.get(Constants.SIGHT_HERO_GUESS).cols());
        Imgproc.resize(hero_highlight.get(0).getMat(), resizedImage, sz);

        Mat hero_line = resizedImage.submat(7, 9, 8, 131);

        List<Sight> results = CV.matchingSightTemplates(heroes, hero_line, 1, true);
        System.out.println(results.size());
        Sight s = results.get(0);

        System.out.println((s.getX() + 386) + " : " + (s.getY() + 326));
        keyPresser.moveAndclick(s.getX() + 386, s.getY() + 326);

        ScreenHelper.saveImage(resizedImage, "resized_up");
        ScreenHelper.saveImage(hero_line, "line_up");

        this.shouldPlay = true;
    }

    @Override
    public void fromRight() {
        System.out.println("RIGHT IN CLASS");
        screenFrame = ScreenHelper.GetCurrentScreenImage();
        List<Sight> hero_highlight = CV.matchingSightTemplates(screenFrame, templates.get(Constants.SIGHT_HERO_HIGHLIGHT), 1, true);

        Mat resizedImage = new Mat();
        Size sz = new Size(templates.get(Constants.SIGHT_HERO_GUESS).rows(), templates.get(Constants.SIGHT_HERO_GUESS).cols());
        Imgproc.resize(hero_highlight.get(0).getMat(), resizedImage, sz);

        Mat hero_line = resizedImage.submat(6, 131, 131, 133);

        List<Sight> results = CV.matchingSightTemplates(heroes, hero_line, 1, true);
        Sight s = results.get(0);

        System.out.println((s.getX() + 386) + " : " + (s.getY() + 326));
        keyPresser.moveAndclick(s.getX() + 386, s.getY() + 326);

        ScreenHelper.saveImage(resizedImage, "resized_right");
        ScreenHelper.saveImage(hero_line, "line_right");

        this.shouldPlay = true;
    }

    @Override
    public void stop() {
        System.out.println("PAUSING FIRST SIGHT");
        this.shouldPlay = false;
    }

    @Override
    public void start() {
        System.out.println("STARTING FIRST SIGHT AGAIN");
        this.shouldPlay = true;
    }

    @Override
    public boolean isPlaying() {
        return this.isPlaying;
    }

    @Override
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }


}
