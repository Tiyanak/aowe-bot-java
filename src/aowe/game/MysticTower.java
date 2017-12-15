package aowe.game;

import aowe.helper.CV;
import aowe.helper.Constants;
import aowe.helper.KeyPresser;
import aowe.helper.ScreenHelper;
import aowe.model.Battle;
import javafx.stage.Screen;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.*;

/**
 * Created by Igor Farszky on 21.9.2017..
 */
public class MysticTower implements Game {

    private boolean isPlaying;
    private Map<String, Mat> templates;
    private KeyPresser keyPresser;

    public MysticTower() {
        isPlaying = false;
        this.keyPresser = new KeyPresser();
        initTemplates();
    }

    public void initTemplates() {
        this.templates = new HashMap<>();

        for (String mystic_temp : Constants.ASSETS) {
            String path = Constants.AOWE_ASSETS + mystic_temp + Constants.PNG_EXT;
            try {
                this.templates.put(mystic_temp, Imgcodecs.imread(path));
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void play() {

        this.isPlaying = true;

        while (true) {

            Mat screenFrame = ScreenHelper.GetCurrentScreenImage();
            List<Battle> mystic_over = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.GEM_FULL_CONFIRM), true, true, false, Constants.MATCHING_EMPTY);
            if (!mystic_over.isEmpty()) {
                Battle over = mystic_over.get(0);
                keyPresser.moveAndclick(over.getX(), over.getY());
                break;
            }
            List<Battle> cities = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_SMALL_CITY_BURNING), true, true, false, Constants.MATCHING_EMPTY);
            if (cities.isEmpty()) {
                System.out.println("no smalls");
                cities = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_BIG_CITY_BURNING), true, true, false, Constants.MATCHING_EMPTY);
                if (cities.isEmpty()) {
                    System.out.println("no bigs");
                    continue;
                }
            }

            boolean finished = false;
            Battle city = cities.get(0);
            keyPresser.moveAndclick(city.getX(), city.getY());
            sleep(300);

            medicalCenter();

            while(!finished) {

                screenFrame = ScreenHelper.GetCurrentScreenImage();

                List<Battle> blocked = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_X), false, false, false, Constants.MATCHING_EMPTY);
                if (blocked.size() > 0) {
                    System.out.println("blocked");
                    Battle x = blocked.get(0);
                    keyPresser.moveAndclick(x.getX(), x.getY());
                    continue;
                }

                List<Battle> me = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_ME_RIGHT), true, false, false, Constants.MATCHING_PRECISION);
                me.addAll(CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_ME_LEFT), true, false, false, Constants.MATCHING_PRECISION));
                if (!me.isEmpty()) {

                    List<Battle> battles = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_BATTLE), false, false, false, Constants.MATCHING_PRECISION);
                    List<Battle> chests = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_CHEST_BATTLE), false, false, false, Constants.MATCHING_PRECISION);
                    List<Battle> boss = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_BOSS), true, false, false, Constants.MATCHING_PRECISION);

                    List<Battle> battles_chests = battles;
                    battles_chests.addAll(chests);

                    if (battles_chests.size() > 0) {
                        Battle closestBattle = findClosestBattle(battles_chests, me.get(0));
                        if (chests.contains(closestBattle)) {
                            openChest(closestBattle);
                            sleep(500);
                        } else {
                            fight(closestBattle);
                            sleep(500);
                        }
                    } else if (boss.size() > 0) {
                        handleBoss(boss.get(0));
                        finished = true;
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    @Override
    public void fromLeft() {

    }

    @Override
    public void fromBottom() {

    }

    @Override
    public void fromUp() {

    }

    @Override
    public void fromRight() {

    }

    private void medicalCenter() {
        pressButton(Constants.TOWER_BARRACKS, false);
        sleep(300);
        pressButton(Constants.TOWER_MEDICAL, false);
        sleep(300);
        pressButton(Constants.TOWER_TREAT, true);
        pressButton(Constants.HYDRA_X, false);
    }

    public void handleBoss(Battle boss) {
        fight(boss);
        sleep(500);

        keyPresser.click();
        sleep(1000);

        pressButton(Constants.TOWER_BOSS_CHEST, false);
        sleep(500);
    }


    public void fight(Battle battle) {


        pressUntilFight(battle);
        pressButton(Constants.HYDRA_FIGHT, false);
        pressButton(Constants.HYDRA_FORWARD, false);

    }

    public void pressUntilFight(Battle battle) {

        while(true) {
            keyPresser.moveAndclick(battle.getX(), battle.getY());
            Mat screenFrame = ScreenHelper.GetCurrentScreenImage();
            List<Battle> fight_list = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_FIGHT), true, false, false, Constants.MATCHING_PRECISION);
            if (fight_list.isEmpty()) {
                sleep(100);
                continue;
            } else {
                break;
            }
        }
    }

    public void openChest(Battle chest) {

        keyPresser.moveAndclick(chest.getX(), chest.getY());
        sleep(2000);

    }

    public void pressButton(String templateToClick, boolean searchOnce) {

        boolean tempShowed = searchOnce;
        int limit = 10;

        while (limit > 0) {

            Mat screenFrame = ScreenHelper.GetCurrentScreenImage();
            List<Battle> tempMatching = CV.matchingHydraTemplates(screenFrame, this.templates.get(templateToClick), true, false, false, Constants.BLOCKED_PRECISION);
            limit--;

            if (tempMatching.size() > 0) {
                tempShowed = true;
                keyPresser.moveAndclick(tempMatching.get(0).getX(), tempMatching.get(0).getY());
            } else if (tempShowed && tempMatching.size() == 0) return;
            else continue;
        }
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

    public List<Battle> findSameBattles(List<Battle> battles) {

        List<Battle> sameBattles = new ArrayList<>();

        for (int i = 0; i < battles.size() - 1; i++) {
            if (sameBattles.contains(battles.get(i))) continue;
            for (int j = i + 1; j < battles.size(); j++)
                if (!sameBattles.contains(battles.get(j)) && battles.get(i).similiar(battles.get(j)))
                    sameBattles.add(battles.get(j));

        }

        return sameBattles;

    }

    @Override
    public void stop() { }

    @Override
    public void start() { }

    @Override
    public boolean isPlaying() {
        return this.isPlaying;
    }

    @Override
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

}
