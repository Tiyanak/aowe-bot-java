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
public class Tower implements Game {

    private boolean isPlaying;
    private Map<String, Mat> templates;
    private KeyPresser keyPresser;

    public Tower() {
        isPlaying = false;
        this.keyPresser = new KeyPresser();
        System.out.println("Tower");
        initTemplates();
    }

    public void initTemplates() {
        this.templates = new HashMap<>();
        System.out.println("init templates");

        for (String tower_temp : Constants.ASSETS) {
            String path = Constants.AOWE_ASSETS + tower_temp + Constants.PNG_EXT;
            try {
                this.templates.put(tower_temp, Imgcodecs.imread(path));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @Override
    public void play() {

        this.isPlaying = true;


        while (true) {

            Mat screenFrame = ScreenHelper.GetCurrentScreenImage();

            List<Battle> mystic_over = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.GEM_FULL_CONFIRM), true, false, false, Constants.MATCHING_PRECISION);
            if (!mystic_over.isEmpty()) {
                System.out.println("Tower over");
                break;
            }

            List<Battle> tower_over_list = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_OVER), true, false, false, Constants.MATCHING_PRECISION);
            if (!tower_over_list.isEmpty()) {
                System.out.println("Tower over");
                break;
            }

            List<Battle> blocked = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_X), false, false, false, Constants.MATCHING_EMPTY);
            if (blocked.size() > 0) {
                System.out.println("Hydra X");
                Battle x = blocked.get(0);
                keyPresser.moveAndclick(x.getX(), x.getY());
                continue;
            }

            List<Battle> movie_forward = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_FORWARD), false, false, false, Constants.MATCHING_PRECISION);
            if (movie_forward.size() > 0) {
                System.out.println("Hydra forward");
                Battle x = movie_forward.get(0);
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

                if (boss.size() > 0) {
                    handleBoss(boss.get(0));
                } else if (battles_chests.size() > 0) {
                    Battle closestBattle = findClosestBattle(battles_chests, me.get(0));
                    if (chests.contains(closestBattle)) {
                        openChest(closestBattle);
                    } else {
                        fight(closestBattle);
                    }
                } else {
                    System.out.println("Found nothing");
                    continue;
                }

            } else {
                List<Battle> big_burning = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_BIG_CITY_BURNING), true, false, false, Constants.MATCHING_EMPTY);
                if (big_burning.size() > 0) {
                    System.out.println("Burning big city");
                    Battle x = big_burning.get(0);
                    keyPresser.moveAndclick(x.getX(), x.getY());
                    sleep(300);

                    screenFrame = ScreenHelper.GetCurrentScreenImage();
                    List<Battle> confirm = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.GEM_FULL_CONFIRM), true, false, false, Constants.MATCHING_EMPTY);
                    if (!confirm.isEmpty()) {
                        Battle confirmBtn = confirm.get(0);
                        keyPresser.moveAndclick(confirmBtn.getX(), confirmBtn.getY());
                        sleep(200);
                    }
                } else {
                    List<Battle> big = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.TOWER_BIG_CITY), true, false, false, Constants.MATCHING_EMPTY);
                    if (big.size() > 0) {
                        System.out.println("Big city");
                        Battle x = big.get(0);
                        keyPresser.moveAndclick(x.getX(), x.getY());
                        sleep(200);

                        screenFrame = ScreenHelper.GetCurrentScreenImage();
                        List<Battle> confirm = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.GEM_FULL_CONFIRM), true, false, false, Constants.MATCHING_EMPTY);
                        if (!confirm.isEmpty()) {
                            Battle confirmBtn = confirm.get(0);
                            keyPresser.moveAndclick(confirmBtn.getX(), confirmBtn.getY());
                            sleep(200);
                        }
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

    public void handleBoss(Battle boss) {

        fight(boss);
        sleep(500);

        clickUntilSee(Constants.GOLDEN_STAR, Constants.TOWER_BOSS_CHEST);
        clickUntilSee(Constants.TOWER_BOSS_CHEST, Constants.UNCHARTED_GOLD_PAY);
//        clickUntilSee(Constants.TOWER_BOSS_CHEST, Constants.TOWER_BIG_CITY_BURNING);
        clickUntilSee(Constants.UNCHARTED_GOLD_PAY, Constants.TOWER_BIG_CITY_BURNING);

    }

    private void clickUntilSee(String clickAsset, String seeAsset) {

        Mat screen = null;

        while (true) {

            screen = ScreenHelper.GetCurrentScreenImage();
            List<Battle> click_list = CV.matchingHydraTemplates(screen, templates.get(clickAsset), true, false, false, Constants.MATCHING_PRECISION);
            if (!click_list.isEmpty()) {
                Battle click = click_list.get(0);
                keyPresser.moveAndclick(click.getX(), click.getY());
                sleep(200);
            }

            screen = ScreenHelper.GetCurrentScreenImage();
            List<Battle> see_list = CV.matchingHydraTemplates(screen, templates.get(seeAsset), true, false, false, Constants.MATCHING_PRECISION);
            if (!see_list.isEmpty()) {
                break;
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
        int limit = 5;

        while (limit > 0) {

            sleep(200);

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
