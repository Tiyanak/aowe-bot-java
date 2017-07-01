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
public class FirstSight implements Game{

    private Map<String, Mat> templates;
    private boolean isPlaying;
    private boolean shouldPlay;

    public FirstSight() {
        initTemplates();
        this.isPlaying = false;
        this.shouldPlay = true;
    }

    private void initTemplates() {
        this.templates = new HashMap<>();
        this.templates.put(Constants.SIGHT_START, Imgcodecs.imread(Constants.AOWE_ASSETS + Constants.SIGHT_START + Constants.PNG_EXT));
        this.templates.put(Constants.SIGHT_HERO_HIGHLIGHT, Imgcodecs.imread(Constants.AOWE_ASSETS + Constants.SIGHT_HERO_HIGHLIGHT + Constants.PNG_EXT));
        this.templates.put(Constants.SIGHT_HERO_GUESS, Imgcodecs.imread(Constants.AOWE_ASSETS + Constants.SIGHT_HERO_GUESS + Constants.PNG_EXT));
    }

    public void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void play(){

        this.isPlaying = true;
        KeyPresser keyPresser = new KeyPresser();
        Scanner sc = new Scanner(System.in);

        while(true){

            if (!shouldPlay){
               sleep(1000);
               continue;
            }

            System.out.print("SIGHT COMMAND (s=start, q=quit) : ");
            String command = sc.next();

            if (command.equalsIgnoreCase("s")){
                System.out.println("GAME STARTED");
                sleep(2000);
            }else if(command.equalsIgnoreCase("q")){
                break;
            }else{
                continue;
            }

            // click start aowe.game
            List<Battle> start = CV.matchingHydraTemplates(ScreenHelper.GetCurrentScreenImage(), this.templates.get(Constants.SIGHT_START), true, false, true, Constants.MATCHING_PRECISION);
            keyPresser.click(start.get(0).getX(), start.get(0).getY());

            // sleep until 1 second of image revield
            sleep(4000);
            keyPresser.click(100, 100);

            // take init screenshot
            Mat screenFrame = ScreenHelper.GetCurrentScreenImage();
            Mat heroes = screenFrame.submat(326, 920, 386, 1131);

            List<Sight> hero_highlight = CV.matchingSightTemplates(screenFrame, templates.get(Constants.SIGHT_HERO_HIGHLIGHT), 1, true);

            Mat resizedImage = new Mat();
            Size sz = new Size(templates.get(Constants.SIGHT_HERO_GUESS).rows(), templates.get(Constants.SIGHT_HERO_GUESS).cols());
            Imgproc.resize(hero_highlight.get(0).getMat(), resizedImage, sz);

            Mat hero_line = resizedImage.submat(132, 134, 6, 131);

            List<Sight> results = CV.matchingSightTemplates(heroes, hero_line, 1, true);
            Sight s = results.get(0);

            System.out.println((s.getX()+386) + " : " + (s.getY()+326));
            keyPresser.click(s.getX()+386, s.getY()+326);

            sleep(1000);

        }

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
