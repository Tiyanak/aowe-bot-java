package aowe.game;

import aowe.helper.CV;
import aowe.helper.Constants;
import aowe.helper.KeyPresser;
import aowe.helper.ScreenHelper;
import aowe.model.Battle;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.*;

/**
 * Created by Igor Farszky on 21.9.2017..
 */
public class MysticTower implements Game {

    private boolean shouldPlay;
    private boolean isPlaying;
    private Map<String, Mat> templates;
    private KeyPresser keyPresser;

    public MysticTower() {
        this.shouldPlay = true;
        isPlaying = false;
        this.keyPresser = new KeyPresser();
        initTemplates();
    }

    public void initTemplates() {
        this.templates = new HashMap<>();

        for (String mystic_temp : Constants.MYSTIC_TOWER_TEMPLATES) {
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
        boolean finished = false;

        while (!finished) {

            Mat screenFrame = ScreenHelper.GetCurrentScreenImage();

            List<Battle> blocked = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_X), true, false, false, Constants.MATCHING_EMPTY);
            if (blocked.size() > 0) {
                System.out.println("blocked");
                Battle x = blocked.get(0);
                keyPresser.moveAndclick(x.getX(), x.getY());
                continue;
            }

            List<Battle> me = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_ME_RIGHT), true, false, false, Constants.MATCHING_PRECISION);
            me.addAll(CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_ME_LEFT), true, false, false, Constants.MATCHING_PRECISION));
            if (me.isEmpty()) {
                continue;
            }

            List<Battle> battles = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_BATTLE), false, false, false, Constants.MATCHING_PRECISION);
            List<Battle> chests = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_CHEST_BATTLE), false, false, false, Constants.MATCHING_PRECISION);
            List<Battle> boss = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_BOSS), true, false, false, Constants.MATCHING_PRECISION);

            List<Battle> battles_chests = battles;
            battles_chests.addAll(chests);

            if (battles_chests.size() > 0) {
                battles_chests.removeAll(findSameBattles(battles_chests));
                Battle closestBattle = findClosestBattle(battles_chests, me.get(0));
                if (chests.contains(closestBattle)) {
                    openChest(closestBattle);
                    sleep(500);
                } else {
                    fight(closestBattle);
                    sleep(500);
                }
            } else if (boss.size() > 0) {
                fight(boss.get(0));
                sleep(500);
                keyPresser.moveAndclick(100, 100);
                sleep(500);
                screenFrame = ScreenHelper.GetCurrentScreenImage();
                int counterLimit = 20;
                while (true) {
                    List<Battle> win_chests = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_FINISH_CHEST), false, false, false, Constants.MATCHING_PRECISION);
                    if (!win_chests.isEmpty()) {
                        keyPresser.moveAndclick(win_chests.get(0).getX(), win_chests.get(0).getY());
                        break;
                    } else if (counterLimit == 0) {
                        break;
                    } else {
                        counterLimit--;
                    }
                }
                finished = true;
            } else {
                finished = true;
            }

        }

    }

    public void fight(Battle battle) {

        keyPresser.moveAndclick(battle.getX(), battle.getY());

        pressButton(Constants.HYDRA_FIGHT, false);
        pressButton(Constants.HYDRA_FORWARD, false);

    }

    public void openChest(Battle chest) {

        keyPresser.moveAndclick(chest.getX(), chest.getY());
        sleep(2000);

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