package aowe.game;

import aowe.helper.CV;
import aowe.helper.Constants;
import aowe.helper.KeyPresser;
import aowe.helper.ScreenHelper;
import aowe.model.Battle;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.util.*;

/**
 * Created by Igor Farszky on 1.7.2017..
 */
@SuppressWarnings(value = "all")
public class Hydra implements Game{

    private Map<String, Mat> templates;
    private int levels;
    private KeyPresser keyPresser;
    private boolean isPlaying;
    private boolean shouldPlay;
    private boolean untilDead;

    public Hydra() {
        this.keyPresser = new KeyPresser();
        initTemplates();
        levels = 999*10;
        this.isPlaying = false;
        this.shouldPlay = true;
        this.untilDead = false;
    }

    public Hydra(int levels) {
        this.keyPresser = new KeyPresser();
        this.levels = levels*10;
        this.isPlaying = false;
        this.shouldPlay = true;
        this.untilDead = false;
        initTemplates();
    }


    public void initTemplates() {
        this.templates = new HashMap<>();

        for (String hydra_temp : Constants.HYDRA_TEMPLATES) {
            String path = Constants.AOWE_ASSETS + hydra_temp + Constants.PNG_EXT;
            try {
                this.templates.put(hydra_temp, Imgcodecs.imread(path));
            } catch (Exception e) {
            }
        }
    }

    public void play() {

        this.isPlaying = true;

        while (this.levels > 0 || this.untilDead) {

            if (!shouldPlay){
                sleep(1000);
                continue;
            }

            this.sleep(200);

            Mat screenFrame = ScreenHelper.GetCurrentScreenImage();

            List<Battle> aowe_hero_tavern = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.AOWE_HERO_TAVERN), true, false, false, Constants.MATCHING_PRECISION);
            if (aowe_hero_tavern.size() > 0){
                this.isPlaying = false;
                System.exit(0);
            }

            if (!this.untilDead) {
                List<Battle> hydra_hearts = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_HEART), false, false, false, Constants.MATCHING_HEARTS);
                hydra_hearts.removeAll(findSameBattles(hydra_hearts));
                if (hydra_hearts.size() == 1) {
                    System.exit(0);
                }
            }

            List<Battle> blocked = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_X), true, false, false, Constants.BLOCKED_PRECISION);
            List<Battle> battleMovie = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_FORWARD_SHADE), true, false, false, Constants.BLOCKED_PRECISION);

            if (blocked.size() > 0) {
                Battle x = blocked.get(0);
                keyPresser.click(x.getX(), x.getY());
                continue;
            }else if(battleMovie.size() > 0){
                Battle bm = battleMovie.get(0);
                keyPresser.click(bm.getX(), bm.getY());
                continue;
            }

            List<Battle> me = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_ME), true, false, true, Constants.MATCHING_PRECISION);
            if (me.isEmpty()) {
                continue;
            }

            List<Battle> battles = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.HYDRA_BATTLE), false, false, false, Constants.MATCHING_PRECISION);
            if (battles.isEmpty()) {
                continue;
            } else {
                battles.removeAll(findSameBattles(battles));
            }

            List<Battle> bossesTemp = CV.matchingHydraTemplates(screenFrame, templates.get(Constants.HYDRA_BOSS), true, false, false, Constants.MATCHING_PRECISION);
            this.levels--;

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

    private void bossFight(List<Battle> searchBoss, Battle me, Battle boss, List<Battle> battles) {

        while (true){

            if (battleSearchAStar(me, boss, searchBoss)){
                this.fight(boss);
                break;
            }else{
                searchBoss.remove(boss);
                boss = findClosestBattle(battles, boss);
                battles.remove(boss);
                searchBoss.add(boss);
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

        keyPresser.click(battle.getX(), battle.getY());

        pressButton(Constants.HYDRA_FIGHT, false);
        sleep(500);
        pressButton(Constants.HYDRA_FORWARD, true);
        sleep(500);
        pressButton(Constants.HYDRA_FORWARD_SHADE, true);
        sleep(200);

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
            } else if (tempShowed && tempMatching.size() == 0) return;
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

    public boolean battleSearchAStar(Battle start, Battle goal, List<Battle> searchBattles){
        List<Battle> closedSet = new ArrayList<>(), openSet = new ArrayList<>();
        Map<Battle, Integer> gScore = new HashMap<>(), fScore = new HashMap<>();

        openSet.add(start);
        gScore.put(start, 0);
        fScore.put(start, cityBlockDistance(start, goal));

        while (!openSet.isEmpty()){

            Battle current = getLowestScore(openSet, fScore);
            if (current.similiar(goal)) return true;
            openSet.remove(current);
            if (!closedSet.contains(current)) closedSet.add(current);

            for (Battle neighbour : getNeightbours(current, searchBattles)){
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

    public Battle getLowestScore(List<Battle> nodes, Map<Battle, Integer> scores){
        Battle lowestScoreBattle = new Battle(0, 0);
        Integer minScore = 9999;
        for (Battle b : nodes){
            if (!scores.containsKey(b)) continue;

            if (scores.get(b) < minScore){
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
