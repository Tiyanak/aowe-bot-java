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
 * Created by Igor Farszky on 1.7.2017..
 */
public class Hydra implements Game{

    private Map<String, Mat> templates;
    private int lives;
    private KeyPresser keyPresser;
    private boolean isPlaying;
    private boolean shouldPlay;

    public Hydra() {
        this.keyPresser = new KeyPresser();
        initTemplates();
        lives = 5;
        this.isPlaying = false;
        this.shouldPlay = true;
    }

    public void initTemplates() {
        this.templates = new HashMap<>();

        System.out.println("STARTING INITIALIZING TEMPLATES");
        for (String hydra_temp : Constants.HYDRA_TEMPLATES) {
            String path = Constants.AOWE_ASSETS + hydra_temp + Constants.PNG_EXT;
            try {
                this.templates.put(hydra_temp, Imgcodecs.imread(path));
            } catch (Exception e) {
                System.out.println("CANT FIND THE TEMPLATE PATH");
            }
        }

        System.out.println("FINISHED INITIALIZING TEMPLATES");

    }

    public void play() {

        this.isPlaying = true;

        while (this.lives > 2) {

            if (!shouldPlay){
                sleep(1000);
                continue;
            }

            this.sleep(200);

            Mat screenFrame = ScreenHelper.GetCurrentScreenImage();

            List<Battle> aowe_hero_tavern = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.AOWE_HERO_TAVERN), true, false, false, Constants.MATCHING_PRECISION);
            if (aowe_hero_tavern.size() > 0){
                System.out.println("HYDRA FINISHED!");
                this.isPlaying = false;
                System.exit(0);
            }

            List<Battle> blocked = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_X), true, false, false, Constants.BLOCKED_PRECISION);
            List<Battle> battleMovie = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_FORWARD_SHADE), true, false, false, Constants.BLOCKED_PRECISION);

            if (blocked.size() > 0) {
                System.out.println("ILEGAL WINDOW -> CLOSING IT");
                Battle x = blocked.get(0);
                keyPresser.click(x.getX(), x.getY());
                continue;
            }else if(battleMovie.size() > 0){
                System.out.println("SKIPING BATTLE MOVIE -> CLOSING IT");
                Battle bm = battleMovie.get(0);
                keyPresser.click(bm.getX(), bm.getY());
                continue;
            }

            List<Battle> me = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_ME), true, false, true, Constants.MATCHING_PRECISION);
            if (me.isEmpty()) {
                System.out.println("NO ME IN GAME -> RETRYING");
                continue;
            }

            List<Battle> battles = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_BATTLE), false, false, false, Constants.MATCHING_PRECISION);
            if (battles.isEmpty()) {
                System.out.println("NO BATTLES IN GAME -> RETRYING");
                continue;
            } else {
                battles.removeAll(findSameBattles(battles));
            }

            List<Battle> bossesTemp = CV.matchingHydraTemplates(screenFrame, templates.get(Constants.HYDRA_BOSS), true, false, false, Constants.MATCHING_PRECISION);

            if (bossesTemp.size() > 0) {
                this.bossFight(battles, bossesTemp.get(0), me.get(0));
            } else if (battles.size() > 0) {
                Battle closestBattle = this.findClosestBattle(battles, me.get(0));
                this.fight(closestBattle);
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

    private Battle findClosestBattle(List<Battle> battles, Battle me) {

        Battle closestBattle = new Battle(0, 0);
        int min = 9999;
        for (Battle b : battles) {
            int distance = this.cityBlockDistance(me, b);
            if (distance < min) {
                min = distance;
                closestBattle = b;
            }
        }

        return closestBattle;

    }

    private int cityBlockDistance(Battle me, Battle b) {

        return Math.abs(me.getX() - b.getX()) + Math.abs(me.getY() - b.getY());

    }

    public void fight(Battle battle) {

        System.out.println("FIGHTING -> " + battle.toString());

        keyPresser.click(battle.getX(), battle.getY());

        pressButton(Constants.HYDRA_FIGHT, false);
        pressButton(Constants.HYDRA_FORWARD, true);
        sleep(500);
        pressButton(Constants.HYDRA_FORWARD_SHADE, true);

    }

    public void doBossFight(Battle battle){

        System.out.println("FIGHTING BOSS -> " + battle.toString());

        keyPresser.click(battle.getX(), battle.getY());
        sleep(2000);

        Mat screenFrame = ScreenHelper.GetCurrentScreenImage();
        List<Battle> isFight = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_FIGHT), true, false, false, Constants.MATCHING_PRECISION);

        if (isFight.size() == 0){
            List<Battle> pressBuff = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_BUFF), true, false, false, Constants.MATCHING_PRECISION);
            if (pressBuff.size() > 0){
                keyPresser.click(pressBuff.get(0).getX(), pressBuff.get(0).getY());
            }
            return;
        }

        pressButton(Constants.HYDRA_FIGHT, false);
        pressButton(Constants.HYDRA_FORWARD, true);
        sleep(500);
        pressButton(Constants.HYDRA_FORWARD_SHADE, true);

    }

    public void bossFight(List<Battle> battles, Battle boss, Battle me) {

        doBossFight(boss);
        sleep(100);

        Mat screenFrame = ScreenHelper.GetCurrentScreenImage();
        List<Battle> blocked = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_X), true, false, false, Constants.BLOCKED_PRECISION);

        if (blocked.size() == 0) {
            return;
        } else {
            System.out.println("BOSS BLOCKED");
            Battle x = blocked.get(0);
            keyPresser.click(x.getX(), x.getY());
        }

        while (battles.size() > 0) {

            Battle bestBattleCandidate = this.findClosestBattle(battles, boss);

            doBossFight(bestBattleCandidate);
            sleep(200);

            screenFrame = ScreenHelper.GetCurrentScreenImage();
            blocked = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_X), true, false, false, Constants.BLOCKED_PRECISION);

            if (blocked.size() == 0) {
                return;
            } else {
                System.out.println("CANDIDATE BLOCKED");
                Battle x = blocked.get(0);
                keyPresser.click(x.getX(), x.getY());
                battles.remove(bestBattleCandidate);
                continue;
            }

        }
    }

    public void pressButton(String templateToClick, boolean searchOnce) {

        boolean tempShowed = searchOnce;
        int limit = 20;

        while (limit > 0) {

            Mat screenFrame = ScreenHelper.GetCurrentScreenImage();
            List<Battle> tempMatching = CV.matchingHydraTemplates(screenFrame, this.templates.get(templateToClick), true, false, false, Constants.BLOCKED_PRECISION);
            limit--;

            if (tempMatching.size() > 0) {
                tempShowed = true;
                keyPresser.click(tempMatching.get(0).getX(), tempMatching.get(0).getY());
            } else if (tempShowed && tempMatching.size() == 0) {
                return;
            } else {
                continue;
            }

        }

    }

    @Override
    public void stop() {
        System.out.println("PAUSING HYDRA");
        this.shouldPlay = false;
    }

    @Override
    public void start() {
        System.out.println("STARTING HYDRA AGAIN");
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
