package aowe.game;

import aowe.helper.CV;
import aowe.helper.Constants;
import aowe.helper.KeyPresser;
import aowe.helper.ScreenHelper;
import aowe.model.Battle;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Igor Farszky on 23.10.2017..
 */
public class ParallelSpace implements Game {

    private Map<String, Mat> templates;
    private int levels;
    private KeyPresser keyPresser;
    private boolean isPlaying;
    private boolean shouldPlay;
    private boolean untilDead;

    public ParallelSpace() {
        this.keyPresser = new KeyPresser();
        initTemplates();
        levels = 999*10;
        this.isPlaying = false;
        this.shouldPlay = true;
        this.untilDead = false;
    }

    public void initTemplates() {
        this.templates = new HashMap<>();

        for (String para_temp : Constants.ASSETS) {
            String path = Constants.AOWE_ASSETS + para_temp + Constants.PNG_EXT;
            try {
                this.templates.put(para_temp, Imgcodecs.imread(path));
            } catch (Exception e) {
            }
        }
    }

    public void play() {

        this.isPlaying = true;

        Mat screenFrame = ScreenHelper.GetCurrentScreenImage();
        List<Battle> battles = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_SMALL_CITY), false, true, false, Constants.MATCHING_PRECISION);
        battles.addAll(CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_BIG_CITY), false, true, false, Constants.MATCHING_PRECISION));
        if (battles.isEmpty()) {
            System.out.println("Bitke su prazne");
        } else {
            battles.removeAll(findSameBattles(battles));
        }

        while (true) {

            sleep(500);
            if (battles.isEmpty()) {
                break;
            }

            if (battles.size() > 0) {

                int iteration_per_battle = 5;
                Battle closestBattle = battles.remove(0);

                while (iteration_per_battle > 0) {

                    screenFrame = ScreenHelper.GetCurrentScreenImage();
                    List<Battle> movie = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_FORWARD), true, false, false, Constants.BLOCKED_PRECISION);
                    if (!movie.isEmpty()) {
                        System.out.println("forwarad");
                        Battle forward = movie.get(0);
                        keyPresser.moveAndclick(forward.getX(), forward.getY());
                        continue;
                    }

                    List<Battle> blocked = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_X), true, false, false, Constants.BLOCKED_PRECISION);
                    if (blocked.size() > 0) {
                        System.out.println("hydra x");
                        Battle x = blocked.get(0);
                        keyPresser.moveAndclick(x.getX(), x.getY());
                        continue;
                    }

                    this.fight(closestBattle);

                    iteration_per_battle--;
                    sleep(500);

                }

            } else {
                continue;
            }

        }

    }

    public List<Battle> findSameBattles(List<Battle> battles) {

        List<Battle> sameBattles = new ArrayList<>();

        for (int i = 0; i < battles.size() - 1; i++) {
            if (sameBattles.contains(battles.get(i))) {
                continue;
            }
            for (int j = i + 1; j < battles.size(); j++) {
                if (!sameBattles.contains(battles.get(j)) && battles.get(i).similiar(battles.get(j))) {
                    sameBattles.add(battles.get(j));
                }
            }
        }

        return sameBattles;

    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void fight(Battle battle) {

        keyPresser.moveAndclick(battle.getX(), battle.getY());

        pressButton(Constants.HYDRA_FIGHT, false);
        pressButton(Constants.HYDRA_FORWARD, false);
        sleep(500);

    }

    public void pressButton(String templateToClick, boolean searchOnce) {

        boolean tempShowed = searchOnce;
        int limit = 20;

        while (limit > 0) {

            sleep(100);

            Mat screenFrame = ScreenHelper.GetCurrentScreenImage();
            List<Battle> tempMatching = CV.matchingHydraTemplates(screenFrame, this.templates.get(templateToClick), true, false, false, Constants.MATCHING_PRECISION);
            limit--;

            if (tempMatching.size() > 0) {
                tempShowed = true;
                keyPresser.moveAndclick(tempMatching.get(0).getX(), tempMatching.get(0).getY());
            } else if (tempShowed && tempMatching.isEmpty()) return;
            else continue;
        }
    }

    @Override
    public void stop() { this.shouldPlay = false;}

    @Override
    public void start() { this.shouldPlay = true; }

    @Override
    public boolean isPlaying() {
        return this.isPlaying;
    }

    @Override
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

}
