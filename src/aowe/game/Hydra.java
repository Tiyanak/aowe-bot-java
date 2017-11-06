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
 * Created by Igor Farszky on 1.7.2017..
 */
@SuppressWarnings(value = "all")
public class Hydra implements Game {

    private Map<String, Mat> templates;
    private KeyPresser keyPresser;
    private boolean isPlaying;
    private boolean shouldPlay;
    private boolean untilDead;
    private int hearts, numOfResets;

    public Hydra() {
        this.keyPresser = new KeyPresser();
        initTemplates();
        this.isPlaying = false;
        this.shouldPlay = true;
        this.untilDead = false;
        this.hearts = 5;
        this.numOfResets = 5;
    }

    public Hydra(int numOfResets) {
        this.keyPresser = new KeyPresser();
        this.isPlaying = false;
        this.shouldPlay = true;
        this.untilDead = false;
        this.hearts = 5;
        this.numOfResets = numOfResets;
        initTemplates();
    }


    public void initTemplates() {
        this.templates = new HashMap<>();

        for (String hydra_temp : Constants.ASSETS) {
            String path = Constants.AOWE_ASSETS + hydra_temp + Constants.PNG_EXT;
            try {
                this.templates.put(hydra_temp, Imgcodecs.imread(path));
            } catch (Exception e) {
            }
        }
    }

    public void play() {

        this.isPlaying = true;
        Mat screenFrame = ScreenHelper.GetCurrentScreenImage();
        List<Battle> hydra_hearts_init = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_HEART), false, false, false, Constants.MATCHING_HEARTS);
        hydra_hearts_init.removeAll(findSameBattles(hydra_hearts_init));
        this.hearts = hydra_hearts_init.size();

        while (this.untilDead || this.numOfResets >= 0) {

            if (!shouldPlay) {
                sleep(5000);
                continue;
            }

            screenFrame = ScreenHelper.GetCurrentScreenImage();

            if (this.untilDead) {
                List<Battle> aowe_hero_tavern = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.AOWE_HERO_TAVERN), true, false, false, Constants.MATCHING_PRECISION);
                if (aowe_hero_tavern.size() > 0) {
                    this.isPlaying = false;
                    System.exit(0);
                }
            } else {
                List<Battle> hydra_hearts = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_HEART), false, false, false, Constants.MATCHING_HEARTS);
                hydra_hearts.removeAll(findSameBattles(hydra_hearts));
                if (hydra_hearts.size() < this.hearts && hydra_hearts.size() > 0) {
                    if (numOfResets <= 0) {
                        System.exit(0);
                    } else {
                        resetHydra();
                        this.hearts = hydra_hearts.size();
                    }
                } else if (hydra_hearts.size() == this.hearts) {
                    //skip
                } else {
                    List<Battle> hydra_pay_reset = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_PAY_RESET), true, false, false, Constants.MATCHING_HEARTS);
                    if (!hydra_pay_reset.isEmpty()) {
                        Battle hydra_pay_reset_btn = hydra_pay_reset.get(0);
                        keyPresser.moveAndclick(hydra_pay_reset_btn.getX(), hydra_pay_reset_btn.getY());
                        sleep(200);
                        this.numOfResets--;
                        resetHydra();
                    }
                    this.hearts = hydra_hearts.size();
                }
            }

            List<Battle> movie = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_FORWARD_SHADE), true, false, false, Constants.BLOCKED_PRECISION);
            if (!movie.isEmpty()) {
                Battle forward = movie.get(0);
                keyPresser.moveAndclick(forward.getX(), forward.getY());
                continue;
            }

            List<Battle> blocked = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_X), true, false, false, Constants.BLOCKED_PRECISION);
            if (blocked.size() > 0) {
                Battle x = blocked.get(0);
                keyPresser.moveAndclick(x.getX(), x.getY());
                continue;
            }

            List<Battle> me = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_ME), true, true, true, Constants.MATCHING_PRECISION);
            if (me.isEmpty()) {
                continue;
            }

            List<Battle> battles = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_BATTLE), false, true, false, Constants.MATCHING_PRECISION);
            if (battles.isEmpty()) {
                continue;
            } else {
                battles.removeAll(findSameBattles(battles));
            }

            List<Battle> bossesTemp = CV.matchingHydraTemplates(screenFrame, templates.get(Constants.HYDRA_BOSS), true, true, false, Constants.MATCHING_PRECISION);

            if (bossesTemp.size() > 0) {
                List<Battle> searchBoss = CV.matchingHydraTemplates(screenFrame, templates.get(Constants.HYDRA_EMPTY), false, true, false, Constants.MATCHING_EMPTY);
                searchBoss.removeAll(findSameBattles(searchBoss));
                searchBoss.add(me.get(0));
                searchBoss.add(bossesTemp.get(0));
                this.bossFight(searchBoss, me.get(0), bossesTemp.get(0), battles);
            } else if (battles.size() > 0) {
                Battle closestBattle = this.findClosestBattle(battles, me.get(0));
                this.fight(closestBattle);
            } else {
                continue;
            }

        }

    }

    private void resetHydra() {

        pressButton(Constants.HYDRA_X, false);
        clickUntilSee(Constants.HYDRA_EXIT, Constants.EVENTS);
        pressButton(Constants.HYDRA_X, false);
        clickUntilSee(Constants.EVENTS, Constants.LIMITED_EVENTS);
        pressButton(Constants.LIMITED_EVENTS, false);

        Mat screen = null;
        for (int i = 0; i < 6; i++) {
            screen = ScreenHelper.GetCurrentScreenImage();
            List<Battle> hydra_event = CV.matchingHydraTemplates(screen, templates.get(Constants.HYDRA_EVENT), true, false, false, Constants.MATCHING_PRECISION);
            if (hydra_event.isEmpty()) {
                screen = ScreenHelper.GetCurrentScreenImage();
                List<Battle> right_arrows = CV.matchingHydraTemplates(screen, templates.get(Constants.ARROW_RIGHT_EVENTS), true, false, true, Constants.MATCHING_PRECISION);
                if (!right_arrows.isEmpty()) {
                    keyPresser.moveAndclick(right_arrows.get(0).getX(), right_arrows.get(0).getY());
                    sleep(300);
                }
            } else {
                Battle hydra = hydra_event.get(0);
                clickUntilSee(Constants.HYDRA_EVENT, Constants.HYDRA_ENTER);
            }
        }

        clickUntilSee(Constants.HYDRA_ENTER, Constants.HYDRA_BATTLE);

    }

    private void clickUntilSee(String clickAsset, String seeAsset) {

        Mat screen = null;

        while (true) {

            screen = ScreenHelper.GetCurrentScreenImage();
            List<Battle> click_list = CV.matchingHydraTemplates(screen, templates.get(clickAsset), true, false, false, Constants.MATCHING_PRECISION);
            if (!click_list.isEmpty()) {
                Battle click = click_list.get(0);
                keyPresser.moveAndclick(click.getX(), click.getY());
                sleep(500);
            }

            screen = ScreenHelper.GetCurrentScreenImage();
            List<Battle> see_list = CV.matchingHydraTemplates(screen, templates.get(seeAsset), true, false, false, Constants.MATCHING_PRECISION);
            if (!see_list.isEmpty()) {
                break;
            }

        }

    }

    private boolean bossFight(List<Battle> searchBoss, Battle me, Battle boss, List<Battle> battles) {

        boolean fromFirst = true;

        while (true) {

            if (battleSearchAStar(me, boss, searchBoss)) {
                this.fight(boss);
                break;
            } else {
                searchBoss.remove(boss);
                boss = findClosestBattle(battles, boss);
                battles.remove(boss);
                searchBoss.add(boss);
                fromFirst = false;
            }

        }

        return fromFirst;

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

        keyPresser.moveAndclick(battle.getX(), battle.getY());

        pressButton(Constants.HYDRA_FIGHT, false);
        pressButton(Constants.HYDRA_FORWARD, false);

    }

    public void pressButton(String templateToClick, boolean searchOnce) {

        boolean tempShowed = searchOnce;
        int limit = 20;

        while (limit > 0) {

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
    public void stop() {
        this.shouldPlay = false;
    }

    @Override
    public void start() {
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

    public boolean battleSearchAStar(Battle start, Battle goal, List<Battle> searchBattles) {
        List<Battle> closedSet = new ArrayList<>(), openSet = new ArrayList<>();
        Map<Battle, Integer> gScore = new HashMap<>(), fScore = new HashMap<>();

        openSet.add(start);
        gScore.put(start, 0);
        fScore.put(start, cityBlockDistance(start, goal));

        while (!openSet.isEmpty()) {

            Battle current = getLowestScore(openSet, fScore);
            if (current.similiar(goal)) return true;
            openSet.remove(current);
            if (!closedSet.contains(current)) closedSet.add(current);

            for (Battle neighbour : getNeightbours(current, searchBattles)) {
                if (closedSet.contains(neighbour)) continue;
                if (!openSet.contains(neighbour)) openSet.add(neighbour);
                int distanceStartNeighbor = gScore.get(current) + cityBlockDistance(current, neighbour);
                if (gScore.containsKey(neighbour) && distanceStartNeighbor >= gScore.get(neighbour)) continue;

                gScore.put(neighbour, distanceStartNeighbor);
                fScore.put(neighbour, cityBlockDistance(neighbour, goal));
            }
        }
        return false;
    }

    private List<Battle> getNeightbours(Battle current, List<Battle> searchbattles) {
        List<Battle> neighbors = new ArrayList<>();
        for (Battle b : searchbattles) if (current.neighborWith(b)) neighbors.add(b);
        return neighbors;
    }

    public Battle getLowestScore(List<Battle> nodes, Map<Battle, Integer> scores) {
        Battle lowestScoreBattle = new Battle(0, 0);
        Integer minScore = 9999;
        for (Battle b : nodes) {
            if (!scores.containsKey(b)) continue;

            if (scores.get(b) < minScore) {
                lowestScoreBattle = b;
                minScore = scores.get(b);
            }
        }
        return lowestScoreBattle;
    }

    public boolean isUntilDead() {
        return untilDead;
    }

    public void setUntilDead(boolean untilDead) {
        this.untilDead = untilDead;
    }
}
